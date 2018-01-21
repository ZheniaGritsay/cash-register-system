package com.projects.model.service;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Employee;

public interface EmployeeService extends AbstractService<Employee, Long> {
    Employee findByFirstAndLastName(String firstName, String lastName) throws DaoException;
}
