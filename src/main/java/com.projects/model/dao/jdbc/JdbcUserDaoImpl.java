package com.projects.model.dao.jdbc;

import com.projects.model.dao.UserDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.Position;
import com.projects.model.domain.constant.Role;
import com.projects.model.domain.dto.Employee;
import com.projects.model.domain.dto.User;
import com.projects.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDaoImpl extends JdbcAbstractDaoImpl<User, Long> implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcUserDaoImpl.class);

    @Override
    protected SQLQueries getSQLQueries() {
        return new SQLQueries(SQLQueries.USER_QUERIES);
    }

    @Override
    protected void preparedStatementForCreate(PreparedStatement pStatement, User user) {
        try {
            pStatement.setString(1, user.getLogin());
            pStatement.setString(2, user.getPassword());
            pStatement.setInt(3, user.getRole().ordinal());
            pStatement.setLong(4, user.getEmployee().getId());
        } catch (SQLException e) {

        }
    }

    @Override
    protected void preparedStatementForUpdate(PreparedStatement pStatement, User user) {
        try {
            preparedStatementForCreate(pStatement, user);
            pStatement.setLong(5, user.getId());
        } catch (SQLException e) {

        }
    }

    @Override
    protected List<User> parseResultSet(ResultSet resultSet) {
        List<User> userList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Employee employee = new Employee.Builder()
                        .id(resultSet.getLong("employee_id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .salary(resultSet.getDouble("salary"))
                        .position(Position.values()[resultSet.getInt("position_id")])
                        .build();
                User user = new User(
                        resultSet.getLong("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        Role.values()[resultSet.getInt("role_id")],
                        employee
                );

                userList.add(user);
            }
        } catch (SQLException e) {

        }

        return userList;
    }

    @Override
    public User getByLogin(String login) throws DaoException {
        User user;
        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_BY_LOGIN))) {
            pStatement.setString(1, login);
            user = parseResultSet(pStatement.executeQuery()).get(0);
        } catch (SQLException e) {
            logger.error("failed to get a user by login", e);
            throw new DaoException("unable to get a user by login: " + e.getMessage());
        }

        return user;
    }
}
