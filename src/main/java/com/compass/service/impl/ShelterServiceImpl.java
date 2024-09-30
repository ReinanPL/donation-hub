package com.compass.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;


import com.compass.util.ValidationUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import com.compass.DAO.ShelterDAO;
import com.compass.DAO.impl.ShelterDAOImpl;
import com.compass.model.Shelter;
import com.compass.service.ShelterService;
import com.compass.util.EntityManagerFactorySingleton;

import static java.lang.Long.parseLong;

public class ShelterServiceImpl implements ShelterService {

	EntityManager em = EntityManagerFactorySingleton.getInitDb();
	private final Scanner sc = new Scanner(System.in);
	private final ShelterDAO shelterDAO = new ShelterDAOImpl(em);
	private final ValidationUtil validationService = new ValidationUtil();

	@Override
	public void registerShelter() {
		try {
			var shelter = createShelter();
			shelterDAO.create(shelter);
			System.out.println("\n=== Abrigo cadastrado com sucesso! ===");
			System.out.println(shelter);
		} catch (Exception e) {
			System.err.println("Erro inesperado ao cadastrar o abrigo: " + e.getMessage());
		}

	}

	@Override
	public void updateShelter() {
		try {
			var shelter = createShelterFromInput();
			shelterDAO.update(shelter);
			System.out.println("\n=== Abrigo atualizado com sucesso! ===");
			System.out.println(shelter);
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro inesperado ao atualizar o abrigo: " + e.getMessage());
		}

	}

	@Override
	public void deleteShelter() {
		try {
			var id = parseLong(getInput("\nDigite o ID do abrigo a ser deletado: "));
			Optional.ofNullable(shelterDAO.find(id))
					.orElseThrow(() -> new EntityNotFoundException("Abrigo não encontrado."));

			shelterDAO.remove(id);
			System.out.println("\n=== Abrigo deletado com sucesso! ===");
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro inesperado ao deletar o abrigo: " + e.getMessage());
		}

	}

	@Override
	public void getAllShelters() { // pode ser uma impressao vazia, nao deve lançar exceção
		try {
			var shelters = Optional.of(shelterDAO.findAll())
					.filter(list -> !list.isEmpty())
					.orElseThrow(() -> new EntityNotFoundException("Nenhum Abrigo Encontrado!"));
			System.out.println("\n=== Lista de Abrigos ===");
			shelters.forEach(System.out::println);
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao obter a lista de abrigos: " + e.getMessage());
		}
	}

	@Override
	public void getShelterById() {
		try {
			var id = Long.parseLong(getInput("\nDigite o ID do abrigo: "));
			var shelter = Optional.of(shelterDAO.find(id))
					.orElseThrow(() -> new EntityNotFoundException("Abrigo não encontrado."));

			System.out.println("\n=== Detalhes do Abrigo ===");
			System.out.println(shelter);
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro ao buscar o abrigo: " + e.getMessage());
		}
	}

	public Shelter createShelter() {
		System.out.println("\n=== Registro de Abrigo ===");
		var name = getInput("Nome do abrigo: ");
		var address = getInput("Endereço: ");
		var responsible = getInput("Responsavel: ");
		var mail = getInput("Email: ");
		var phone = getInput("Telefone: ");

		var shelter = new Shelter(null, name, address, responsible, phone, mail, null);
		validationService.validate(shelter);
		return shelter;
	}

	public Shelter createShelterFromInput() {
		var id = Long.parseLong(getInput("\nDigite o ID do abrigo a ser atualizado: "));
		Optional.ofNullable(shelterDAO.find(id))
				.orElseThrow(() -> new EntityNotFoundException("Abrigo não encontrado."));

		System.out.println("\n=== Atualizar Abrigo ===");
		var name = getInput("Novo nome do abrigo: ");
		var address = getInput("Novo endereço: ");
		var responsible = getInput("Novo responsavel: ");
		var mail = getInput("Novo email: ");
		var phone = getInput("Novo telefone: ");

		var shelter = new Shelter(id, name, address, responsible, phone, mail, null);
		validationService.validate(shelter);
		return shelter;
	}

	private String getInput(String prompt) {
		System.out.print(prompt);
		return sc.nextLine();
	}
}
