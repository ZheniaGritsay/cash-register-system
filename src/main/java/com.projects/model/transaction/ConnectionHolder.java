package com.projects.model.transaction;

import com.projects.model.transaction.exception.TransactionException;

import java.sql.Connection;

public interface ConnectionHolder {
    Connection getCurrentConnection();

    boolean isTransactionActive();

    void setTransactionActive(boolean active);

    void closeCurrentConnection() throws TransactionException;
}
