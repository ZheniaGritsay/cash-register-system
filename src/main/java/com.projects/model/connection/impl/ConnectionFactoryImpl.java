package com.projects.model.connection.impl;

import com.projects.model.connection.ConnectionFactory;
import com.projects.model.transaction.ConnectionHolder;
import com.projects.model.transaction.impl.ConnectionHandler;
import com.projects.model.transaction.impl.TransactionManagerService;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactoryImpl implements ConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionFactoryImpl.class);
    private static final ConnectionFactoryImpl instance = new ConnectionFactoryImpl();
    private DataSource connectionPool;

    private ConnectionFactoryImpl() {
    }

    public DataSource init() {
        Properties properties = new Properties();
        DataSource ds = null;
        try (InputStream is = ConnectionFactoryImpl.class.getClassLoader()
                .getResourceAsStream("database.properties")) {
            properties.load(is);

            ds = new DataSource();
            ds.setDriverClassName(properties.getProperty("driver"));
            ds.setUrl(properties.getProperty("url"));
            ds.setUsername(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("password"));

        } catch (IOException e) {

        }
        return ds;
    }

    @Override
    public Connection getConnection() {
        if (checkTransaction())
            return getTransactionConnection();

        Connection connection = null;
        try {
            connection = (Connection) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] {Connection.class},
                    new ConnectionHandler(connectionPool.getConnection()));
        } catch (SQLException e) {

        }

        return connection;
    }

    private boolean checkTransaction() {
        return TransactionManagerService.getConnectionHolder().isTransactionActive();
    }

    private Connection getTransactionConnection() {
        return TransactionManagerService.getConnectionHolder().getCurrentConnection();
    }

    public void setConnectionPool(DataSource connectionPool) {
        this.connectionPool = connectionPool;
    }

    public static ConnectionFactoryImpl getInstance() {
        return instance;
    }

    public void shutDown() {
        connectionPool.close();
    }
}
