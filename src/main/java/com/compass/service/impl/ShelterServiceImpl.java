package com.compass.service.impl;

import java.util.List;
import java.util.Scanner;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.EntityTransaction;
import com.compass.DAO.ShelterDAO;
import com.compass.DAO.impl.ShelterDAOImpl;
import com.compass.Exception.CommitException;
import com.compass.model.Shelter;
import com.compass.service.ShelterService;
import com.compass.util.EntityManagerFactorySingleton;

public class ShelterServiceImpl implements ShelterService {

	EntityManager em = EntityManagerFactorySingleton.getInitDb();

	EntityTransaction transaction = em.getTransaction();

	private final Scanner sc = new Scanner(System.in);

	private ShelterDAO shelterDAO = new ShelterDAOImpl(em);

	@Override
	public void registerShelter() {
		try {
			Shelter shelter = createShelter();
			shelterDAO.create(shelter);
			shelterDAO.commit();
			System.out.println("\n=== Abrigo cadastrado com sucesso! ===");
			System.out.println(shelter);
		} catch (CommitException e) {
			System.err.println("Erro: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro inesperado ao cadastrar o abrigo: " + e.getMessage());
		}

	}

	@Override
	public void updateShelter() {
		try {
			Shelter shelter = createShelterFromInput();
			shelterDAO.update(shelter);
			shelterDAO.commit();
			System.out.println("\n=== Abrigo atualizado com sucesso! ===");
			System.out.println(shelter);
		} catch (CommitException e) {
			System.err.println("Erro ao atualizar o abrigo: " + e.getMessage());
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro inesperado ao atualizar o abrigo: " + e.getMessage());
		}

	}

	@Override
	public void deleteShelter() {
		try {
			System.out.print("\nDigite o ID do abrigo a ser deletado: ");
			Long id = sc.nextLong();
			Shelter shelter = shelterDAO.find(id);
			if (shelter == null) {
				throw new EntityNotFoundException("Abrigo não encontrado.");
			}
			shelterDAO.remove(id);
			shelterDAO.commit();
			System.out.println("\n=== Abrigo deletado com sucesso! ===");
		} catch (CommitException e) {
			System.err.println("Erro ao deletar o abrigo: " + e.getMessage());
		} catch (EntityNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			System.err.println("Erro inesperado ao deletar o abrigo: " + e.getMessage());
		}

	}

	@Override
	public void getAllShelters() {
		try {
			List<Shelter> shelters = shelterDAO.findAll();
			System.out.println("\n=== Lista de Abrigos ===");
			if (shelters.isEmpty()) {
				System.out.println("Nenhum abrigo cadastrado.");
			} else {
				shelters.forEach(System.out::println);
			}
		} catch (Exception e) {
			System.err.println("Erro ao obter a lista de abrigos: " + e.getMessage());
		}
	}

	@Override
	public void getShelterById() {
		try {
	        System.out.print("\nDigite o ID do abrigo: ");
	        Long id = sc.nextLong();
	        sc.nextLine(); 

	        Shelter shelter = shelterDAO.find(id);
	        if (shelter != null) {
	            System.out.println("\n=== Detalhes do Abrigo ===");
	            System.out.println(shelter);
	        } else {
	            throw new EntityNotFoundException("Abrigo não encontrado.");
	        }
	    } catch (EntityNotFoundException e) {
	        System.err.println(e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Erro ao buscar o abrigo: " + e.getMessage());
	    }
	}

	public Shelter createShelter() {
		System.out.println("\n=== Registro de Abrigo ===");
		System.out.print("Nome do abrigo: ");
		String name = sc.nextLine();

		System.out.print("Endereço: ");
		String adress = sc.nextLine();

		System.out.print("Responsavel: ");
		String responsible = sc.nextLine();

		System.out.print("Email: ");
		String mail = sc.nextLine();

		System.out.print("Telefone: ");
		String phone = sc.nextLine();

		Shelter shelter = new Shelter();
		shelter.setName(name);
		shelter.setAddress(adress);
		shelter.setResponsible(responsible);
		shelter.setEmail(mail);
		shelter.setPhoneNumber(phone);

		return shelter;

	}

	public Shelter createShelterFromInput() {
		System.out.print("\nDigite o ID do abrigo a ser atualizado: ");
	    Long id = sc.nextLong();
	    sc.nextLine();

		Shelter shelter = shelterDAO.find(id);
		
		if (shelter == null) {
	        System.err.println("Abrigo não encontrado.");
	        return null; 
	    }

		System.out.println("\n=== Atualizar Abrigo ===");

		System.out.print("Novo nome: ");
		String name = sc.nextLine();

		System.out.print("Novo endereço: ");
		String adress = sc.nextLine();

		System.out.print("Novo responsavel: ");
		String responsible = sc.nextLine();

		System.out.print("Novo email: ");
		String mail = sc.nextLine();

		System.out.print("Novo telefone: ");
		String phone = sc.nextLine();

		shelter.setName(name);
		shelter.setAddress(adress);
		shelter.setResponsible(responsible);
		shelter.setEmail(mail);
		shelter.setPhoneNumber(phone);

		return shelter;

	}

}
