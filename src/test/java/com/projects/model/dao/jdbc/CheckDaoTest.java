package com.projects.model.dao.jdbc;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.CheckDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.domain.dto.Check;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.helpers.DropDataBase;
import com.projects.helpers.Dummies;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;

public class CheckDaoTest {
    private CheckDao checkDao = new JdbcCheckDaoImpl();

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
        long newId = checkDao.create(Dummies.getDummyCheck(0L, 1L));
        Check check = checkDao.getById(newId);

        assertTrue(newId > 0);
        assertEquals(newId, (long) check.getId());
        assertNotNull(check);
    }

    @Test
    public void update() throws DaoException {
        boolean updated = checkDao.update(Dummies.getDummyCheck(1L, 2L));

        assertTrue(updated);
    }

    @Test
    public void getById() throws DaoException {
        Check check = checkDao.getById(2L);

        assertEquals(2L, (long) check.getId());
        assertNotNull(check);
    }

    @Test
    public void getAll() throws DaoException {
        List<Check> checkList = checkDao.getAll();

        assertNotNull(checkList);
        assertTrue(checkList.size() > 0);
    }

    @Test
    public void getPerPage() throws DaoException {
        List<Check> checkList = checkDao.getPerPage(5, 0);

        assertNotNull(checkList);
        assertEquals(5, checkList.size());
    }

    @Test
    public void getCount() throws DaoException {
        int count = checkDao.getCount();

        assertTrue(count > 0);
    }

    @Test
    public void getAllByDate() throws DaoException {
        List<Check> checkList = checkDao.getAllByDate("01-10-2018");

        assertNotNull(checkList);
        assertEquals(1, checkList.size());

        checkList = checkDao.getAllByDate("01");

        assertNotNull(checkList);
        assertTrue(checkList.size() > 1);

        checkList = checkDao.getAllByDate(null);

        assertTrue(checkList.isEmpty());
    }

    @Test
    public void getAllByReportId() throws DaoException {
        List<Check> checkList = checkDao.getAllByReportId(1L);

        assertNotNull(checkList);
        assertTrue(checkList.size() > 0);
    }

    @Test
    public void getAllByEmployeeId() throws DaoException {
        List<Check> checkList = checkDao.getAllByEmployeeId(1L);

        assertNotNull(checkList);
        assertTrue(checkList.size() > 0);
    }

    @Test
    public void detachProduct() throws DaoException {
        boolean detached = checkDao.detachProduct(3L, 10L);

        assertTrue(detached);
    }

    @Test
    public void attachProduct() throws DaoException {
        boolean attached = checkDao.attachProduct(1L, 4L, 150);

        assertTrue(attached);
    }

    @Test
    public void updateProductQuantity() throws DaoException {
        boolean detached = checkDao.updateProductQuantity(200, 2L, 4L);

        assertTrue(detached);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionCreate() throws DaoException {
        checkDao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionUpdate() throws DaoException {
        checkDao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetById() throws DaoException {
        checkDao.getById(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionDelete() throws DaoException {
        checkDao.delete(null);
    }
}
