package com.step.hryshkin.dao;

import com.step.hryshkin.model.OrderGood;

import java.util.Optional;

public interface OrderGoodDAO {

    void createNewOrderGoodDAO(OrderGood orderGood);

    Optional<OrderGood> getOrderGoodById(long id);
}
