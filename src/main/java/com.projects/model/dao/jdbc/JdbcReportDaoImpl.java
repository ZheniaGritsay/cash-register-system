package com.projects.model.dao.jdbc;

import com.projects.model.dao.ReportDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Report;
import com.projects.model.holders.ReportHolder;
import com.projects.model.util.SQLQueries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcReportDaoImpl extends JdbcAbstractDaoImpl<Report, Long> implements ReportDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcReportDaoImpl.class);

    @Override
    protected SQLQueries getSQLQueries() {
        return new SQLQueries(SQLQueries.REPORT_QUERIES);
    }

    @Override
    protected void preparedStatementForCreate(PreparedStatement pStatement, Report report) {
        try {
            pStatement.setTimestamp(1, Timestamp.valueOf(report.getSince()));
            pStatement.setTimestamp(2, Timestamp.valueOf(report.getUntil()));
            pStatement.setDouble(3, report.getTotalSum());
            pStatement.setTimestamp(4, Timestamp.valueOf(report.getCreationDate()));
            pStatement.setInt(5, report.getType().ordinal());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for create: " + e.getMessage());
        }
    }

    @Override
    protected void preparedStatementForUpdate(PreparedStatement pStatement, Report report) {
        try {
            preparedStatementForCreate(pStatement, report);
            pStatement.setLong(6, report.getId());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for update: " + e.getMessage());
        }
    }

    @Override
    protected List<Report> parseResultSet(ResultSet resultSet) {
        List<Report> reportList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Report report = new ReportHolder.BuilderHolder()
                        .id(resultSet.getLong("id"))
                        .since(resultSet.getTimestamp("since_date").toLocalDateTime())
                        .until(resultSet.getTimestamp("until_date").toLocalDateTime())
                        .checks(null)
                        .totalSum(resultSet.getDouble("total_sum"))
                        .creationDate(resultSet.getTimestamp("creation_date").toLocalDateTime())
                        .type(ReportType.values()[resultSet.getInt("type_id")])
                        .build();

                reportList.add(report);
            }
        } catch (SQLException e) {
            logger.error("failed parse result set: " + e.getMessage());
        }

        return reportList;
    }

    @Override
    public List<Report> getAllByType(ReportType type) throws DaoException {
        List<Report> list = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_ALL_BY_TYPE));
            ps.setInt(1, type.ordinal());
            return ps;
        });
        return list;
    }

    @Override
    public boolean appendCheck(long reportId, long checkId) throws DaoException {
        boolean appended;

        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.INSERT_INTO_REPORTS_CHECKS))) {

            pStatement.setLong(1, reportId);
            pStatement.setLong(2, checkId);

            appended = pStatement.execute();

        } catch (SQLException e) {
            logger.error("failed to append a check to the report: " + e.getMessage());
            throw new DaoException("unable to append a check", e);
        }

        return appended;
    }

    @Override
    public boolean removeCheck(long reportId, long checkId) throws DaoException {
        boolean removed;

        try(Connection connection = getConnection();
            PreparedStatement pStatement = connection.prepareStatement(getSQLQueries().getQuery(SQLQueries.DELETE_FROM_REPORTS_CHECKS))) {

            pStatement.setLong(1, reportId);
            pStatement.setLong(2, checkId);

            removed = pStatement.execute();

        } catch (SQLException e) {
            logger.error("failed to remove a check from the report: " + e.getMessage());
            throw new DaoException("unable to remove a check", e);
        }

        return removed;
    }
}
