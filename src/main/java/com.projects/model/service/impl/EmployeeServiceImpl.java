package com.projects.model.service.impl;

import com.projects.model.dao.EmployeeDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Employee;
import com.projects.model.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmployeeServiceImpl extends AbstractServiceImpl<EmployeeDao, Employee, Long> implements EmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    public EmployeeServiceImpl(EmployeeDao dao) {
        super(dao);
    }

    @Override
    public Employee findByFirstAndLastName(String firstName, String lastName) throws DaoException {
        Employee employee = dao.getByFirstAndLastName(firstName, lastName);
        logger.info("retrieved employee by first and last name");
        return employee;
    }
}
