package com.projects.model.service.impl;

import com.projects.model.dao.AbstractDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.Entity;
import com.projects.model.service.AbstractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractServiceImpl<Dao extends AbstractDao, T extends Entity, PK extends Number> implements AbstractService<T, PK> {
    private static Logger logger = LoggerFactory.getLogger(AbstractService.class);
    protected final Dao dao;

    protected AbstractServiceImpl(Dao dao) {
        this.dao = dao;
    }

    @Override
    public T create(T entity) throws DaoException {
        long id = dao.create(entity);
        logger.info("created an entity");
        return (T) dao.getById(id);
    }

    @Override
    public T findById(long id) throws DaoException {
        T entity = (T) dao.getById(id);
        logger.info("retrieved an entity by id");
        return entity;
    }

    @Override
    public boolean update(T entity) throws DaoException {
        boolean updated = dao.update(entity);
        logger.info("updated an entity");
        return updated;
    }

    @Override
    public boolean delete(long id) throws DaoException {
        boolean deleted = dao.delete(id);
        logger.info("deleted an entity");
        return deleted;
    }

    @Override
    public List<T> findAll() throws DaoException {
        List<T> entities = dao.getAll();
        logger.info("retrieved all entities");
        return entities;
    }

    @Override
    public List<T> getForPage(int amount, int offset) throws DaoException {
        List<T> entitiesPerPage = dao.getPerPage(amount, offset);
        logger.info("retrieved entities per page");
        return entitiesPerPage;
    }

    @Override
    public int getRecords() throws DaoException {
        int records = dao.getCount();
        logger.info("retrieved number of the entity records");
        return records;
    }
}
