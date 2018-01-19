package com.projects.model.service.impl;

import com.projects.model.dao.UserDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.User;
import com.projects.model.service.UserService;
import com.projects.util.Encryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserServiceImpl extends AbstractServiceImpl<UserDao, User, Long> implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserDao dao) {
        super(dao);
    }

    @Override
    public User findByLogin(String login) throws DaoException {
        User userByLogin = dao.getByLogin(login);
        logger.info("retrieved a user by login");
        return userByLogin;
    }

    @Override
    public User authentication(String login, String password) throws DaoException {
        User user = dao.getByLogin(login);
        if (user != null && user.getPassword().equals(Encryptor.encrypt(password, Encryptor.SHA256))) {
            logger.info("user authentication succeeded");
            return user;
        }
        logger.info("user authentication did not succeed");
        return null;
    }
}
