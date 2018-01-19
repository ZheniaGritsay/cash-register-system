package com.projects.model.dao;

import com.projects.model.domain.Entity;
import com.projects.model.dao.exception.DaoException;

import java.util.List;

public interface AbstractDao<T extends Entity, PK extends Number> {
    long create(T entity) throws DaoException;

    T getById(PK id) throws DaoException;

    boolean update(T entity) throws DaoException;

    boolean delete(PK id) throws DaoException;

    List<T> getAll() throws DaoException;

    List<T> getPerPage(int amount, int offset) throws DaoException;

    int getCount() throws DaoException;
}
