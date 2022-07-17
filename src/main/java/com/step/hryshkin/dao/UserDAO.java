package com.step.hryshkin.dao;

import com.step.hryshkin.model.User;

import java.util.Optional;

public interface UserDAO {

    void createNewUser(User user);

    Optional<User> getUserByName(String userName);
}
