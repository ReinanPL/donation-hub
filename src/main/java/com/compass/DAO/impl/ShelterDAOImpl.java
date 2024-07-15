package com.compass.DAO.impl;

import jakarta.persistence.EntityManager;
import com.compass.DAO.ShelterDAO;
import com.compass.model.Shelter;

public class ShelterDAOImpl extends GenericDAOImpl<Shelter, Long> implements ShelterDAO {

	public ShelterDAOImpl(EntityManager em) {
		super(em, Shelter.class);
	}

}
