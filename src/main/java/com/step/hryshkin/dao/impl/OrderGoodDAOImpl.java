package com.step.hryshkin.dao.impl;

import com.step.hryshkin.config.Connector;
import com.step.hryshkin.config.ContextInitializer;
import com.step.hryshkin.dao.OrderGoodDAO;
import com.step.hryshkin.model.OrderGood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class OrderGoodDAOImpl implements OrderGoodDAO {

    private static final Logger LOGGER = LogManager.getLogger(OrderGoodDAOImpl.class);


    @Override
    public void createNewOrderGoodDAO(OrderGood orderGood) {
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO ORDERGOODS (ORDERID, GOODID) values (?,?)")) {
                statement.setLong(1, orderGood.getOrderId());
                statement.setLong(2, orderGood.getGoodId());
                statement.executeUpdate();
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at OrderGoodDAOImpl at CreateNewOrderGood" + throwable);
        }
    }

    @Override
    public Optional<OrderGood> getOrderGoodById(long id) {
        return Optional.empty();
    }
}
