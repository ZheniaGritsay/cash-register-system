package com.projects.model.transaction.impl;

import com.projects.model.transaction.TransactionObject;

public class ThreadContext {
    private TransactionObject transaction;
    private int transactionIsolation;

    public ThreadContext() {
    }

    public TransactionObject getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionObject transaction) {
        this.transaction = transaction;
    }

    public int getTransactionIsolation() {
        return transactionIsolation;
    }

    public void setTransactionIsolation(int transactionIsolation) {
        this.transactionIsolation = transactionIsolation;
    }
}
