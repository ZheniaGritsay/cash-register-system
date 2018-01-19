package com.projects.controller.listener;

import com.projects.controller.command.manager.impl.CommandManagerImpl;
import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;

@WebListener
public class ApplicationContext implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationContext.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            DataBaseInitializer dbInitializer = new DataBaseInitializerImpl();
            dbInitializer.initializeDb();

            DataSource ds = ConnectionFactoryImpl.getInstance().init();
            ConnectionFactoryImpl.getInstance().setConnectionPool(ds);

            CommandManagerImpl.getInstance().initCommands();
        } catch (InitializationException | IOException ex) {
            logger.error("failed to start up the application: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        logger.info("application start up");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionFactoryImpl.getInstance().shutDown();
        logger.info("application shut down");
    }
}
