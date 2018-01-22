package com.projects.model.service.impl;

import com.projects.model.dao.CheckDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;
import com.projects.model.service.CheckService;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.List;

public class CheckServiceTest {
    private CheckDao checkDao = mock(CheckDao.class);
    private CheckService checkService = new CheckServiceImpl(checkDao);

    @Test
    public void create() throws DaoException {

    }

    public void findById() throws DaoException {

    }

    public void update() throws DaoException {

    }

    public void delete() throws DaoException {

    }

    public void findAll() throws DaoException {

    }

    public void getForPage() throws DaoException {

    }

    public void getRecords() throws DaoException {

    }

    public void findAllByDate(String date) throws DaoException {

    }

    public void findAllByReportId(long reportId) throws DaoException {

    }

    public void findAllByEmployeeId(long employeeId) throws DaoException {

    }

    public void addProduct(long checkId, long productId, int boughtQuantity) throws DaoException {

    }

    public void removeProduct(long checkId, long productId) throws DaoException {

    }

    public void updateProductQuantity(int boughtQuantity, long checkId, long productId) throws DaoException {

    }

    public void countSum(List<Product> products) {

    }
}
