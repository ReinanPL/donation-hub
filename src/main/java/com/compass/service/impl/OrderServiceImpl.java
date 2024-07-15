package com.compass.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import com.compass.DAO.DonationDAO;
import com.compass.DAO.OrderDAO;
import com.compass.DAO.ShelterDAO;
import com.compass.DAO.impl.DonationDAOImpl;
import com.compass.DAO.impl.OrderDAOImpl;
import com.compass.DAO.impl.ShelterDAOImpl;
import com.compass.model.Donation;
import com.compass.model.Lot;
import com.compass.model.Order;
import com.compass.model.Shelter;
import com.compass.model.enums.ItemType;
import com.compass.model.enums.OrderStatus;
import com.compass.service.OrderService;
import com.compass.util.EntityManagerFactorySingleton;

public class OrderServiceImpl implements OrderService {

	EntityManager em = EntityManagerFactorySingleton.getInitDb();

	private final Scanner sc = new Scanner(System.in);
	private OrderDAO orderDAO = new OrderDAOImpl(em);
	private ShelterDAO shelterDAO = new ShelterDAOImpl(em);
	private DonationDAO donationDAO = new DonationDAOImpl(em);

	@Override
	public void createOrder() {

		try {
			Order order = createOrderFromInput();
			int currentQuantity = getTotalQuantityByType(order.getShelter(), order.getDonation().getLot().getItemType());

	        if (currentQuantity + order.getDonation().getLot().getQuantity() > 200) {
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
			System.out.print("\nDigite o ID do pedido a ser aceito: ");
			Long id = sc.nextLong();
			sc.nextLine();

			Order order = orderDAO.find(id);

			if (order == null || !order.getStatus().equals(OrderStatus.WAITING)) {
				throw new IllegalArgumentException("Ordem inválida ou já processada");
			}

			Donation donation = order.getDonation();
			Shelter shelter = order.getShelter();

			donation.setDistributionCenter(null);
			donation.setShelter(shelter);

			order.setStatus(OrderStatus.ACCEPTED);
			orderDAO.update(order);
			donationDAO.update(donation);
			System.out.println("\n=== Pedido aceito com sucesso! ===");
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao aceitar o pedido: " + e.getMessage());

		}
	}

	@Override
	public void rejectOrder() {
		try {
			System.out.print("\nDigite o ID do pedido a ser rejeitado: ");
			Long id = sc.nextLong();
			sc.nextLine();

			Order order = orderDAO.find(id);

			if (order == null || !order.getStatus().equals(OrderStatus.WAITING)) {
				throw new IllegalArgumentException("Ordem inválida ou já processada");
			}

			System.out.print("\nMotivo da recusa do pedido: ");
			String motivo = sc.nextLine();

			order.setStatus(OrderStatus.DENIED);
			order.setRefusalMotive(motivo);
			orderDAO.update(order);

		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao rejeitar o pedido: " + e.getMessage());
		}

	}

	@Override
	public void findOrderByShelter() {
		try {
			System.out.print("\nDigite o ID do abrigo para procurar os pedidos: ");
			Long shelterId = sc.nextLong();
			sc.nextLine();

			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Order> cq = cb.createQuery(Order.class);
			Root<Order> root = cq.from(Order.class);
			Join<Order, Shelter> shelterJoin = root.join("shelter");
			cq.select(root).where(cb.equal(shelterJoin.get("id"), shelterId));
			TypedQuery<Order> query = em.createQuery(cq);

			List<Order> orders = query.getResultList();
			if (orders.isEmpty()) {
				System.out.println("Nenhum pedido encontrado para o abrigo com ID " + shelterId + ".");
			} else {
				orders.forEach(System.out::println);
			}
		} catch (EntityNotFoundException e) {
			System.err.println("Abrigo não encontrado: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao buscar pedidos: " + e.getMessage());
		}
	}

	public Order createOrderFromInput() {
		System.out.println("\nRegistro de pedidos:");
		System.out.println("1. Roupas");
		System.out.println("2. Produtos de Higiene");
		System.out.println("3. Alimentos");
		System.out.print("Tipo de lote requisitado: ");

		int num = sc.nextInt();
		sc.nextLine();

		ItemType itemType = null;
		switch (num) {
		case 1:
			itemType = ItemType.CLOTHING;
			break;
		case 2:
			itemType = ItemType.HYGIENE;
			break;
		case 3:
			itemType = ItemType.FOOD;
			break;
		default:
			System.out.println("Opção inválida.");
			return null;
		}

		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Donation> cq = cb.createQuery(Donation.class);
		Root<Donation> root = cq.from(Donation.class);
		Join<Donation, Lot> lotJoin = root.join("lot");
		cq.select(root).where(cb.equal(lotJoin.get("itemType"), itemType));

		TypedQuery<Donation> query = em.createQuery(cq);
		List<Donation> donations = query.getResultList();
		donations.forEach(System.out::println);

		System.out.print("Id da doação: ");
		Long idDonation = sc.nextLong();
		sc.nextLine();

		System.out.print("Id do abrigo de destino: ");
		Long idShelter = sc.nextLong();
		sc.nextLine();

		OrderStatus status = OrderStatus.WAITING;
		LocalDate dateNow = LocalDate.now();

		Order order = new Order();
		order.setDonation(donationDAO.find(idDonation));
		order.setShelter(shelterDAO.find(idShelter));
		order.setRefusalMotive(null);
		order.setStatus(status);
		order.setDate(dateNow);

		return order;
	}
	
	public int getTotalQuantityByType(Shelter shelter, ItemType itemType) {
	    int totalQuantity = 0;
	    for (Donation donation : shelter.getDonations()) { 
	        if (donation.getLot() != null && donation.getLot().getItemType() == itemType) {
	            totalQuantity += donation.getLot().getQuantity();
	        }
	    }
	    return totalQuantity;
	}

}
