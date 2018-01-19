package com.projects.model.service.impl;

import com.projects.model.dao.EmployeeDao;
import com.projects.model.domain.dto.Employee;
import com.projects.model.service.EmployeeService;

public class EmployeeServiceImpl extends AbstractServiceImpl<EmployeeDao, Employee, Long> implements EmployeeService {

    public EmployeeServiceImpl(EmployeeDao dao) {
        super(dao);
    }
}
