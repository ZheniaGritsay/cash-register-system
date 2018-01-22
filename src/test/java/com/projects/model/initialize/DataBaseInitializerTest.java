package com.projects.model.initialize;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.*;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.dao.factory.impl.DaoFactoryImpl;
import com.projects.model.domain.dto.*;
import com.projects.helpers.DropDataBase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataBaseInitializerTest {
    private DataBaseInitializer initializer = new DataBaseInitializerImpl();
    private ConnectionFactoryImpl connectionFactory = ConnectionFactoryImpl.getInstance();
    private ProductDao productDao;
    private CheckDao checkDao;
    private ReportDao reportDao;
    private EmployeeDao employeeDao;
    private UserDao userDao;

    @Before
    public void setUp() {
        connectionFactory.setConnectionPool(connectionFactory.init());
        productDao = DaoFactoryImpl.getInstance().getProductDao();
        checkDao = DaoFactoryImpl.getInstance().getCheckDao();
        reportDao = DaoFactoryImpl.getInstance().getReportDao();
        employeeDao = DaoFactoryImpl.getInstance().getEmployeeDao();
        userDao = DaoFactoryImpl.getInstance().getUserDao();
    }

    @After
    public void tearDown() {
        DropDataBase.dropDataBase();
    }

    @Test
    public void initializeDb() throws InitializationException, DaoException {
        initializer.initializeDb();

        Product product = productDao.getById(1L);
        assertNotNull(product);
        assertEquals(1L, (long) product.getId());

        Check check = checkDao.getById(2L);
        assertNotNull(check);
        assertEquals(2L, (long) check.getId());

        Report report = reportDao.getById(3L);
        assertNotNull(report);
        assertEquals(3L, (long) report.getId());

        Employee employee = employeeDao.getById(4L);
        assertNotNull(employee);
        assertEquals(4L, (long) employee.getId());

        User user = userDao.getById(2L);
        assertNotNull(user);
        assertEquals(2L, (long) user.getId());
    }
}
