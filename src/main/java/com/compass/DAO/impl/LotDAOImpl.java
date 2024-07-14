package com.compass.DAO.impl;

import jakarta.persistence.EntityManager;
import com.compass.DAO.LotDAO;
import com.compass.model.Lot;

public class LotDAOImpl extends GenericDAOImpl<Lot, Long> implements LotDAO {

	public LotDAOImpl(EntityManager em) {
		super(em);
	}

}
