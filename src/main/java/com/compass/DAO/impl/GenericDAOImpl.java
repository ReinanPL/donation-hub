package com.compass.DAO.impl;

import java.lang.reflect.ParameterizedType;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import com.compass.DAO.GenericDAO;
import com.compass.Exception.CommitException;
import com.compass.Exception.NoDataException;

public class GenericDAOImpl<T,K> implements GenericDAO<T, K> {

	private EntityManager em;
	
	private Class<T> clazz;
	
	@SuppressWarnings("all")
	public GenericDAOImpl(EntityManager em) {
		super();
		this.em = em;
		clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
	
	
	@Override
	public void create(T entity) {
		em.persist(entity);
		
	}

	@Override
	public void update(T entity) {
		em.merge(entity);
		
	}

	@Override
	public T find(K id) {
		return em.find(clazz, id);
		
	}
	
	@Override
	public List<T> findAll() {
	    CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
	    criteriaQuery.from(clazz);
	    return em.createQuery(criteriaQuery).getResultList();
	}

	@Override
	public void remove(K id) throws NoDataException {
		T entidade = find(id);
		if (entidade == null)
			throw new NoDataException();
		em.remove(entidade);
		
	}
	
	

	@Override
	public void commit() throws CommitException {
		try {
			em.getTransaction().begin();
			em.getTransaction().commit();
			}catch (Exception e) {
				em.getTransaction().rollback();
				e.printStackTrace();
				throw new CommitException();
			}
		
	}


	
	

}
