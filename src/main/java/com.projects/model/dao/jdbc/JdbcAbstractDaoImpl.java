package com.projects.model.dao.jdbc;

import com.projects.model.connection.ConnectionFactory;
import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.AbstractDao;
import com.projects.model.dao.PreparedStatementConfig;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.Entity;
import com.projects.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.List;

public abstract class JdbcAbstractDaoImpl<T extends Entity, PK extends Number> implements AbstractDao<T, PK> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcAbstractDaoImpl.class);
    private final ConnectionFactory connectionFactory = ConnectionFactoryImpl.getInstance();

    protected abstract SQLQueries getSQLQueries();

    protected abstract void preparedStatementForCreate(PreparedStatement pStatement, T entity);

    protected abstract void preparedStatementForUpdate(PreparedStatement pStatement, T entity);

    protected abstract List<T> parseResultSet(ResultSet resultSet);

    protected Connection getConnection() {
        return connectionFactory.getConnection();
    }

    @Override
    public long create(T entity) throws DaoException {
        long id;
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.CREATE),
                    Statement.RETURN_GENERATED_KEYS)) {
            preparedStatementForCreate(pStatement, entity);
            pStatement.execute();

            ResultSet resultSet = pStatement.getGeneratedKeys();
            resultSet.next();
            id = resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error("failed to create entity", e);
            throw new DaoException("unable to create: " + e.getMessage());
        }
        return id;
    }

    @Override
    public T getById(PK id) throws DaoException {
        T entity;
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_BY_ID))) {
            pStatement.setLong(1, id.longValue());
            ResultSet resultSet = pStatement.executeQuery();

            List<T> list = parseResultSet(resultSet);
            if (!list.isEmpty()) {
                entity = list.get(0);
            } else {
                entity = null;
            }

        } catch (SQLException e) {
            logger.error("failed to get an entity by id", e);
            throw new DaoException("unable to get by id: " + e.getMessage());
        }
        return entity;
    }

    @Override
    public boolean update(T entity) throws DaoException {
        boolean updated;
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.UPDATE))) {

            preparedStatementForUpdate(pStatement, entity);
            updated = pStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("failed to update an entity", e);
            throw new DaoException("unable to update: " + e.getMessage());
        }
        return updated;
    }

    @Override
    public boolean delete(PK id) throws DaoException {
        boolean removed;
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getSQLQueries()
                     .getQuery(SQLQueries.DELETE))) {

            pStatement.setLong(1, id.longValue());
            removed = pStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            logger.error("failed to delete an entity", e);
            throw new DaoException("unable to delete: " + e.getMessage());
        }
        return removed;
    }

    @Override
    public List<T> getAll() throws DaoException {
        List<T> list;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getSQLQueries().getQuery(SQLQueries.GET_ALL))) {

            list = parseResultSet(resultSet);

        } catch (SQLException e) {
            logger.error("failed to get all entities", e);
            throw new DaoException("unable to get all: " + e.getMessage());
        }
        return list;
    }

    @Override
    public List<T> getPerPage(int amount, int offset) throws DaoException {
        List<T> list;
        try (Connection connection = getConnection();
             PreparedStatement pStatement = connection.prepareStatement(getSQLQueries()
                     .getQuery(SQLQueries.PAGINATION))) {

            pStatement.setInt(1, amount);
            pStatement.setInt(2, offset);
            list = parseResultSet(pStatement.executeQuery());

        } catch (SQLException e) {
            logger.error("failed to get entities per page", e);
            throw new DaoException("unable to get per page: " + e.getMessage());
        }
        return list;
    }

    @Override
    public int getCount() throws DaoException {
        int count;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(getSQLQueries().getQuery(SQLQueries.COUNT))) {

            resultSet.next();
            count = resultSet.getInt(1);

        } catch (SQLException e) {
            logger.error("failed to count entities", e);
            throw new DaoException("unable to execute count: " + e.getMessage());
        }
        return count;
    }

    protected List<T> preparedStatementExec(PreparedStatementConfig pStatementConfig) throws DaoException {
        List<T> entityList;
        try(Connection connection = getConnection();
            PreparedStatement pStatement = pStatementConfig.configure(connection)) {

            entityList = parseResultSet(pStatement.executeQuery());

        } catch (SQLException e) {
            logger.error("failed to execute prepared statement", e);
            throw new DaoException("unable to execute prepared statement: " + e.getMessage());
        }
        return entityList;
    }
}
