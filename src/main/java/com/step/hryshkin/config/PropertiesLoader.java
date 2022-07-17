package com.step.hryshkin.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {
    private static final Logger LOGGER = LogManager.getLogger(ContextInitializer.class);
    private static final String DB_PROPS_PATH = "/dataBase.properties";
    private static final Properties PROPERTIES = getProperties();

    public static String loadProperty(String property) {
        return PROPERTIES.getProperty(property);
    }

    private static Properties getProperties() {
        Properties temp = new Properties();
        try (InputStream inp = PropertiesLoader.class.getResourceAsStream(DB_PROPS_PATH)) {
            if (inp != null) {
                temp.load(inp);
            }
        }catch (IOException e) {
            LOGGER.error("IOException at PropertiesLoader at getProperties() " + e);
        }
        return temp;
    }
}
