package com.projects.model.transaction.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class ConnectionHandler implements InvocationHandler {
    private Object target;

    public ConnectionHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object retVal = null;

        if ("close".equals(method.getName())) {
            if (!TransactionManagerService.isTransactionExists()) {
                retVal = method.invoke(target, args);
            }
        } else {
            retVal = method.invoke(target, args);
        }

        return retVal;
    }
}
