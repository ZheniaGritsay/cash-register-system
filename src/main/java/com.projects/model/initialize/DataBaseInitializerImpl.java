package com.projects.model.initialize;

import com.projects.model.dao.exception.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class DataBaseInitializerImpl implements DataBaseInitializer {
    private static Logger logger = LoggerFactory.getLogger(DataBaseInitializerImpl.class);
    private String schemaSqlPath = "schema.sql";
    private String initSqlPath = "init.sql";

    public DataBaseInitializerImpl() {
    }

    @Override
    public void initializeDb() throws InitializationException {
        Connection connection = null;
        try {
            Properties properties = loadProperties();
            boolean createSchema = Boolean.valueOf(properties.getProperty("schema"));
            boolean init = Boolean.valueOf(properties.getProperty("init"));
            if (!createSchema && !init)
                return;

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("root.user"),
                    properties.getProperty("root.password"));

            Statement statement = connection.createStatement();
            if (createSchema) {
                String schemaSqlQuery = loadSqlScript(schemaSqlPath);
                statement.execute(schemaSqlQuery);
            }

            if (init) {
                String initSqlQuery = loadSqlScript(initSqlPath);
                statement.execute(initSqlQuery);
            }

        } catch (SQLException e) {
            logger.error("failed to initialize database", e);
            throw new InitializationException("unable to initialize database: " + e.getMessage());
        } catch (IOException e) {
            logger.error("failed to load sql script file", e);
            throw new InitializationException("unable to load sql script: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            logger.error("failed to load driver", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("failed to close connection", e);
                }
            }
        }
    }

    private String loadSqlScript(String scriptPath) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(scriptPath)) {
            int b = is.read();
            while (b != -1) {
                sb.append((char) b);
                b = is.read();
            }
        }

        return sb.toString();
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        try(InputStream is = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            properties.load(is);
        } catch (IOException e) {
            logger.error("failed to load properties for database initialization: " + e.getMessage());
        }

        return properties;
    }
}
