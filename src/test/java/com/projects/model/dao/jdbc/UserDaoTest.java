package com.projects.model.dao.jdbc;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.UserDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.domain.dto.User;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.helpers.DropDataBase;
import com.projects.helpers.Dummies;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserDaoTest {
    private UserDao userDao = new JdbcUserDaoImpl();
    private List<User> userList;

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
        long newId = userDao.create(Dummies.getDummyUser(0L, 2L));
        User user = userDao.getById(newId);

        assertTrue(newId > 0);
        assertEquals(newId, (long) user.getId());
        assertNotNull(user);
    }

    @Test
    public void update() throws DaoException {
        boolean updated = userDao.update(Dummies.getDummyUser(1L, 1L));

        assertTrue(updated);
    }

    @Test
    public void getById() throws DaoException {
        User user = userDao.getById(2L);

        assertEquals(2L, (long) user.getId());
        assertNotNull(user);
    }

    @Test
    public void getAll() throws DaoException {
        userList = userDao.getAll();

        assertNotNull(userList);
        assertTrue(userList.size() > 0);
    }

    @Test
    public void delete() throws DaoException {
        boolean deleted = userDao.delete(3L);

        assertTrue(deleted);
        assertNull(userDao.getById(10L));
    }

    @Test
    public void getPerPage() throws DaoException {
        userList = userDao.getPerPage(3, 0);

        assertNotNull(userList);
        assertEquals(3, userList.size());
    }

    @Test
    public void getCount() throws DaoException {
        int count = userDao.getCount();

        assertTrue(count > 0);
    }

    @Test
    public void getByLogin() throws DaoException {
        User user = userDao.getByLogin("rickLogin");

        assertNotNull(user);
        assertEquals("rickLogin", user.getLogin());

        user = userDao.getByLogin("nonExistsLogin");

        assertNull(user);

        user = userDao.getByLogin(null);

        assertNull(user);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionCreate() throws DaoException {
        userDao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionUpdate() throws DaoException {
        userDao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetById() throws DaoException {
        userDao.getById(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionDelete() throws DaoException {
        userDao.delete(null);
    }
}
