package com.projects.model.service;

import com.projects.model.domain.Entity;
import com.projects.model.dao.exception.DaoException;

import java.util.List;

public interface AbstractService<T extends Entity, PK extends Number> {
    T create(T entity) throws DaoException;

    T findById(long id) throws DaoException;

    boolean update(T entity) throws DaoException;

    boolean delete(long id) throws DaoException;

    List<T> findAll() throws DaoException;

    List<T> getForPage(int amount, int offset) throws DaoException;

    int getRecords() throws DaoException;
}
