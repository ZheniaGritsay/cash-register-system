package com.projects;

import com.projects.model.connection.ConnectionFactory;
import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.CheckDao;
import com.projects.model.dao.PreparedStatementConfig;
import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.dao.factory.impl.DaoFactoryImpl;
import com.projects.model.domain.constant.QuantityType;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.model.validation.Validator;
import com.projects.model.validation.Violation;
import com.projects.model.validation.exception.ValidationException;
import com.projects.model.validation.impl.ValidatorFactoryImpl;
import org.apache.tomcat.jdbc.pool.DataSource;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws ValidationException, NoSuchMethodException, DaoException {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//        LocalDateTime time = LocalDateTime.parse("2017-12-27 17:50", dtf);
//        System.out.println(time);


    }
}
