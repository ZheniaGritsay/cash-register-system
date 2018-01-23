package com.projects.model.transaction;

import com.projects.helpers.DropDataBase;
import com.projects.helpers.Dummies;
import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.EmployeeDao;
import com.projects.model.dao.UserDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.dao.jdbc.JdbcEmployeeDaoImpl;
import com.projects.model.dao.jdbc.JdbcUserDaoImpl;
import com.projects.model.domain.dto.Employee;
import com.projects.model.domain.dto.User;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.model.transaction.exception.TransactionException;
import com.projects.model.transaction.impl.TransactionManagerService;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TransactionManagerTest {
    private TransactionManager tm = TransactionManagerService.getTransactionManager();
    private UserDao userDao = new JdbcUserDaoImpl();
    private EmployeeDao employeeDao = new JdbcEmployeeDaoImpl();
    private User user;
    private Employee employee;

    @BeforeClass
    public static void setUpClass() throws InitializationException {
        DataBaseInitializer initializer = new DataBaseInitializerImpl();
        initializer.initializeDb();
        ConnectionFactoryImpl.getInstance().setConnectionPool(ConnectionFactoryImpl.getInstance().init());
    }

    @AfterClass
    public static void tearDownClass() {
        DropDataBase.dropDataBase();
    }

    @Test
    public void rollback() throws TransactionException {
        employee = Dummies.getDummyEmployee(0L);
        user = Dummies.getDummyUser(0L, 20L);
        long createdEmployeeId = 0L;

        tm.begin();
        try {
            createdEmployeeId = employeeDao.create(employee);
            userDao.create(user);

            tm.commit();
        } catch (DaoException e) {
            tm.rollback();
        }

        try {
            assertNull(employeeDao.getById(createdEmployeeId));
        } catch (DaoException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void commit() throws TransactionException {
        employee = Dummies.getDummyEmployee(0L);
        user = Dummies.getDummyUser(0L, 6L);
        long createdEmployeeId = 0L;
        long createdUserId = 0L;

        tm.begin();
        try {
            createdEmployeeId = employeeDao.create(employee);
            createdUserId = userDao.create(user);

            tm.commit();
        } catch (DaoException e) {
            tm.rollback();
        }

        Employee createdEmployee = null;
        User createdUser = null;
        try {
            createdEmployee = employeeDao.getById(createdEmployeeId);
            createdUser = userDao.getById(createdUserId);
        } catch (DaoException e) {
            e.printStackTrace();
        }

        assertNotNull(createdUser);
        assertNotNull(createdEmployee);
        assertEquals(createdUserId, (long) createdUser.getId());
        assertEquals(createdEmployeeId, (long) createdEmployee.getId());

    }
}
