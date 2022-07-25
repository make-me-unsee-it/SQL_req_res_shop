package com.step.hryshkin.dao;

import com.step.hryshkin.model.Good;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface GoodDAO {

    Optional<Good> getByTitle(String title);

    List<String> getGoodBasketByUserName(String userName);

    BigDecimal getTotalPriceByUserName(String userName);

    Optional<Good> getById(long id);

    List<Good> getAll();

}


