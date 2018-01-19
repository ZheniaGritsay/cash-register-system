package com.projects.model.transaction;

import com.projects.model.transaction.exception.TransactionException;

public interface TransactionManager {
    void begin() throws TransactionException;

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    void setTransactionIsolation(int transactionIsolation);
}
