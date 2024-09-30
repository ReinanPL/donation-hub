package com.compass.DAO.impl;

import com.compass.model.Lot;
import com.compass.model.enums.ItemType;
import com.compass.util.EntityManagerFactorySingleton;
import jakarta.persistence.EntityManager;
import com.compass.DAO.DonationDAO;
import com.compass.model.Donation;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class DonationDAOImpl extends GenericDAOImpl<Donation, Long> implements DonationDAO {

	private final EntityManager em = EntityManagerFactorySingleton.getInitDb();

	public DonationDAOImpl(EntityManager em) {
		super(em, Donation.class);
	}

	public List<Donation> getDonationByIdDistributionCenter(long id){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Donation> cq = cb.createQuery(Donation.class);
		Root<Donation> root = cq.from(Donation.class);
		cq.select(root).where(cb.equal(root.get("distributionCenter").get("id"), id));
        return em.createQuery(cq).getResultList();
	}

	public List<Donation> getDonationByItemType(ItemType itemType){
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Donation> cq = cb.createQuery(Donation.class);
		Root<Donation> root = cq.from(Donation.class);
		Join<Donation, Lot> lotJoin = root.join("lot");
		cq.select(root).where(cb.equal(lotJoin.get("itemType"), itemType));
		return em.createQuery(cq).getResultList();
	}

}


