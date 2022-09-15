package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.OrderDAO;
import com.step.hryshkin.dao.impl.OrderDAOImpl;
import com.step.hryshkin.service.OrderService;

public class OrderServiceImpl implements OrderService {

    private static final OrderDAO goodDAO = new OrderDAOImpl();

    @Override
    public String printTotalPriceForOrder(long id) {
        System.out.println("НАДЕЮСЬ МЫ ЗДЕСЬ?");
        return goodDAO.getTotalPriceByOrderId(id).toString();
    }
}
