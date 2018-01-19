package com.projects.model.dao;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.User;

public interface UserDao extends AbstractDao<User, Long> {
    User getByLogin(String login) throws DaoException;
}
