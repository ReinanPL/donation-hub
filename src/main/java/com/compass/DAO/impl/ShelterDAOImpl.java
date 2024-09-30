package com.compass.DAO.impl;

import com.compass.model.Order;
import jakarta.persistence.EntityManager;
import com.compass.DAO.ShelterDAO;
import com.compass.model.Shelter;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class ShelterDAOImpl extends GenericDAOImpl<Shelter, Long> implements ShelterDAO {

	public ShelterDAOImpl(EntityManager em) {
		super(em, Shelter.class);
	}

}
