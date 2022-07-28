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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OrderDAOImpl implements OrderDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrderDAOImpl.class);

    @Override
    public void createNewOrder(Order order) {
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO ORDERS (USERID, TOTALPRICE) values (?,?)")) {
                statement.setLong(1, order.getUserId());
                statement.setBigDecimal(2, order.getTotalPrice());
                statement.executeUpdate();
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at OrderDAOImpl at CreateNewOrder" + throwable);
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
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at OrderDAOImpl at getLastOrder()" + throwable);
        }
        return order;
    }

    @Override
    public void updateOrder(Order order) {
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("UPDATE orders SET totalPrice = ? WHERE id = ?;")) {
                statement.setBigDecimal(1, order.getTotalPrice());
                statement.setLong(2, order.getId());
                statement.executeUpdate();
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at OrderDAOImpl at CreateNewOrder" + throwable);
        }
    }

    @Override
    public BigDecimal getTotalPriceByOrderId(long id) {
        BigDecimal totalPrice = new BigDecimal("0");
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT totalPrice AS RESULT FROM orders " +
                    "WHERE id = '" + id + "';")) {
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
            LOGGER.error("SQLException at OrderDAOImpl at getTotalPriceByOrderId " + throwable);
        }
        return totalPrice;
    }

}
