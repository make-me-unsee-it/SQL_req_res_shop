package com.step.hryshkin.dao;

import com.step.hryshkin.model.Good;

import java.util.List;
import java.util.Optional;

public interface GoodDAO {

    Optional<Good> getByTitle(String title);

    Optional<Good> getById(long id);

    List<Good> getAll();

}


