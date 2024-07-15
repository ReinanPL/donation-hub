package com.compass.view;

import java.util.Scanner;

import com.compass.service.impl.DonationServiceImpl;
import com.compass.service.impl.OrderServiceImpl;
import com.compass.service.impl.ShelterServiceImpl;
import com.compass.util.CsvUtil;

public class ApplicationView {

	Scanner sc = new Scanner(System.in);

	ShelterServiceImpl shelter = new ShelterServiceImpl();

	DonationServiceImpl donation = new DonationServiceImpl();

	OrderServiceImpl order = new OrderServiceImpl();

	CsvUtil csv = new CsvUtil();

	public void initPrincipal() {
		while (true) {
			System.out.println("=================================");
			System.out.println("||        Donation-HUB        ||");
			System.out.println("=================================");
			System.out.println("|| 1 - Doação                 ||");
			System.out.println("|| 2 - Abrigo                 ||");
			System.out.println("|| 3 - Transferência          ||");
			System.out.println("|| 4 - Pedidos                ||");
			System.out.println("|| 0 - Sair                   ||");
			System.out.println("=================================");
			System.out.print("Escolha uma opção: ");

			int choice = sc.nextInt();
			sc.nextLine();

			switch (choice) {
			case 1:
				initDonation();
				break;
			case 2:
				initShelter();
				break;
			case 3:
				initTranfer();
				break;
			case 4:
				initOrder();
				break;
			case 0:
				System.out.println("Encerrando o sistema...");
				return;
			default:
				System.out.println("Opção inválida!");
			}
		}
	}

	private void clearScreen() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	private void initDonation() {
		while (true) {
			System.out.println("=================================");
			System.out.println("||        Menu de Doação        ||");
			System.out.println("=================================");
			System.out.println("|| 1 - Nova Doação              ||");
			System.out.println("|| 2 - Atualizar Doação         ||");
			System.out.println("|| 3 - Deletar Doação           ||");
			System.out.println("|| 4 - Listar Doações           ||");
			System.out.println("|| 5 - Buscar Doação por ID     ||");
			System.out.println("|| 6 - Importar CSV             ||");
			System.out.println("|| 7 - Limpar Tela              ||");
			System.out.println("|| 0 - Voltar ao Menu Principal ||");
			System.out.println("=================================");
			System.out.print("Escolha uma opção: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				donation.registerDonation();
				break;

			case 2:
				donation.updateDonation();
				break;
			case 3:
				donation.deleteDonation();
				break;
			case 4:
				donation.getAllDonations();
				break;
			case 5:
				donation.getDonationById();
				break;
			case 6:
				csv.importCsv();
				break;
			case 7:
				clearScreen();
				break;
			case 0:
                return;
			default:
				System.out.println("Opção inválida!");
			}

		}

	}

	private void initShelter() {

		while (true) {
			System.out.println("=================================");
			System.out.println("||       Menu de Abrigo        ||");
			System.out.println("=================================");
			System.out.println("|| 1 - Novo Abrigo             ||");
			System.out.println("|| 2 - Atualizar Abrigo        ||");
			System.out.println("|| 3 - Deletar Abrigo          ||");
			System.out.println("|| 4 - Listar Abrigos          ||");
			System.out.println("|| 5 - Buscar Abrigo por ID    ||");
			System.out.println("|| 6 - Limpar Tela             ||");
			System.out.println("|| 0 - Voltar ao Menu Principal||");
			System.out.println("=================================");
			System.out.print("Escolha uma opção: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				shelter.registerShelter();
				break;
			case 2:
				shelter.updateShelter();
				break;
			case 3:
				shelter.deleteShelter();
				break;
			case 4:
				shelter.getAllShelters();
				break;
			case 5:
				shelter.getShelterById();
				break;
			case 6:
				clearScreen();
				break;
			case 0:
                return;
			default:
				System.out.println("Opção inválida!");
			}
		}
	}

 	private void initOrder() {
		while (true) {
			System.out.println("=================================");
			System.out.println("||       Menu de Pedidos       ||");
			System.out.println("=================================");
			System.out.println("|| 1 - Novo Pedido             ||");
			System.out.println("|| 2 - Buscar por ID Abrigo    ||");
			System.out.println("|| 3 - Aceitar Pedido          ||");
			System.out.println("|| 4 - Recusar Pedido          ||");
			System.out.println("|| 5 - Limpar Tela             ||");
			System.out.println("|| 0 - Voltar ao Menu Principal||");
			System.out.println("=================================");
			System.out.print("Escolha uma opção: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				order.createOrder();
				break;
			case 2:
				order.findOrderByShelter();
				break;
			case 3:
				order.acceptOrder();
				break;
			case 4:
				order.rejectOrder();
				break;
			case 5:
				clearScreen();
				break;
			case 0:
                return;
			default:
				System.out.println("Opção inválida!");
			}

		}
	} 

	private void initTranfer() {
		donation.transferLot();
	}

}
