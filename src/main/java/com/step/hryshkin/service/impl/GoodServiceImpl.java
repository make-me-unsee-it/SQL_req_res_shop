package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.service.GoodService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodServiceImpl implements GoodService {
    private final GoodDAO goodDAO = new GoodDAOImpl();

    @Override
    public List<String> printGoods() {
        List<Good> list = goodDAO.getAll();
        List<String> result = new ArrayList<>();
        for (Good good : list) {
            result.add(good.getTitle() +
                    " (" +
                    good.getPrice().toString().replace(".", ",") +
                    "$)");
        }
        System.out.println(result);
        return result;
    }
}
