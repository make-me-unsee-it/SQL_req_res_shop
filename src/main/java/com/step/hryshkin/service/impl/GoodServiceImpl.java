package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.service.GoodService;

public class GoodServiceImpl implements GoodService {
    private static final GoodDAO goodDAO = new GoodDAOImpl();

    @Override
    public String printTotalPriceForCurrentUser(String name) {
        return goodDAO.getTotalPriceByUserName(name).toString();
    }
}
