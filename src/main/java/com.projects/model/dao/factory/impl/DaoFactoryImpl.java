package com.projects.model.dao.factory.impl;

import com.projects.model.dao.*;
import com.projects.model.dao.factory.DaoFactory;
import com.projects.model.dao.jdbc.*;

public class DaoFactoryImpl implements DaoFactory {
    private static DaoFactoryImpl instance = new DaoFactoryImpl();
    private final UserDao userDao = new JdbcUserDaoImpl();
    private final EmployeeDao employeeDao = new JdbcEmployeeDaoImpl();
    private final ProductDao productDao = new JdbcProductDaoImpl();
    private final CheckDao checkDao = new JdbcCheckDaoImpl();
    private final ReportDao reportDao = new JdbcReportDaoImpl();

    private DaoFactoryImpl(){
    }

    @Override
    public UserDao getUserDao() {
        return userDao;
    }

    @Override
    public EmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    @Override
    public ProductDao getProductDao() {
        return productDao;
    }

    @Override
    public CheckDao getCheckDao() {
        return checkDao;
    }

    @Override
    public ReportDao getReportDao() {
        return reportDao;
    }

    public static DaoFactoryImpl getInstance() {
        return instance;
    }
}
