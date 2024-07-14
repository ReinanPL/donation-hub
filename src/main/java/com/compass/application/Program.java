package com.compass.application;


import com.compass.DAO.DistributionCenterDAO;
import com.compass.DAO.impl.DistributionCenterDAOImpl;
import com.compass.Exception.CommitException;
import com.compass.model.DistributionCenter;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Program {

	public static void main(String[] args) {
		EntityManagerFactory fabrica = Persistence.createEntityManagerFactory("conn");
		EntityManager em = fabrica.createEntityManager();
		
		DistributionCenterDAO centerDao = new DistributionCenterDAOImpl(em);
		
		DistributionCenter center = new DistributionCenter(null, "Abrigo", "nome", null, null);
		
		centerDao.create(center);
		try{
			centerDao.commit();
		} catch(CommitException e) {
			e.printStackTrace();
		}
		em.close();
		fabrica.close();
	}

}
