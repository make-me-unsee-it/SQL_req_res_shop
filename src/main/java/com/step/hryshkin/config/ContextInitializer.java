package com.step.hryshkin.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ContextInitializer implements ServletContextListener {
    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);
    private final String initUrl = PropertiesLoader.loadProperty("db.init");
    private final String user = PropertiesLoader.loadProperty("db.user");
    private final String password = PropertiesLoader.loadProperty("db.password");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try (Connection connection = DriverManager.getConnection(initUrl, user, password)) {
            LOGGER.info("Connection stable");
        } catch (SQLException e) {
            LOGGER.error("SQLEException on ContextInitializer " + e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
