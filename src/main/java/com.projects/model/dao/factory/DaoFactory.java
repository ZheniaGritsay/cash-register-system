package com.projects.model.dao.factory;

import com.projects.model.dao.*;

public interface DaoFactory {
    UserDao getUserDao();

    EmployeeDao getEmployeeDao();

    ProductDao getProductDao();

    CheckDao getCheckDao();

    ReportDao getReportDao();
}
