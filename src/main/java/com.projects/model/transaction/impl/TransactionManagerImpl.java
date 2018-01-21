package com.projects.model.transaction.impl;

import com.projects.model.transaction.ConnectionHolder;
import com.projects.model.transaction.TransactionManager;
import com.projects.model.transaction.TransactionObject;
import com.projects.model.transaction.exception.TransactionException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionManagerImpl implements TransactionManager {
    private Map<Thread, ThreadContext> contexts;

    public TransactionManagerImpl() {
        this.contexts = new ConcurrentHashMap<>();
    }

    @Override
    public void begin() throws TransactionException {
        TransactionObject transaction = getCurrentTransaction();
        if (transaction == null)
            transaction = createTransaction();

        int isolation = getOrCreateCurrentContext().getTransactionIsolation();
        transaction.setIsolationLevel(isolation);

        transaction.begin();
    }

    @Override
    public void commit() throws TransactionException {
        TransactionObject transaction = getCurrentTransaction();
        if (transaction == null)
            throw new TransactionException("no transaction started");

        transaction.commit();
        releaseResources();

    }

    @Override
    public void rollback() throws TransactionException {
        TransactionObject transaction = getCurrentTransaction();
        if (transaction == null)
            throw new TransactionException("no transaction started");

        transaction.rollback();
        releaseResources();
    }

    @Override
    public void setTransactionIsolation(int transactionIsolation) {
        getOrCreateCurrentContext().setTransactionIsolation(transactionIsolation);
    }

    private TransactionObject getCurrentTransaction() {
        if (contexts.get(Thread.currentThread()) == null)
            return null;

        return getOrCreateCurrentContext().getTransaction();
    }

    private ThreadContext getOrCreateCurrentContext() {
        ThreadContext threadContext = contexts.get(Thread.currentThread());
        if (threadContext == null) {
            threadContext = new ThreadContext();
            setCurrentContext(threadContext);
        }

        return threadContext;
    }

    private void setCurrentContext(ThreadContext context) {
        if (context == null)
            throw new IllegalArgumentException("context cannot be null");

        contexts.put(Thread.currentThread(), context);
    }

    private TransactionObject createTransaction() {
        ConnectionHolder connectionHolder = TransactionManagerService.getConnectionHolder();
        TransactionObject transaction = new TransactionObjectImpl(connectionHolder);
        getOrCreateCurrentContext().setTransaction(transaction);
        return transaction;
    }

    private void releaseResources() {
        contexts.remove(Thread.currentThread());
    }
}
