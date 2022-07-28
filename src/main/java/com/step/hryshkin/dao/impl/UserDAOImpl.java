package com.step.hryshkin.dao.impl;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.config.ContextInitializer;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);

    @Override
    public void createNewUser(User user) {
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO USERS (username, password) values (?,?)")) {
                statement.setString(1, user.getLogin());
                statement.setString(2, user.getPassword());
                statement.executeUpdate();
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at UserDAOImpl at CreateNewUser" + throwable);
        }
    }

    @Override
    public Optional<User> getUserByName(String userName) {
        Optional<User> result = Optional.empty();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("SELECT * FROM USERS WHERE USERNAME = '" + userName + "'")) {
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    result = Optional.of(new User(rs.getLong("ID"),
                            rs.getString("USERNAME"),
                            rs.getString("PASSWORD")));
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at UserDAOImpl at getUserByName(String username)" + throwable);
        }
        return result;
    }
}
