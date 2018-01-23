package com.projects.model.service.impl;

import com.projects.helpers.Dummies;
import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Product;
import com.projects.model.service.ProductService;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ProductServiceTest {
    private ProductDao productDao = mock(ProductDao.class);
    private ProductService productService = new ProductServiceImpl(productDao);

    @Test
    public void create() throws DaoException {
        Product product = Dummies.getDummyProduct(0L);
        productService.create(product);
        verify(productDao, times(1)).create(product);
    }

    @Test
    public void findById() throws DaoException {
        productService.findById(1L);
        verify(productDao, times(1)).getById(1L);
    }

    @Test
    public void update() throws DaoException {
        Product product = Dummies.getDummyProduct(0L);
        productService.update(product);
        verify(productDao, times(1)).update(product);
    }

    @Test
    public void delete() throws DaoException {
        productService.delete(2L);
        verify(productDao, times(1)).delete(2L);
    }

    @Test
    public void findAll() throws DaoException {
        productService.findAll();
        verify(productDao, times(1)).getAll();
    }

    @Test
    public void getForPage() throws DaoException {
        productService.getForPage(5, 0);
        verify(productDao, times(1)).getPerPage(5, 0);
    }

    @Test
    public void getRecords() throws DaoException {
        productService.getRecords();
        verify(productDao, times(1)).getCount();
    }

    @Test
    public void findByTitle() throws DaoException {
        productService.findByTitle("Beef");
        verify(productDao, times(1)).getByTitle("Beef");
    }

    @Test
    public void findByCode() throws DaoException {
        productService.findByCode(67740L);
        verify(productDao, times(1)).getByCode(67740L);
    }

    @Test
    public void findByCodeMatching() throws DaoException {
        productService.findByCodeMatching(8674L);
        verify(productDao, times(1)).getByCodeMatching(8674L);
    }

    @Test
    public void findAllByCheckId() throws DaoException {
        productService.findAllByCheckId(5L);
        verify(productDao, times(1)).getAllByCheckId(5L);
    }
}
