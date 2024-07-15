package com.compass.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactorySingleton {
	
	private static EntityManagerFactory fabrica;
	
	private static EntityManager em;
	
	private EntityManagerFactorySingleton() {
		
	}
	
	public static EntityManagerFactory getInstance() {
		if (fabrica == null) 
			fabrica = Persistence.createEntityManagerFactory("conn");
			return fabrica;
	}
	
	public static EntityManager getInitDb() {
	    
		EntityManagerFactory fabrica = EntityManagerFactorySingleton.getInstance();
		return em = fabrica.createEntityManager();
	}
	
	public static void closeDb() {
		em.close();
		fabrica.close();
	}

}
