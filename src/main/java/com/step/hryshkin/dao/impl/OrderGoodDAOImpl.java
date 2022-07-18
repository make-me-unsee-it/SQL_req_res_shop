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

    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);

    @Override
    public void createNewOrderGoodDAO(OrderGood orderGood) {
        System.out.println("дао какао 1");
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement statement = connection
                    .prepareStatement("INSERT INTO ORDERGOODS (ORDERID, GOODID) values (?,?)")) {
                System.out.println("дао какао 2");
                System.out.println("ордерайди = " + orderGood.getOrderId());
                System.out.println("ордерайди = " + orderGood.getGoodId());

                statement.setString(1, orderGood.getOrderId().toString());
                statement.setString(2, orderGood.getGoodId().toString());
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            LOGGER.error("SQLException at OrderGoodDAOImpl at CreateNewOrderGood" + exception);
        }
    }

    @Override
    public Optional<OrderGood> getOrderGoodById(long id) {
        return Optional.empty();
    }
}
