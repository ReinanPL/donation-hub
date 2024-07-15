package com.compass.DAO.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import com.compass.DAO.GenericDAO;
import com.compass.Exception.NoDataException;

public class GenericDAOImpl<T, K> implements GenericDAO<T, K> {

	private EntityManager em;

	private Class<T> entityClass;

	public GenericDAOImpl(EntityManager em, Class<T> entityClass) {
		super();
		this.em = em;
		this.entityClass = entityClass;
	}

	@Override
	public void create(T entity) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin(); 
			em.persist(entity);
			transaction.commit(); 
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback(); // Desfaz a transação em caso de erro
			}
			throw new RuntimeException("Erro ao criar a entidade: " + e.getMessage(), e); 
		}

	}

	@Override
	public void update(T entity) {
		EntityTransaction transaction = em.getTransaction();
		try {
			transaction.begin();
			em.merge(entity);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new RuntimeException("Erro ao atualizar a entidade: " + e.getMessage(), e);
		}
	}

	@Override
	public T find(K id) {
		return em.find(entityClass, id);

	}

	@Override
	public List<T> findAll() {
		return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass).getResultList();
	}

	@Override
	public void remove(K id) {
		EntityTransaction transaction = em.getTransaction();

		try {
			transaction.begin();
			T entity = find(id);
			if (entity == null) {
				throw new NoDataException("Entidade não encontrada com o ID: " + id);
			}
			em.remove(entity);
			transaction.commit();
		} catch (NoDataException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			if (transaction != null && transaction.isActive()) {
				transaction.rollback();
			}
			throw new RuntimeException("Erro ao remover a entidade: " + e.getMessage(), e);
		}

	}

}
