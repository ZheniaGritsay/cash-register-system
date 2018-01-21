package com.projects.model.dao;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Employee;

public interface EmployeeDao extends AbstractDao<Employee, Long> {
    Employee getByFirstAndLastName(String firstName, String lastName) throws DaoException;
}
