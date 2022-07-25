package com.step.hryshkin.dao.impl;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.config.ContextInitializer;
import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoodDAOImpl implements GoodDAO {
    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    public Optional<Good> getByTitle(String title) {
        Optional<Good> good = Optional.empty();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOODS WHERE TITLE = '" + title + "'")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    good = Optional.of(new Good(rs.getLong("ID"),
                            rs.getNString("TITLE"),
                            rs.getBigDecimal("PRICE")));
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at UserDAOImpl at CreateNewUser" + throwable);

        }
        return good;
    }

    @Override
    public Optional<Good> getById(long id) {
        Optional<Good> good = Optional.empty();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOODS WHERE ID =" + id)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    good = Optional.of(new Good(rs.getLong("ID"),
                            rs.getNString("TITLE"),
                            rs.getBigDecimal("PRICE")));
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException in method getID" + throwable);
        }
        return good;
    }

    @Override
    public List<Good> getAll() {
        Good good;
        List<Good> goodList = new ArrayList<>();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM GOODS ")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    good = new Good(rs.getLong("ID"),
                            rs.getNString("TITLE"),
                            rs.getBigDecimal("PRICE"));
                    goodList.add(good);
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException in method getAll" + throwable);
        }
        return goodList;
    }

    @Override
    public List<String> getGoodBasketByUserName(String userName) {
        Optional<User> newUser = userDAO.getUserByName(userName);
        List<String> goodsInBasket = new ArrayList<>();
        if (newUser.isPresent()) {
            String userNameId = String.valueOf(newUser.get().getId());
            try (Connection connection = Connector.createConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT g.title, o.totalPrice FROM orders AS o " +
                        "JOIN orderGoods AS b ON o.id = b.orderId " +
                        "JOIN goods AS g ON b.goodId = g.id " +
                        "WHERE o.userId = '" + userNameId + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        goodsInBasket.add(rs.getNString("TITLE") + " " + rs.getBigDecimal("TOTALPRICE") + "$");
                    }
                }
            } catch (SQLException throwable) {
                LOGGER.error("SQLException at GoodDAOImpl at getGoodBasketByUserName" + throwable);
            }
        }
        return goodsInBasket;
    }

    @Override
    public BigDecimal getTotalPriceByUserName(String userName) {
        Optional<User> newUser = userDAO.getUserByName(userName);
        BigDecimal totalPrice = new BigDecimal("0");
        if (newUser.isPresent()) {
            String userNameId = String.valueOf(newUser.get().getId());
            try (Connection connection = Connector.createConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT SUM (totalPrice) AS RESULT FROM orders " +
                        "WHERE USERID = '" + userNameId + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String result = rs.getNString("RESULT");
                        if (result == null) {
                            result = "0";
                        }
                        totalPrice = new BigDecimal(result);
                    }
                }
            } catch (SQLException throwable) {
                LOGGER.error("SQLException at GoodDAOImpl at getTotalPriceByUserName" + throwable);
            }
        }
        return totalPrice;
    }
}
