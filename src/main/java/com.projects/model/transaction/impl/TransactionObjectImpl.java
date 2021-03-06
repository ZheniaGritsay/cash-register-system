package com.projects.model.transaction.impl;

import com.projects.model.transaction.ConnectionHolder;
import com.projects.model.transaction.Status;
import com.projects.model.transaction.TransactionObject;
import com.projects.model.transaction.exception.TransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionObjectImpl implements TransactionObject {
    private static final Logger logger = LoggerFactory.getLogger(TransactionObjectImpl.class);
    private ConnectionHolder connectionHolder;
    private int transactionIsolation;
    private Status status;

    public TransactionObjectImpl(ConnectionHolder connectionHolder) {
        this.connectionHolder = connectionHolder;
        this.transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
        this.status = Status.NO_TRANSACTION;
    }

    public TransactionObjectImpl(ConnectionHolderImpl connectionHolder, int transactionIsolation) {
        this.connectionHolder = connectionHolder;
        if (transactionIsolation <= 0)
            this.transactionIsolation = Connection.TRANSACTION_READ_COMMITTED;
        else
            this.transactionIsolation = transactionIsolation;
        this.status = Status.NO_TRANSACTION;
    }

    @Override
    public void begin() throws TransactionException {
        if (status != Status.NO_TRANSACTION)
            throw new IllegalStateException();

        status = Status.ACTIVE;

        try {
            connectionHolder.getCurrentConnection().setAutoCommit(false);
            connectionHolder.getCurrentConnection().setTransactionIsolation(transactionIsolation);
        } catch (SQLException e) {
            logger.error("failed to start transaction: " + e.getMessage());
            throw new TransactionException(e);
        }

        connectionHolder.setTransactionActive(true);
    }

    @Override
    public void commit() throws TransactionException {
        if (status == Status.COMMITTING || status == Status.COMMITTED ||
                status == Status.ROLLING_BACK || status == Status.ROLLED_BACK)
            throw new IllegalStateException();

        status = Status.COMMITTING;

        try {
            connectionHolder.getCurrentConnection().commit();
            status = Status.COMMITTED;
            connectionHolder.setTransactionActive(false);
            connectionHolder.getCurrentConnection().setAutoCommit(true);
            connectionHolder.closeCurrentConnection();
        } catch (SQLException e) {
            logger.error("failed to commit transaction: " + e.getMessage());
            throw new TransactionException("unable to commit", e);
        }
    }

    @Override
    public void rollback() throws TransactionException {
        if (status == Status.COMMITTING || status == Status.COMMITTED ||
                status == Status.ROLLING_BACK || status == Status.ROLLED_BACK)
            throw new TransactionException("transaction has already finished");

        status = Status.ROLLING_BACK;

        try {
            connectionHolder.getCurrentConnection().rollback();
            status = Status.ROLLED_BACK;
            connectionHolder.setTransactionActive(false);
            connectionHolder.getCurrentConnection().setAutoCommit(true);
            connectionHolder.closeCurrentConnection();
        } catch (SQLException e) {
            logger.error("failed to rollback transaction: " + e.getMessage());
            throw new TransactionException("unable to rollback", e);
        }
    }

    @Override
    public void setIsolationLevel(int isolationLevel) {
        if (isolationLevel <= 0)
            setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        else
            setTransactionIsolation(isolationLevel);
    }

    public ConnectionHolder getConnectionHolder() {
        return connectionHolder;
    }

    public void setConnectionHolder(ConnectionHolderImpl connectionHolder) {
        this.connectionHolder = connectionHolder;
    }

    public int getTransactionIsolation() {
        return transactionIsolation;
    }

    public void setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
