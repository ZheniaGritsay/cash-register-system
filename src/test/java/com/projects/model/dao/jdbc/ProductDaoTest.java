package com.projects.model.dao.jdbc;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.domain.dto.Product;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.helpers.DropDataBase;
import com.projects.helpers.Dummies;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProductDaoTest {
    private ProductDao productDao = new JdbcProductDaoImpl();
    private List<Product> productList;

    @BeforeClass
    public static void setUpClass() throws InitializationException {
        DataBaseInitializer initializer = new DataBaseInitializerImpl();
        initializer.initializeDb();
        ConnectionFactoryImpl.getInstance().setConnectionPool(ConnectionFactoryImpl.getInstance().init());
    }

    @AfterClass
    public static void tearDownClass() {
        DropDataBase.dropDataBase();
    }

    @Test
    public void create() throws DaoException {
        long newId = productDao.create(Dummies.getDummyProduct(0L));
        Product product = productDao.getById(newId);

        assertTrue(newId > 0);
        assertEquals(newId, (long) product.getId());
        assertNotNull(product);
    }

    @Test
    public void update() throws DaoException {
        boolean updated = productDao.update(Dummies.getDummyProduct(1L));

        assertTrue(updated);
    }

    @Test
    public void getById() throws DaoException {
        Product product = productDao.getById(2L);

        assertEquals(2L, (long) product.getId());
        assertNotNull(product);
    }

    @Test
    public void getAll() throws DaoException {
        productList = productDao.getAll();

        assertNotNull(productList);
        assertTrue(productList.size() > 0);
    }

    @Test
    public void delete() throws DaoException {
        boolean deleted = productDao.delete(10L);

        assertTrue(deleted);
        assertNull(productDao.getById(10L));
    }

    @Test
    public void getPerPage() throws DaoException {
        productList = productDao.getPerPage(5, 0);

        assertNotNull(productList);
        assertEquals(5, productList.size());
    }

    @Test
    public void getCount() throws DaoException {
        int count = productDao.getCount();

        assertTrue(count > 0);
    }

    @Test
    public void getByTitle() throws DaoException {
        productList = productDao.getByTitle("Bread");

        assertNotNull(productList);
        assertEquals(1, productList.size());

        productList = productDao.getByTitle("C");

        assertNotNull(productList);
        assertTrue(productList.size() > 1);

        productList = productDao.getByTitle(null);

        assertTrue(productList.isEmpty());
    }

    @Test
    public void getByCode() throws DaoException {
        Product product = productDao.getByCode(46572L);

        assertEquals(46572L, (long) product.getCode());
        assertNotNull(product);
    }

    @Test
    public void getByCodeMatching() throws DaoException {
        productList = productDao.getByCodeMatching(978303L);

        assertNotNull(productList);
        assertEquals(1, productList.size());

        productList = productDao.getByCodeMatching(8L);

        assertNotNull(productList);
        assertTrue(productList.size() > 1);

        productList = productDao.getByCodeMatching(null);

        assertTrue(productList.isEmpty());
    }

    @Test
    public void getAllByCheckId() throws DaoException {
        productList = productDao.getByCodeMatching(1L);

        assertNotNull(productList);
        assertTrue(productList.size() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionCreate() throws DaoException {
        productDao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionUpdate() throws DaoException {
        productDao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetById() throws DaoException {
        productDao.getById(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionDelete() throws DaoException {
        productDao.delete(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetByCode() throws DaoException {
        productDao.getByCode(null);
    }
}
