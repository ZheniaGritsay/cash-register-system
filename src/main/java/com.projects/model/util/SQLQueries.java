package com.projects.model.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SQLQueries {
    private static final Logger logger = LoggerFactory.getLogger(SQLQueries.class);
    private Properties properties = new Properties();

    public static final String USER_QUERIES = "sql/queries/user-queries.properties";
    public static final String EMPLOYEE_QUERIES = "sql/queries/employee-queries.properties";
    public static final String PRODUCT_QUERIES = "sql/queries/product-queries.properties";
    public static final String CHECK_QUERIES = "sql/queries/check-queries.properties";
    public static final String REPORT_QUERIES = "sql/queries/report-queries.properties";

    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String DELETE = "delete";
    public static final String GET_BY_ID = "get.by.id";
    public static final String GET_ALL = "get.all";

    public static final String GET_BY_LOGIN = "get.by.login";
    public static final String GET_BY_TITLE = "get.by.title";
    public static final String GET_ALL_BY_TYPE = "get.all.by.type";
    public static final String GET_BY_CODE = "get.by.code";
    public static final String GET_BY_FIRST_AND_LAST_NAME = "get.by.first.and.last.name";
    public static final String GET_BY_CODE_MATCHING = "get.by.code.matching";
    public static final String GET_ALL_BY_DATE = "get.all.by.date";
    public static final String GET_ALL_BY_EMPLOYEE_ID = "get.all.by.employee.id";
    public static final String GET_ALL_BY_CHECK_ID = "get.all.by.check.id";
    public static final String GET_ALL_BY_REPORT_ID = "get.all.by.report.id";

    public static final String INSERT_INTO_CHECKS_PRODUCTS = "insert.into.checks.products";
    public static final String DELETE_FROM_CHECKS_PRODUCTS = "delete.from.checks.products";
    public static final String UPDATE_CHECKS_PRODUCTS = "update.checks.products";
    public static final String INSERT_INTO_REPORTS_CHECKS = "insert.into.reports.checks";
    public static final String DELETE_FROM_REPORTS_CHECKS = "delete.from.reports.checks";

    public static final String PAGINATION = "pagination";
    public static final String COUNT = "count";

    public SQLQueries(String queryUrl) {
        try (InputStream is = SQLQueries.class.getClassLoader().getResourceAsStream(queryUrl)) {
            properties.load(is);
        } catch (IOException e) {
            logger.error("failed to load properties", e);
        }
    }

    public String getQuery(String query) {
        return properties.getProperty(query);
    }
}
