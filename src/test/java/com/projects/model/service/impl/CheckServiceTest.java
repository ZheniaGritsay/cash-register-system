package com.projects.model.service.impl;

import com.projects.helpers.Dummies;
import com.projects.model.dao.CheckDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;
import com.projects.model.service.CheckService;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class CheckServiceTest {
    private CheckDao checkDao = mock(CheckDao.class);
    private CheckService checkService = new CheckServiceImpl(checkDao);

    @Test
    public void create() throws DaoException {
        Check check = Dummies.getDummyCheck(0L, 1L);
        checkService.create(check);
        verify(checkDao, times(1)).create(check);
    }

    @Test
    public void findById() throws DaoException {
        checkService.findById(1L);
        verify(checkDao, times(1)).getById(1L);
    }

    @Test
    public void update() throws DaoException {
        Check check = Dummies.getDummyCheck(0L, 1L);
        checkService.update(check);
        verify(checkDao, times(1)).update(check);
    }

    @Test
    public void delete() throws DaoException {
        checkService.delete(2L);
        verify(checkDao, times(1)).delete(2L);
    }

    @Test
    public void findAll() throws DaoException {
        checkService.findAll();
        verify(checkDao, times(1)).getAll();
    }

    @Test
    public void getForPage() throws DaoException {
        checkService.getForPage(5, 0);
        verify(checkDao, times(1)).getPerPage(5, 0);
    }

    @Test
    public void getRecords() throws DaoException {
        checkService.getRecords();
        verify(checkDao, times(1)).getCount();
    }

    @Test
    public void findAllByDate() throws DaoException {
        checkService.findAllByDate("01-20-2018");
        verify(checkDao, times(1)).getAllByDate("01-20-2018");
    }

    @Test
    public void findAllByReportId() throws DaoException {
        checkService.findAllByReportId(5L);
        verify(checkDao, times(1)).getAllByReportId(5L);
    }

    @Test
    public void findAllByEmployeeId() throws DaoException {
        checkService.findAllByEmployeeId(3L);
        verify(checkDao, times(1)).getAllByEmployeeId(3L);
    }

    @Test
    public void addProduct() throws DaoException {
        checkService.addProduct(2L, 3L, 200);
        verify(checkDao, times(1)).attachProduct(2L, 3L, 200);
    }

    @Test
    public void removeProduct() throws DaoException {
        checkService.removeProduct(3L, 5L);
        verify(checkDao, times(1)).detachProduct(3L, 5L);
    }

    @Test
    public void updateProductQuantity() throws DaoException {
        checkService.updateProductQuantity(150, 4L, 1L );
        verify(checkDao, times(1)).updateProductQuantity(150, 4L, 1L);
    }

    @Test
    public void countSum() {
        List<Product> products = Arrays.asList(Dummies.getDummyProduct(1L), Dummies.getDummyProduct(2L),
                Dummies.getDummyProduct(3L));

        assertEquals(22.5, checkService.countSum(products), 0);
    }
}
