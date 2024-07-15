package com.compass.DAO;

import java.util.List;

import com.compass.Exception.NoDataException;

public interface GenericDAO<T,K> {

	void create(T entity);
	
	void update(T entity);
	
	T find(K id);
	
	List<T> findAll();
	
	void remove(K id) throws NoDataException;
	
}
