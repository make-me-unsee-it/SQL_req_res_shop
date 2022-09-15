package com.step.hryshkin.dao;

import com.step.hryshkin.model.Good;

import java.util.List;
import java.util.Optional;

public interface GoodDAO {

    List<String> getGoodListByOrderId(long id);

    Optional<Good> getById(long id);

    List<Good> getAll();
}


