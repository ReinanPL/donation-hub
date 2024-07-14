package com.compass.DAO.impl;

import jakarta.persistence.EntityManager;
import com.compass.DAO.DistributionCenterDAO;
import com.compass.model.DistributionCenter;

public class DistributionCenterDAOImpl extends GenericDAOImpl<DistributionCenter, Long> implements DistributionCenterDAO {

	public DistributionCenterDAOImpl(EntityManager em) {
		super(em);
	}

}
