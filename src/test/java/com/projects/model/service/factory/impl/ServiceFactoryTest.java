package com.projects.model.service.factory.impl;

import com.projects.model.service.*;
import com.projects.model.service.factory.ServiceFactory;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public class ServiceFactoryTest {
    private ServiceFactory serviceFactory = ServiceFactoryImpl.getInstance();

    @Test
    public void getUserService() {
        UserService userService = serviceFactory.getUserService();
        UserService anotherUserService = serviceFactory.getUserService();

        assertSame(userService, anotherUserService);
    }

    @Test
    public void getEmployeeService() {
        EmployeeService employeeService = serviceFactory.getEmployeeService();
        EmployeeService anotherEmployeeService = serviceFactory.getEmployeeService();

        assertSame(employeeService, anotherEmployeeService);
    }

    @Test
    public void getProductService() {
        ProductService productService = serviceFactory.getProductService();
        ProductService anotherProductService = serviceFactory.getProductService();

        assertSame(productService, anotherProductService);
    }

    @Test
    public void getCheckService() {
        CheckService checkService = serviceFactory.getCheckService();
        CheckService anotherCheckService = serviceFactory.getCheckService();

        assertSame(checkService, anotherCheckService);
    }

    @Test
    public void getReportService() {
        ReportService reportService = serviceFactory.getReportService();
        ReportService anotherReportService = serviceFactory.getReportService();

        assertSame(reportService, anotherReportService);
    }
}
