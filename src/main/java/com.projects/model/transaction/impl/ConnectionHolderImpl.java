package com.projects.model.transaction.impl;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.transaction.ConnectionHolder;
import com.projects.model.transaction.exception.TransactionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionHolderImpl implements ConnectionHolder {
    private static final Logger logger = LogManager.getLogger(ConnectionHolderImpl.class);
    private Connection currentConnection;
    private boolean transactionActive;

    @Override
    public Connection getCurrentConnection() {
        if (currentConnection == null)
            currentConnection = ConnectionFactoryImpl.getInstance().getConnection();
        return currentConnection;
    }

    public void setCurrentConnection(Connection currentConnection) {
        this.currentConnection = currentConnection;
    }

    @Override
    public boolean isTransactionActive() {
        return transactionActive;
    }

    public void setTransactionActive(boolean transactionActive) {
        this.transactionActive = transactionActive;
    }

    @Override
    public void closeCurrentConnection() throws TransactionException {
        try {
            currentConnection.close();
        } catch (SQLException e) {
            logger.error("failed to close transaction connection: " + e.getMessage());
            throw new TransactionException("unable to close connection", e);
        }
    }
}
