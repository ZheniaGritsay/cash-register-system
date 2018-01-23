package com.projects.model.service.impl;

import com.projects.helpers.Dummies;
import com.projects.model.dao.UserDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.User;
import com.projects.model.service.UserService;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UserServiceTest {
    private UserDao userDao = mock(UserDao.class);
    private UserService userService = new UserServiceImpl(userDao);

    @Test
    public void create() throws DaoException {
        User user = Dummies.getDummyUser(0L, 1L);
        userService.create(user);
        verify(userDao, times(1)).create(user);
    }

    @Test
    public void findById() throws DaoException {
        userService.findById(1L);
        verify(userDao, times(1)).getById(1L);
    }

    @Test
    public void update() throws DaoException {
        User user = Dummies.getDummyUser(1L, 2L);
        userService.update(user);
        verify(userDao, times(1)).update(user);
    }

    @Test
    public void delete() throws DaoException {
        userService.delete(2L);
        verify(userDao, times(1)).delete(2L);
    }

    @Test
    public void findAll() throws DaoException {
        userService.findAll();
        verify(userDao, times(1)).getAll();
    }

    @Test
    public void getForPage() throws DaoException {
        userService.getForPage(5, 0);
        verify(userDao, times(1)).getPerPage(5, 0);
    }

    @Test
    public void getRecords() throws DaoException {
        userService.getRecords();
        verify(userDao, times(1)).getCount();
    }

    @Test
    public void findByLogin() throws DaoException {
        userService.findByLogin("rickLogin");
        verify(userDao, times(1)).getByLogin("rickLogin");
    }

    @Test
    public void authentication() throws DaoException {
        userService.authentication("kateLogin", "katePass");
        verify(userDao, times(1)).getByLogin("kateLogin");
    }
}
