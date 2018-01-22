package com.projects.model.dao.jdbc;

import com.projects.model.dao.CheckDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.CheckStatus;
import com.projects.model.domain.constant.Position;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Employee;
import com.projects.model.holders.CheckHolder;
import com.projects.model.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCheckDaoImpl extends JdbcAbstractDaoImpl<Check, Long> implements CheckDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcCheckDaoImpl.class);

    @Override
    protected SQLQueries getSQLQueries() {
        return new SQLQueries(SQLQueries.CHECK_QUERIES);
    }

    @Override
    protected void preparedStatementForCreate(PreparedStatement pStatement, Check check) {
        try {
            pStatement.setLong(1, check.getEmployee().getId());
            pStatement.setDouble(2, check.getSum());
            pStatement.setTimestamp(3, Timestamp.valueOf(check.getDate()));
            pStatement.setInt(4, check.getStatus().ordinal());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for create: " + e.getMessage());
        }
    }

    @Override
    protected void preparedStatementForUpdate(PreparedStatement pStatement, Check check) {
        try {
            preparedStatementForCreate(pStatement, check);
            pStatement.setLong(5, check.getId());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for update: " + e.getMessage());
        }
    }

    @Override
    protected List<Check> parseResultSet(ResultSet resultSet) {
        List<Check> checkList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Employee employee = new Employee.Builder()
                        .id(resultSet.getLong("employee_id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .salary(resultSet.getDouble("salary"))
                        .position(Position.values()[resultSet.getInt("position_id")])
                        .build();
                Check check = new CheckHolder.BuilderHolder()
                        .id(resultSet.getLong("id"))
                        .employee(employee)
                        .products(null)
                        .sum(resultSet.getDouble("sum"))
                        .date(resultSet.getTimestamp("check_date").toLocalDateTime())
                        .status(CheckStatus.values()[resultSet.getInt("status_id")])
                        .build();

                checkList.add(check);
            }
        } catch (SQLException e) {
            logger.error("failed parse result set: " + e.getMessage());
        }
        return checkList;
    }

    @Override
    public List<Check> getAllByDate(String date) throws DaoException {
        List<Check> list = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_ALL_BY_DATE));
            ps.setString(1, date + "%");
            return ps;
        });

        return list;
    }

    @Override
    public List<Check> getAllByReportId(long reportId) throws DaoException {
        List<Check> list = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_ALL_BY_REPORT_ID));
            ps.setLong(1, reportId);
            return ps;
        });

        return list;
    }

    @Override
    public List<Check> getAllByEmployeeId(long employeeId) throws DaoException {
        List<Check> list = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_ALL_BY_EMPLOYEE_ID));
            ps.setLong(1, employeeId);
            return ps;
        });

        return list;
    }

    @Override
    public boolean detachProduct(long checkId, long productId) throws DaoException {
        boolean detached;

        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.DELETE_FROM_CHECKS_PRODUCTS))) {

            pStatement.setLong(1, checkId);
            pStatement.setLong(2, productId);

            detached = pStatement.execute();

        } catch (SQLException e) {
            logger.error("failed to detach a product from the check: " + e.getMessage());
            throw new DaoException("unable to detach product", e);
        }

        return detached;
    }

    @Override
    public boolean attachProduct(long checkId, long productId, int boughtQuantity) throws DaoException {
        boolean attached;

        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.INSERT_INTO_CHECKS_PRODUCTS))) {

            pStatement.setLong(1, checkId);
            pStatement.setLong(2, productId);
            pStatement.setLong(3, boughtQuantity);

            attached = pStatement.execute();

        } catch (SQLException e) {
            logger.error("failed to attach a product from the check: " + e.getMessage());
            throw new DaoException("unable to attach product", e);
        }

        return attached;
    }

    @Override
    public boolean updateProductQuantity(int boughtQuantity, long checkId, long productId) throws DaoException {
        boolean updated;

        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.UPDATE_CHECKS_PRODUCTS))) {

            pStatement.setLong(1, boughtQuantity);
            pStatement.setLong(2, checkId);
            pStatement.setLong(3, productId);

            updated = pStatement.execute();

        } catch (SQLException e) {
            logger.error("failed to update a product in the check: " + e.getMessage());
            throw new DaoException("unable to update product in the check", e);
        }

        return updated;
    }
}
