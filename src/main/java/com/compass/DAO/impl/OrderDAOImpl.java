package com.compass.DAO.impl;

import com.compass.model.DistributionCenter;
import com.compass.model.Shelter;
import com.compass.util.EntityManagerFactorySingleton;
import jakarta.persistence.EntityManager;
import com.compass.DAO.OrderDAO;
import com.compass.model.Order;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class OrderDAOImpl extends GenericDAOImpl<Order, Long> implements OrderDAO {

	private final EntityManager em = EntityManagerFactorySingleton.getInitDb();

	public OrderDAOImpl(EntityManager em) {
		super(em, Order.class);
	}

	public List<Order> getOrderByShelter(long shelterId){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> root = cq.from(Order.class);
		Join<Order, Shelter> shelterJoin = root.join("shelter");
		cq.select(root).where(cb.equal(shelterJoin.get("id"), shelterId));
		TypedQuery<Order> query = em.createQuery(cq);
		return em.createQuery(cq).getResultList();
	}

	@Override
	public List<Order> getOrderByCenter(long centerId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Order> cq = cb.createQuery(Order.class);
		Root<Order> root = cq.from(Order.class);
		Join<Order, DistributionCenter> shelterJoin = root.join("distributionCenter");
		cq.select(root).where(cb.equal(shelterJoin.get("id"), centerId));
		TypedQuery<Order> query = em.createQuery(cq);
		return em.createQuery(cq).getResultList();
	}


}
