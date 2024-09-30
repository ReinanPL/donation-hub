package com.compass.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

import com.compass.DAO.DistributionCenterDAO;
import com.compass.DAO.impl.DistributionCenterDAOImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import com.compass.DAO.DonationDAO;
import com.compass.DAO.OrderDAO;
import com.compass.DAO.ShelterDAO;
import com.compass.DAO.impl.DonationDAOImpl;
import com.compass.DAO.impl.OrderDAOImpl;
import com.compass.DAO.impl.ShelterDAOImpl;
import com.compass.model.Donation;
import com.compass.model.Order;
import com.compass.model.Shelter;
import com.compass.model.enums.ItemType;
import com.compass.model.enums.OrderStatus;
import com.compass.service.OrderService;
import com.compass.util.EntityManagerFactorySingleton;

public class OrderServiceImpl implements OrderService {

	private final EntityManager em = EntityManagerFactorySingleton.getInitDb();
	private final Scanner sc = new Scanner(System.in);
	private final OrderDAO orderDAO = new OrderDAOImpl(em);
	private final ShelterDAO shelterDAO = new ShelterDAOImpl(em);
	private final DonationDAO donationDAO = new DonationDAOImpl(em);
	private final DistributionCenterDAO centerDAO = new DistributionCenterDAOImpl(em);

	@Override
	public void createOrder() {

		try {
			var order = createOrderFromInput();
			var currentQuantity = getTotalQuantityByType(order.getShelter(), order.getLot().getItemType());
	        if (currentQuantity + order.getLot().getQuantity() > 200) {
	        	System.out.println("Limite de estoque do abrigo excedido");
	            order.setStatus(OrderStatus.DENIED);
	            order.setRefusalMotive("Limite de estoque do abrigo excedido");
	        }

			orderDAO.create(order);
			System.out.println("\n=== Pedido criado com sucesso! ===");
			System.out.println(order);
		} catch (Exception e) {
			System.err.println("Erro inesperado ao criar o pedido: " + e.getMessage());
		}
	}

	@Override
	public void acceptOrder() {
		try {
			findOrderByCenter();
			var id = Long.parseLong(getInput("\nDigite o ID do pedido a ser aceito: \n"));
			var order = Optional.of(orderDAO.find(id))
					.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
			Optional.of(order.getStatus())
					.filter(status -> status.equals(OrderStatus.WAITING))
					.orElseThrow(() -> new EntityNotFoundException("Ordem inválida ou já processada"));


			var donation = order.getLot().getDonations();
			var shelter = order.getShelter();

			donation.setDistributionCenter(null);
			donation.setShelter(shelter);
			order.setStatus(OrderStatus.ACCEPTED);
			orderDAO.update(order);
			donationDAO.update(donation);
			System.out.println("\n=== Pedido aceito com sucesso! ===");
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao aceitar o pedido: " + e.getMessage());

		}
	}

	@Override
	public void rejectOrder() {
		try {
			findOrderByCenter();
			var id = Long.parseLong(getInput("\nDigite o ID do pedido a ser rejeitado: \n"));
			var order = Optional.of(orderDAO.find(id))
					.orElseThrow(() -> new EntityNotFoundException("Pedido não encontrado."));
			Optional.of(order.getStatus())
					.filter(status -> status.equals(OrderStatus.WAITING))
					.orElseThrow(() ->  new EntityNotFoundException("Ordem inválida ou já processada"));


			var motive = getInput("\nMotivo da recusa do pedido: \n");
			order.setStatus(OrderStatus.DENIED);
			order.setRefusalMotive(motive);
			orderDAO.update(order);
			System.out.print("\n=== Pedido recusado com sucesso! ===\n");

		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao rejeitar o pedido: " + e.getMessage());
		}

	}

	@Override
	public void findOrderByShelter() {
		try {
			var shelters = shelterDAO.findAll();
			shelters.forEach(System.out::println);
			var shelterId = Long.parseLong(getInput("\nDigite o ID do abrigo para procurar os pedidos: "));
			var orders = Optional.of(orderDAO.getOrderByShelter(shelterId))
					.filter(list -> !list.isEmpty())
					.orElseThrow(() -> new EntityNotFoundException("Nenhum pedido encontrado para o abrigo com o ID:  " + shelterId));
			orders.forEach(System.out::println);
		} catch (EntityNotFoundException e) {
			System.err.println("Abrigo não encontrado: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao buscar pedidos: " + e.getMessage());
		}
	}

	public void findOrderByCenter() {
		var centers = centerDAO.findAll();
		centers.forEach(System.out::println);
		var centerId = Long.parseLong(getInput("\nDigite o ID do centro para procurar os pedidos: "));
		var orders = Optional.of(orderDAO.getOrderByCenter(centerId))
				.filter(list -> !list.isEmpty())
				.orElseThrow(() -> new EntityNotFoundException("Nenhum pedido encontrado do centro de distribuição com o ID:  " + centerId));
		orders.forEach(System.out::println);
	}

	public Order createOrderFromInput() {
		var num = Integer.parseInt(getInput("\nRegistro de pedidos: \n1. Roupas \n2. Produtos de Higiene \n3. Alimentos \n"));

		var itemTypeMap = Map.of(1, ItemType.CLOTHING, 2, ItemType.HYGIENE, 3, ItemType.FOOD);
		var itemType = Optional.of(itemTypeMap.get(num))
				.orElseThrow(() -> new EntityNotFoundException("Opção inválida."));
		var donations = Optional.of(donationDAO.getDonationByItemType(itemType))
				.filter(list -> !list.isEmpty())
				.orElseThrow(() -> new EntityNotFoundException("Doações não encotradas!"));
		donations.forEach(System.out::println);

		var idDonation = Long.parseLong(getInput("Id da doação: "));
		var idShelter = Long.parseLong(getInput("Id do abrigo de destino: "));

		var status = OrderStatus.WAITING;
		var dateNow = LocalDate.now();

		var donation = Optional.of(donationDAO.find(idDonation))
				.orElseThrow(() -> new EntityNotFoundException("Doação não encontrada."));
		var lotOrder = donation.getLot();

		var shelter = Optional.of(shelterDAO.find(idShelter))
				.orElseThrow(() -> new EntityNotFoundException("Abrigo não encontrado."));

		return new Order(shelter, lotOrder, status, null, dateNow, donation.getDistributionCenter());
	}
	
	public int getTotalQuantityByType(Shelter shelter, ItemType itemType) {
		return shelter.getDonations().stream()
				.filter(donation -> donation.getLot() != null && donation.getLot().getItemType() == itemType)
				.mapToInt(donation -> donation.getLot().getQuantity())
				.sum();
	}

	private String getInput(String prompt) {
		System.out.print(prompt);
		return sc.nextLine();
	}

}
