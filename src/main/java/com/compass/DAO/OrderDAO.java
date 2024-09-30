package com.compass.DAO;

import com.compass.model.Order;

import java.util.List;

public interface OrderDAO extends GenericDAO<Order, Long> {

    public List<Order> getOrderByShelter(long shelterId);
    public List<Order> getOrderByCenter(long centerId);

}
