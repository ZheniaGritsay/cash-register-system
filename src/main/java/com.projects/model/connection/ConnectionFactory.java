package com.projects.model.connection;

import java.sql.Connection;

public interface ConnectionFactory {
    Connection getConnection();
}
