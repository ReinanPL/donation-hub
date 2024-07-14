package com.compass.application;

import com.compass.model.DistributionCenter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Program {

	public static void main(String[] args) {
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("conn");
		EntityManager em = fabrica.createEntityManager();
		
		
		DistributionCenter center = new DistributionCenter(null, "Abrigo", "Endereço", null, null);
		
		em.persist(center);
		em.getTransaction().begin();
		em.getTransaction().commit();
		
		em.close();
		fabrica.close();
	}

}
