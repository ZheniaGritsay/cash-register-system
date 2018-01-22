package com.projects.model.dao.jdbc;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.EmployeeDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.domain.dto.Employee;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.helpers.DropDataBase;
import com.projects.helpers.Dummies;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.*;

public class EmployeeDaoTest {
    private EmployeeDao employeeDao = new JdbcEmployeeDaoImpl();

    @BeforeClass
    public static void setUpClass() throws InitializationException {
        DataBaseInitializer initializer = new DataBaseInitializerImpl();
        initializer.initializeDb();
        ConnectionFactoryImpl.getInstance().setConnectionPool(ConnectionFactoryImpl.getInstance().init());
    }

    @AfterClass
    public static void tearDownClass() {
        DropDataBase.dropDataBase();
    }

    @Test
    public void create() throws DaoException {
        long newId = employeeDao.create(Dummies.getDummyEmployee(0L));
        Employee employee = employeeDao.getById(newId);

        assertTrue(newId > 0);
        assertEquals(newId, (long) employee.getId());
        assertNotNull(employee);
    }

    @Test
    public void update() throws DaoException {
        boolean updated = employeeDao.update(Dummies.getDummyEmployee(1L));

        assertTrue(updated);
    }

    @Test
    public void delete() throws DaoException {
        boolean deleted = employeeDao.delete(5L);

        assertTrue(deleted);
    }

    @Test
    public void getById() throws DaoException {
        Employee employee = employeeDao.getById(2L);

        assertEquals(2L, (long) employee.getId());
        assertNotNull(employee);
    }

    @Test
    public void getAll() throws DaoException {
        List<Employee> employeeList = employeeDao.getAll();

        assertNotNull(employeeList);
        assertTrue(employeeList.size() > 0);
    }

    @Test
    public void getPerPage() throws DaoException {
        List<Employee> employeeList = employeeDao.getPerPage(5, 0);

        assertNotNull(employeeList);
        assertEquals(5, employeeList.size());
    }

    @Test
    public void getCount() throws DaoException {
        int count = employeeDao.getCount();

        assertTrue(count > 0);
    }

    @Test
    public void getByFirstAndLastName() throws DaoException {
        Employee employee = employeeDao.getByFirstAndLastName("John", "Williams");

        assertEquals("John", employee.getFirstName());
        assertEquals("Williams", employee.getLastName());
        assertNotNull(employee);

        employee = employeeDao.getByFirstAndLastName("Non", "Exist");

        assertNull(employee);

        employee = employeeDao.getByFirstAndLastName(null, "Williams");

        assertNull(employee);

        employee = employeeDao.getByFirstAndLastName("John", null);

        assertNull(employee);

        employee = employeeDao.getByFirstAndLastName(null, null);

        assertNull(employee);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionCreate() throws DaoException {
        employeeDao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionUpdate() throws DaoException {
        employeeDao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetById() throws DaoException {
        employeeDao.getById(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionDelete() throws DaoException {
        employeeDao.delete(null);
    }
}
