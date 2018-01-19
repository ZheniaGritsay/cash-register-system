package com.projects.model.transaction;

import com.projects.model.transaction.exception.TransactionException;

public interface TransactionObject {
    void begin() throws TransactionException;

    void commit() throws TransactionException;

    void rollback() throws TransactionException;

    void setIsolationLevel(int isolationLevel);
}
