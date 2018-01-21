package com.projects.model.dao.jdbc;

import com.projects.model.dao.EmployeeDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.Position;
import com.projects.model.domain.dto.Employee;
import com.projects.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcEmployeeDaoImpl extends JdbcAbstractDaoImpl<Employee, Long> implements EmployeeDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcEmployeeDaoImpl.class);

    @Override
    protected SQLQueries getSQLQueries() {
        return new SQLQueries(SQLQueries.EMPLOYEE_QUERIES);
    }

    @Override
    protected void preparedStatementForCreate(PreparedStatement pStatement, Employee employee) {
        try {
            pStatement.setString(1, employee.getFirstName());
            pStatement.setString(2, employee.getLastName());
            pStatement.setString(3, employee.getEmail());
            pStatement.setDouble(4, employee.getSalary());
            pStatement.setLong(5, employee.getPosition().ordinal());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for create: " + e.getMessage());
        }
    }

    @Override
    protected void preparedStatementForUpdate(PreparedStatement pStatement, Employee employee) {
        try {
            preparedStatementForCreate(pStatement, employee);
            pStatement.setLong(6, employee.getId());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for update: " + e.getMessage());
        }
    }

    @Override
    protected List<Employee> parseResultSet(ResultSet resultSet) {
        List<Employee> employeeList = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Employee employee = new Employee.Builder()
                        .id(resultSet.getLong("id"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .email(resultSet.getString("email"))
                        .salary(resultSet.getDouble("salary"))
                        .position(Position.values()[resultSet.getInt("position_id")])
                        .build();

                employeeList.add(employee);
            }
        } catch (SQLException e) {
            logger.error("failed parse result set: " + e.getMessage());
        }
        return employeeList;
    }

    @Override
    public Employee getByFirstAndLastName(String firstName, String lastName) throws DaoException {
        Employee employee = null;
        List<Employee> list = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_BY_FIRST_AND_LAST_NAME));
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            return ps;
        });

        if (!list.isEmpty()) {
            employee = list.get(0);
        }

        return employee;
    }
}
