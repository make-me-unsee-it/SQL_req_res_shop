package com.step.hryshkin.dao.impl;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.config.ContextInitializer;
import com.step.hryshkin.dao.OrderDAO;
import com.step.hryshkin.model.Order;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.step.hryshkin.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderDAOImpl implements OrderDAO {

    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);

    @Override
    public void createNewOrder(Order order) {
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO ORDERS (USERID, TOTALPRICE) values (?,?)")) {
                statement.setString(1, order.getUserId().toString());
                statement.setString(2, order.getTotalPrice().toString());
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            LOGGER.error("SQLException at OrderDAOImpl at CreateNewOrder" + exception);
        }
    }

    @Override
    public Optional<Order> getOrderById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Order> getLastOrder() {
        Optional<Order> order = Optional.empty();
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection
                    .prepareStatement("SELECT * FROM ORDERS WHERE ID = (SELECT MAX(ID) FROM ORDERS)")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    order = Optional.of(new Order(rs.getLong("ID"),
                            rs.getLong("USERID"),
                            rs.getBigDecimal("TOTALPRICE")));
                }
            }
        } catch (SQLException exception) {
            LOGGER.error("SQLException at OrderDAOImpl at getLastOrder()");
        }
        return order;
    }
}
