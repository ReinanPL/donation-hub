package com.compass.service.impl;

import java.time.LocalDate;
import java.util.*;

import com.compass.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import com.compass.DAO.DistributionCenterDAO;
import com.compass.DAO.DonationDAO;
import com.compass.DAO.LotDAO;
import com.compass.DAO.impl.DistributionCenterDAOImpl;
import com.compass.DAO.impl.DonationDAOImpl;
import com.compass.DAO.impl.LotDAOImpl;
import com.compass.Exception.LimitReachedException;
import com.compass.model.DistributionCenter;
import com.compass.model.Donation;
import com.compass.model.Lot;
import com.compass.model.enums.GenreCloth;
import com.compass.model.enums.ItemType;
import com.compass.model.enums.SizeCloth;
import com.compass.service.DonationService;
import com.compass.util.EntityManagerFactorySingleton;


public class DonationServiceImpl implements DonationService {

	EntityManager em = EntityManagerFactorySingleton.getInitDb();
	private final Scanner sc = new Scanner(System.in);
	private final DonationDAO donationDAO = new DonationDAOImpl(em);
	private final LotDAO lotDAO = new LotDAOImpl(em);
	private final DistributionCenterDAO centerDAO = new DistributionCenterDAOImpl(em);
	private final ValidationUtil validationService = new ValidationUtil();

	@Override
	public void registerDonation() {
		try {
			var lot = createLot();
			var center = selectCenter();

			var currentQuantity = getTotalQuantityByType(center, lot.getItemType());
	        verifyStock(currentQuantity, lot, center);

			lotDAO.create(lot);
			var donation = new Donation(null, center, lot);
			donationDAO.create(donation);
			System.out.println("\n=== Doação registrada com sucesso! ===");
			System.out.println(donation);
		} catch (EntityNotFoundException e) {
			System.err.println("Erro: Centro de distribuição não encontrado.");
		} catch(LimitReachedException e) {
			System.err.println(e.getMessage());
		}catch (Exception e) {
			System.err.println("Erro inesperado ao cadastrar doação:" + e.getMessage());
		}

	}

	@Override
	public void updateDonation() {
		try {
			getAllDonations();
			var donationId = Long.parseLong(getInput("\nDigite o ID da doação a ser atualizada: "));
			var donation = Optional.of(donationDAO.find(donationId)).
					orElseThrow(() -> new EntityNotFoundException("Doação não encontrada."));

			var existingLot = Optional.of(donation.getLot())
					.orElseThrow(() -> new EntityNotFoundException("A doação não possui um lote associado."));

			var updatedLot = createLot();
			updatedLot.setId(existingLot.getId());
			validationService.validate(updatedLot);
			lotDAO.update(updatedLot);
			System.out.println("\n=== Doação alterada com sucesso! ===");
			System.out.println(donation);
		} catch (EntityNotFoundException | IllegalStateException e) {
			System.err.println("Erro ao atualizar a doação: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro:" + e.getMessage());
		}
	}

	@Override
	public void deleteDonation() {
		try {
			getAllDonations();
			var donationId = Long.parseLong(getInput("\nDigite o ID da doação a ser deletada: "));
            Optional.ofNullable(donationDAO.find(donationId))
                    .orElseThrow(() -> new EntityNotFoundException("Doação não encontrada."));

            donationDAO.remove(donationId);
			System.out.println("\n=== Doação deletada com sucesso! ===");
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao deletar a doação: " + e.getMessage());

		}
	}

	@Override
	public void getDonationById() {
		try {
			var donationId = Long.parseLong(getInput("\nDigite o ID da doação a ser buscada: "));
			var donation = Optional.of(donationDAO.find(donationId))
					.orElseThrow(() -> new EntityNotFoundException("Id da doação não encontrado."));
			System.out.println(donation);
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao buscar a doação: " + e.getMessage());
		}
	}

	@Override
	public void getAllDonations() {
		try {
			var donations = Optional.of(donationDAO.findAll())
					.filter(list -> !list.isEmpty())
					.orElseThrow(() -> new EntityNotFoundException("Nenhuma doação encontrada."));
			System.out.println("\n=== Lista de Doações ===");
			donations.forEach(System.out::println);
		} catch (Exception e) {
			System.err.println("Erro ao obter a lista de doações: " + e.getMessage());
		}
	}

	@Override
	public void transferLot() {
		try {
			System.out.println("Centro de Origem:");
			var id = selectCenter().getId();
			var donations = Optional.of(donationDAO.getDonationByIdDistributionCenter(id))
					.filter(list -> !list.isEmpty())
					.orElseThrow(() -> new EntityNotFoundException("Nenhuma doação encontrada no centro de distribuição com ID " + id + "."));

	        System.out.println("\nDoações disponíveis no centro de origem:");
	        donations.forEach(System.out::println);

			var idLot = Long.parseLong(getInput("Digite o id da doação para fazer a transferência"));
			var donation = Optional.of(donationDAO.find(idLot))
					.orElseThrow(() -> new EntityNotFoundException("Doação não encontrada."));

			System.out.println("Centro de destino:");
			var destinationCenter = selectCenter();

	        var currentQuantity = getTotalQuantityByType(destinationCenter, donation.getLot().getItemType());
	        verifyStock(currentQuantity, donation.getLot(), destinationCenter);

	        donation.setDistributionCenter(destinationCenter);
			donationDAO.update(donation);
			System.out.println("\n=== Doação transferida com sucesso! ===");
		} catch (EntityNotFoundException e) {
	        System.err.println(e.getMessage());
	    } catch (InputMismatchException e) {
	        System.err.println("ID inválido. Digite um número válido.");
	    }

	}

	private Lot createLot() {
		System.out.println("\n=== Registro de Doação: Lote ===");
		var name = getInput("Nome do item: ");
		var description = getInput("Descrição do item: ");
		var quantity = Integer.parseInt(getInput("Quantidade: "));

		var itemTypeMap = Map.of(1, ItemType.CLOTHING, 2, ItemType.HYGIENE, 3, ItemType.FOOD);

		var itemType = Optional.of(itemTypeMap.get(Integer.parseInt((getInput("Escolha o tipo de item: \n 1. Roupas \n 2. Produtos de Higiene \n 3. Alimentos \n")))))
				.orElseThrow(() -> new IllegalArgumentException("Opção inválida."));

		var lot = new Lot(null, name, itemType, description, quantity, null, null, null, null);

		Optional.of(itemType).filter(ItemType.CLOTHING::equals).ifPresent(type -> LotClothing(lot));
		Optional.of(itemType).filter(ItemType.FOOD::equals).ifPresent(type -> LotFood(lot));

		validationService.validate(lot);
		return lot;
	}

	private void LotClothing(Lot lot){
		var GENRE_CLOTH_MAP = Map.of(1, GenreCloth.M, 2, GenreCloth.F);
		var SIZE_CLOTH_MAP = Map.of(1, SizeCloth.CHILDREN, 2, SizeCloth.XS, 3,
				SizeCloth.S, 4, SizeCloth.M, 5, SizeCloth.L, 6, SizeCloth.XL);

		var genre = GENRE_CLOTH_MAP.get(Integer.parseInt(getInput("Gênero da Roupa: \n1. Masculino \n2. Feminino\n")));
		var size = SIZE_CLOTH_MAP.get(Integer.parseInt(getInput("Tamanho da Roupa: \n1. Infantil \n2. PP \n3. P \n4. M \n5. G \n6. GG\n")));
		lot.setGenre(genre);
		lot.setSize(size);
	}

	private void LotFood(Lot lot){
		var validity = LocalDate.parse(getInput("Digite a validade do alimento doado (yyyy-MM-dd): "));
		var unitOfMeasurement =  getInput("Digite a unidade de medida do item: ");
		lot.setValidity(validity);
		lot.setUnitOfMeasurement(unitOfMeasurement);
	}

	public DistributionCenter selectCenter() {
		var centers = Optional.of(centerDAO.findAll())
				.filter(list -> !list.isEmpty())
				.orElseThrow(() -> new EntityNotFoundException("Não existe nenhum centro de distribuição cadastrado."));

		centers.forEach(System.out::println);
		var id = Long.parseLong(getInput("\nDigite o ID do centro de distribuição: "));
		return Optional.of(centerDAO.find(id))
				.orElseThrow(() -> new EntityNotFoundException("Centro de distribuição não encontrado."));
	}
	
	public int getTotalQuantityByType(DistributionCenter center, ItemType itemType) {
		return center.getDonations().stream()
				.filter(d -> d.getLot() != null && d.getLot().getItemType() == itemType)
				.mapToInt(d -> d.getLot().getQuantity())
				.sum();
	}

	private static void verifyStock(int currentQuantity, Lot lot, DistributionCenter destinationCenter) {
		var totalQuantity = currentQuantity + lot.getQuantity();

		Optional.of(totalQuantity)
				.filter(quantity -> quantity <= 1000)
				.orElseThrow(() -> new LimitReachedException(
						"Limite de itens do tipo " + lot.getItemType() +
								" atingido no centro de distribuição " + destinationCenter.getName() + "."
				));
	}

	private String getInput(String prompt) {
		System.out.print(prompt);
		return sc.nextLine();
	}

}
