package com.projects.model.dao.factory.impl;

import com.projects.model.dao.*;
import com.projects.model.dao.factory.DaoFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class DaoFactoryTest {
    private DaoFactory daoFactory = DaoFactoryImpl.getInstance();

    @Test
    public void getUserDao() {
        UserDao userDao = daoFactory.getUserDao();
        UserDao anotherUserDao = daoFactory.getUserDao();

        assertSame(userDao, anotherUserDao);
    }

    @Test
    public void getEmployeeDao() {
        EmployeeDao employeeDao = daoFactory.getEmployeeDao();
        EmployeeDao anotherEmployeeDao = daoFactory.getEmployeeDao();

        assertSame(employeeDao, anotherEmployeeDao);
    }

    @Test
    public void getProductDao() {
        ProductDao productDao = daoFactory.getProductDao();
        ProductDao anotherProductDao = daoFactory.getProductDao();

        assertSame(productDao, anotherProductDao);
    }

    @Test
    public void getCheckDao() {
        CheckDao checkDao = daoFactory.getCheckDao();
        CheckDao anotherCheckDao = daoFactory.getCheckDao();

        assertSame(checkDao, anotherCheckDao);
    }

    @Test
    public void getReportDao() {
        ReportDao reportDao = daoFactory.getReportDao();
        ReportDao anotherReportDao = daoFactory.getReportDao();

        assertSame(reportDao, anotherReportDao);
    }
}
