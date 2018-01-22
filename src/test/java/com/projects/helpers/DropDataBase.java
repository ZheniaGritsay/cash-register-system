package com.projects.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DropDataBase {
    private static final Logger logger = LoggerFactory.getLogger(DropDataBase.class);
    private static final String DROP_TABLES_SQL = "DROP TABLE reports_checks;" +
            "  DROP TABLE checks_products;" +
            "  DROP TABLE reports;" +
            "  DROP TABLE checks;" +
            "  DROP TABLE products;" +
            "  DROP TABLE cash_register_users;" +
            "  DROP TABLE employees;" +
            "  DROP TABLE roles;" +
            "  DROP TABLE positions;" +
            "  DROP TABLE check_status;" +
            "  DROP TABLE report_types;";

    public static void dropDataBase() {
        Connection connection = null;
        Properties properties = new Properties();
        try (InputStream is = DropDataBase.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            properties.load(is);

            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(properties.getProperty("url"),
                    properties.getProperty("root.user"), properties.getProperty("root.password"));

            Statement statement = connection.createStatement();
            statement.execute(DROP_TABLES_SQL);

        } catch (ClassNotFoundException | SQLException | IOException e) {
            logger.error("failed to drop database after tests: " + e.getMessage());
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.error("failed to close connection after dropping database: " + e.getMessage());
                }
            }
        }
    }
}
