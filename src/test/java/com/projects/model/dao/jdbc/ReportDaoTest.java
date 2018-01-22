package com.projects.model.dao.jdbc;

import com.projects.model.connection.impl.ConnectionFactoryImpl;
import com.projects.model.dao.ReportDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.exception.InitializationException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Report;
import com.projects.model.initialize.DataBaseInitializer;
import com.projects.model.initialize.DataBaseInitializerImpl;
import com.projects.helpers.DropDataBase;
import com.projects.helpers.Dummies;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ReportDaoTest {
    private ReportDao reportDao = new JdbcReportDaoImpl();

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
        long newId = reportDao.create(Dummies.getDummyReport(0L));
        Report report = reportDao.getById(newId);

        assertTrue(newId > 0);
        assertEquals(newId, (long) report.getId());
        assertNotNull(report);
    }

    @Test
    public void update() throws DaoException {
        boolean updated = reportDao.update(Dummies.getDummyReport(1L));

        assertTrue(updated);
    }

    @Test
    public void getById() throws DaoException {
        Report report = reportDao.getById(2L);

        assertEquals(2L, (long) report.getId());
        assertNotNull(report);
    }

    @Test
    public void getAll() throws DaoException {
        List<Report> reportList = reportDao.getAll();

        assertNotNull(reportList);
        assertTrue(reportList.size() > 0);
    }

    @Test
    public void getPerPage() throws DaoException {
        List<Report> reportList = reportDao.getPerPage(5, 0);

        assertNotNull(reportList);
        assertEquals(5, reportList.size());
    }

    @Test
    public void getCount() throws DaoException {
        int count = reportDao.getCount();

        assertTrue(count > 0);
    }

    @Test
    public void getAllByType() throws DaoException {
        List<Report> reportList = reportDao.getAllByType(ReportType.CLOSED_CHECKS);

        assertNotNull(reportList);
        assertTrue(reportList.size() > 0);

        reportList.forEach(r -> assertEquals(ReportType.CLOSED_CHECKS, r.getType()));
    }

    @Test
    public void appendCheck() throws DaoException {
        boolean appended = reportDao.appendCheck(1L, 5L);

        assertTrue(appended);
    }

    @Test
    public void removeCheck() throws DaoException {
        boolean removed = reportDao.removeCheck(2L, 3L);

        assertTrue(removed);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionCreate() throws DaoException {
        reportDao.create(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionUpdate() throws DaoException {
        reportDao.update(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetById() throws DaoException {
        reportDao.getById(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionDelete() throws DaoException {
        reportDao.delete(null);
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionGetAllByType() throws DaoException {
        reportDao.getAllByType(null);
    }
}
