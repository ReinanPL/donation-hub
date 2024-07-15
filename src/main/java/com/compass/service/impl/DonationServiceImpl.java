package com.compass.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
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

	private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private DonationDAO donationDAO = new DonationDAOImpl(em);

	private LotDAO lotDAO = new LotDAOImpl(em);

	private DistributionCenterDAO centerDAO = new DistributionCenterDAOImpl(em);

	@Override
	public void registerDonation() {
		try {
			Lot lot = createLot();
			DistributionCenter center = centerDAO.find(selectCenter());
			
			int currentQuantity = getTotalQuantityByType(center, lot.getItemType());

	        if (currentQuantity + lot.getQuantity() > 1000) {
	            throw new LimitReachedException(
	                "Limite de itens do tipo " + lot.getItemType() + " atingido no centro de distribuição " + center.getName() + "."
	            );
	        }
			
			lotDAO.create(lot);
			
			Donation donation = new Donation(null, center, lot);

			donationDAO.create(donation);
			System.out.println("\n=== Doação registrada com sucesso! ===");
			System.out.println(donation);
		} catch (EntityNotFoundException e) {
			System.err.println("Erro: Centro de distribuição não encontrado.");
		} 

	}

	@Override
	public void updateDonation() { 
		try {
			getAllDonations();
			System.out.print("\nDigite o ID da doação a ser atualizada: ");
			Long donationId = sc.nextLong();
			sc.nextLine();

			Donation donation = donationDAO.find(donationId);
			if (donation == null) {
				throw new EntityNotFoundException("Doação não encontrada.");
			}

			Lot existingLot = donation.getLot();
			if (existingLot == null) {
				throw new IllegalStateException("A doação não possui um lote associado.");
			}

			Lot updatedDonation = createLot();

			existingLot.setName(updatedDonation.getName());
			existingLot.setItemType(updatedDonation.getItemType());
			existingLot.setDescription(updatedDonation.getDescription());
			existingLot.setQuantity(updatedDonation.getQuantity());
			existingLot.setUnitOfMeasurement(updatedDonation.getUnitOfMeasurement());
			existingLot.setValidity(updatedDonation.getValidity());
			existingLot.setGenre(updatedDonation.getGenre());
			existingLot.setSize(updatedDonation.getSize());

			lotDAO.update(existingLot);

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
			System.out.print("\nDigite o ID da doação a ser deletada: ");
			Long id = sc.nextLong();
			sc.nextLine();

			Donation donation = donationDAO.find(id);
			if (donation == null) {
				throw new EntityNotFoundException("Doação não encontrada.");
			}

			donationDAO.remove(id);
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
			System.out.print("\nDigite o ID da doação a ser buscada: ");
			Long id = sc.nextLong();
			sc.nextLine();

			Donation donation = donationDAO.find(id);
			if (donation != null) {
				System.out.println("\n=== Detalhes da Doação ===");
				System.out.println(donation);
			} else {
				throw new EntityNotFoundException("Doação não encontrada.");
			}
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (InputMismatchException e) {
			System.err.println("ID inválido. Digite um número válido.");
		} catch (Exception e) {
			System.err.println("Erro ao buscar a doação: " + e.getMessage());
		}
	}

	@Override
	public void getAllDonations() {
		try {
			List<Donation> donations = donationDAO.findAll();

			if (donations.isEmpty()) {
				System.out.println("\n=== Lista de Doações ===");
				System.out.println("Nenhuma doação cadastrada.");
			} else {
				System.out.println("\n=== Lista de Doações ===");
				donations.forEach(System.out::println);
			}
		} catch (Exception e) {
			System.err.println("Erro ao obter a lista de doações: " + e.getMessage());
		}
	}

	public void transferLot() {
		try {
			System.out.println("\nDigite o ID do centro de origem da transferência: ");
			Long id = sc.nextLong();
			sc.nextLine();

	        DistributionCenter sourceCenter = centerDAO.find(id);
	        if (sourceCenter == null) {
	            throw new EntityNotFoundException("Centro de distribuição de origem não encontrado.");
	        }
	        
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Donation> cq = cb.createQuery(Donation.class);
			Root<Donation> root = cq.from(Donation.class);
			cq.select(root).where(cb.equal(root.get("distributionCenter").get("id"), id));
			List<Donation> donations = em.createQuery(cq).getResultList();

			if (donations.isEmpty()) {
	            System.out.println("Nenhuma doação encontrada no centro de distribuição com ID " + id + ".");
	        }

	        System.out.println("\nDoações disponíveis no centro de origem:");
	        donations.forEach(System.out::println);

	        System.out.println("Digite o id da doação para fazer a transferência");
	        Long idLot = sc.nextLong();
	        sc.nextLine(); 

	        Donation donation = donationDAO.find(idLot);
	        if (donation == null) {
	            throw new EntityNotFoundException("Doação não encontrada.");
	        }

	        System.out.println("Digite o id do centro de destino para fazer a transferência");
	        Long idDestino = sc.nextLong();
	        sc.nextLine(); 

	        DistributionCenter destinationCenter = centerDAO.find(idDestino);
	        if (destinationCenter == null) {
	            throw new EntityNotFoundException("Centro de distribuição de destino não encontrado.");
	        }
	        
	        int currentQuantity = getTotalQuantityByType(destinationCenter, donation.getLot().getItemType());
	        if (currentQuantity + donation.getLot().getQuantity() > 1000) {
	            throw new LimitReachedException(
	                "Limite de itens do tipo " + donation.getLot().getItemType() + 
	                " atingido no centro de distribuição " + destinationCenter.getName() + "."
	            );
	        }
	        
	        donation.setDistributionCenter(destinationCenter);

			donationDAO.update(donation);
			System.out.println("\n=== Doação transferida com sucesso! ===");
		} catch (EntityNotFoundException e) {
	        System.err.println(e.getMessage());
	    } catch (InputMismatchException e) {
	        System.err.println("ID inválido. Digite um número válido.");
	    } 

	}

	private static final Map<Integer, GenreCloth> GENRE_CLOTH_MAP = Map.of(1, GenreCloth.M, 2, GenreCloth.F);

	private static final Map<Integer, SizeCloth> SIZE_CLOTH_MAP = Map.of(1, SizeCloth.CHILDREN, 2, SizeCloth.XS, 3,
			SizeCloth.S, 4, SizeCloth.M, 5, SizeCloth.L, 6, SizeCloth.XL);

	private Lot createLot() { //refactor
		try {
			System.out.println("\n=== Registro de Doação: Lote ===");

			System.out.print("Nome do item: ");
			String name = sc.nextLine();

			System.out.print("Descrição do item: ");
			String description = sc.nextLine();

			System.out.print("Quantidade: ");
			int quantity = sc.nextInt();
			sc.nextLine();

			String unitOfMeasurement = null;
			GenreCloth genre = null;
			SizeCloth size = null;
			LocalDate validity = null;

			System.out.print("Escolha o tipo de item: ");
			System.out.println("1. Roupas");
			System.out.println("2. Produtos de Higiene");
			System.out.println("3. Alimentos");

			int itemTypeChoice = sc.nextInt();
			sc.nextLine();

			ItemType itemType = null;

			switch (itemTypeChoice) {
			case 1:
				int genreChoice = 0;
				while (genreChoice != 1 || genreChoice != 2) {
					itemType = ItemType.CLOTHING;
					System.out.println("\nGênero da Roupa:");
					System.out.println("1. Masculino");
					System.out.println("2. Feminino");
					System.out.print("Escolha uma opção: ");

					genreChoice = sc.nextInt();
					sc.nextLine();

					genre = GENRE_CLOTH_MAP.getOrDefault(genreChoice, null);
					if (genre == null) {
						System.out.println("Opção inválida!");

					}
				}

				System.out.println("\nTamanho da Roupa:");
				System.out.println("1. Infantil");
				System.out.println("2. PP");
				System.out.println("3. P");
				System.out.println("4. M");
				System.out.println("5. G");
				System.out.println("6. GG");
				System.out.print("Escolha uma opção: ");

				int sizeChoice = sc.nextInt();
				sc.nextLine();

				size = SIZE_CLOTH_MAP.getOrDefault(sizeChoice, null);
				if (size == null) {
					System.out.println("Opção inválida!");

				}
				break;

			case 2:
				itemType = ItemType.HYGIENE;
				break;
			case 3:
				itemType = ItemType.FOOD;
				System.out.println("Digite a validade do alimento doado (yyyy-MM-dd): ");
				String dateStr = sc.nextLine();
				validity = LocalDate.parse(dateStr, dtf);

				System.out.println("Digite a unidade de medida do item: ");
				unitOfMeasurement = sc.nextLine();

				break;
			default:
				System.out.println("Opção inválida!");

			}

			Lot lot = new Lot();
			lot.setName(name);
			lot.setItemType(itemType);
			lot.setDescription(description);
			lot.setQuantity(quantity);
			lot.setGenre(genre);
			lot.setSize(size);
			lot.setValidity(validity);
			lot.setUnitOfMeasurement(unitOfMeasurement);

			return lot;
		} catch (Exception e) {
			System.err.println("Erro:" + e.getMessage());
		}
		return null;
	}

	public Long selectCenter() {
		try {
			List<DistributionCenter> centers = centerDAO.findAll();
			if (centers.isEmpty()) {
	            System.out.println("\n=== Selecione o Centro de Distribuição ==="); 
	            System.out.println("Nenhum centro de distribuição encontrado.");
	            return null;
	        }
					
					
			System.out.print("\nDigite o número do centro de distribuição: ");
			centers.forEach(System.out::println);
			Long id = sc.nextLong(); 
			
			if (id >= 1 && id <= centers.size()) {
	            return id; 
	        } else {
	            System.out.println("Opção inválida.");
	            return null;
	        }
		} catch (Exception e) {
			System.err.println("Erro:" + e.getMessage());
			return null;
		}

	}
	
	public int getTotalQuantityByType(DistributionCenter center, ItemType itemType) {
	    int totalQuantity = 0;
	    for (Donation donation : center.getDonations()) { 
	        if (donation.getLot() != null && donation.getLot().getItemType() == itemType) {
	            totalQuantity += donation.getLot().getQuantity();
	        }
	    }
	    return totalQuantity;
	}
	
	

}
