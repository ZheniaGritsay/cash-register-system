package com.projects.model.connection.impl;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class ConnectionFactoryTest {
    private ConnectionFactoryImpl connectionFactory = ConnectionFactoryImpl.getInstance();

    @Test
    public void init() {
        assertNotNull(connectionFactory.init());
    }

    @Test
    public void getConnection() throws SQLException {
        connectionFactory.setConnectionPool(connectionFactory.init());

        Connection connection = connectionFactory.getConnection();

        assertNotNull(connection);
        assertFalse(connection.isClosed());
    }
}
