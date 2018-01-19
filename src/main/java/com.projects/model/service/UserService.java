package com.projects.model.service;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.User;

public interface UserService extends AbstractService<User, Long> {
    User findByLogin(String login) throws DaoException;

    User authentication(String login, String password) throws DaoException;
}
