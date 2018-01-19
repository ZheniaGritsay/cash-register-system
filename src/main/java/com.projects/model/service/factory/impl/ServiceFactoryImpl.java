package com.projects.model.service.factory.impl;

import com.projects.model.dao.factory.DaoFactory;
import com.projects.model.dao.factory.impl.DaoFactoryImpl;
import com.projects.model.service.*;
import com.projects.model.service.factory.ServiceFactory;
import com.projects.model.service.impl.*;

public class ServiceFactoryImpl implements ServiceFactory {
    private static ServiceFactoryImpl instance = new ServiceFactoryImpl();
    private final DaoFactory daoFactory = DaoFactoryImpl.getInstance();
    private final CheckService checkService = new CheckServiceImpl(daoFactory.getCheckDao());
    private final EmployeeService employeeService = new EmployeeServiceImpl(daoFactory.getEmployeeDao());
    private final ProductService productService = new ProductServiceImpl(daoFactory.getProductDao());
    private final ReportService reportService = new ReportServiceImpl(daoFactory.getReportDao());
    private final UserService userService = new UserServiceImpl(daoFactory.getUserDao());

    @Override
    public CheckService getCheckService() {
        return checkService;
    }

    @Override
    public EmployeeService getEmployeeService() {
        return employeeService;
    }

    @Override
    public ProductService getProductService() {
        return productService;
    }

    @Override
    public ReportService getReportService() {
        return reportService;
    }

    @Override
    public UserService getUserService() {
        return userService;
    }

    public static synchronized ServiceFactoryImpl getInstance() {
        return instance;
    }
}
