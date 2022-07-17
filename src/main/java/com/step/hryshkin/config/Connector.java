package com.step.hryshkin.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connector {

    private static final String URL = PropertiesLoader.loadProperty("db.url");
    private static final String USER = PropertiesLoader.loadProperty("db.user");
    private static final String PASSWORD = PropertiesLoader.loadProperty("db.password");

    private Connector() {
    }

    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
