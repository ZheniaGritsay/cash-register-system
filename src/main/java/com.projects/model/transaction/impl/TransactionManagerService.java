package com.projects.model.transaction.impl;

import com.projects.model.transaction.ConnectionHolder;

public class TransactionManagerService {
    private static TransactionManagerImpl transactionManager;
    private static ThreadLocal<ConnectionHolder> connectionHolder = new ThreadLocal<>();


    public static synchronized TransactionManagerImpl getTransactionManager() {
        if (transactionManager == null)
            transactionManager = new TransactionManagerImpl();

        return transactionManager;
    }

    public static ConnectionHolder getConnectionHolder() {
        ConnectionHolder holder = connectionHolder.get();
        if (holder == null) {
            holder = new ConnectionHolderImpl();
            connectionHolder.set(holder);
        }

        return holder;
    }

    public static boolean isTransactionExists() {
        ConnectionHolder ch = connectionHolder.get();
        return ch != null && ch.isTransactionActive();
    }
}
