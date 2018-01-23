package com.projects.model.transaction;

import com.projects.model.transaction.impl.TransactionManagerService;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionManagerServiceTest {

    @Test
    public void getTransactionManager() {
        TransactionManager tm = TransactionManagerService.getTransactionManager();
        TransactionManager anotherTm = TransactionManagerService.getTransactionManager();

        assertSame(tm, anotherTm);
    }
}
