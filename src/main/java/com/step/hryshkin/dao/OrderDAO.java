package com.step.hryshkin.dao;

import com.step.hryshkin.model.Order;

import java.util.Optional;

public interface OrderDAO {

    void createNewOrder(Order order);

    Optional<Order> getOrderById(long id);

    Optional<Order> getLastOrder();
}
