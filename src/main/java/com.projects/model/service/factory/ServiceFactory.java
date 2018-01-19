package com.projects.model.service.factory;

import com.projects.model.service.*;

public interface ServiceFactory {
    CheckService getCheckService();

    EmployeeService getEmployeeService();

    ProductService getProductService();

    ReportService getReportService();

    UserService getUserService();
}
