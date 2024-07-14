package com.compass.DAO.impl;

import jakarta.persistence.EntityManager;
import com.compass.DAO.OrderDAO;
import com.compass.model.Order;

public class OrderDAOImpl extends GenericDAOImpl<Order, Long> implements OrderDAO {

	public OrderDAOImpl(EntityManager em) {
		super(em);
	}

}
