package com.projects.model.initialize;

import com.projects.model.dao.exception.InitializationException;

public interface DataBaseInitializer {
    void initializeDb() throws InitializationException;
}
