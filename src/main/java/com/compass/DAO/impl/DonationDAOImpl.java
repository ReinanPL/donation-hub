package com.compass.DAO.impl;

import jakarta.persistence.EntityManager;
import com.compass.DAO.DonationDAO;
import com.compass.model.Donation;

public class DonationDAOImpl extends GenericDAOImpl<Donation, Long> implements DonationDAO {

	public DonationDAOImpl(EntityManager em) {
		super(em, Donation.class);
	}

}
