package com.projects.model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface PreparedStatementConfig {

    PreparedStatement configure(Connection connection) throws SQLException;
}
