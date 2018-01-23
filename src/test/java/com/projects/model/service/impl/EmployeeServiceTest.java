package com.projects.model.service.impl;

import com.projects.helpers.Dummies;
import com.projects.model.dao.EmployeeDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Employee;
import com.projects.model.service.EmployeeService;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmployeeServiceTest {
    private EmployeeDao employeeDao = mock(EmployeeDao.class);
    private EmployeeService employeeService = new EmployeeServiceImpl(employeeDao);

    @Test
    public void create() throws DaoException {
        Employee employee = Dummies.getDummyEmployee(0L);
        employeeService.create(employee);
        verify(employeeDao, times(1)).create(employee);
    }

    @Test
    public void findById() throws DaoException {
        employeeService.findById(1L);
        verify(employeeDao, times(1)).getById(1L);
    }

    @Test
    public void update() throws DaoException {
        Employee employee = Dummies.getDummyEmployee(1L);
        employeeService.update(employee);
        verify(employeeDao, times(1)).update(employee);
    }

    @Test
    public void delete() throws DaoException {
        employeeService.delete(2L);
        verify(employeeDao, times(1)).delete(2L);
    }

    @Test
    public void findAll() throws DaoException {
        employeeService.findAll();
        verify(employeeDao, times(1)).getAll();
    }

    @Test
    public void getForPage() throws DaoException {
        employeeService.getForPage(5, 0);
        verify(employeeDao, times(1)).getPerPage(5, 0);
    }

    @Test
    public void getRecords() throws DaoException {
        employeeService.getRecords();
        verify(employeeDao, times(1)).getCount();
    }

    @Test
    public void findByFirstAndLastName() throws DaoException {
        employeeService.findByFirstAndLastName("John", "Williams");
        verify(employeeDao, times(1)).getByFirstAndLastName("John", "Williams");
    }
}
