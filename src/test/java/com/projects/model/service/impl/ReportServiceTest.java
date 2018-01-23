package com.projects.model.service.impl;

import com.projects.helpers.Dummies;
import com.projects.model.dao.ReportDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Report;
import com.projects.model.service.ReportService;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ReportServiceTest {
    private ReportDao reportDao = mock(ReportDao.class);
    private ReportService reportService = new ReportServiceImpl(reportDao);

    @Test
    public void create() throws DaoException {
        Report report = Dummies.getDummyReport(0L);
        reportService.create(report);
        verify(reportDao, times(1)).create(report);
    }

    @Test
    public void findById() throws DaoException {
        reportService.findById(1L);
        verify(reportDao, times(1)).getById(1L);
    }

    @Test
    public void update() throws DaoException {
        Report report = Dummies.getDummyReport(1L);
        reportService.update(report);
        verify(reportDao, times(1)).update(report);
    }

    @Test
    public void delete() throws DaoException {
        reportService.delete(2L);
        verify(reportDao, times(1)).delete(2L);
    }

    @Test
    public void findAll() throws DaoException {
        reportService.findAll();
        verify(reportDao, times(1)).getAll();
    }

    @Test
    public void getForPage() throws DaoException {
        reportService.getForPage(5, 0);
        verify(reportDao, times(1)).getPerPage(5, 0);
    }

    @Test
    public void getRecords() throws DaoException {
        reportService.getRecords();
        verify(reportDao, times(1)).getCount();
    }

    @Test
    public void findAllByType() throws DaoException {
        reportService.findAllByType(ReportType.MODIFIED_CHECKS);
        verify(reportDao, times(1)).getAllByType(ReportType.MODIFIED_CHECKS);
    }

    @Test
    public void addCheck() throws DaoException {
        reportService.addCheck(1L, 2L);
        verify(reportDao, times(1)).appendCheck(1L, 2L);
    }

    @Test
    public void removeCheck() throws DaoException {
        reportService.removeCheck(2L, 4L);
        verify(reportDao, times(1)).removeCheck(2L, 4l);
    }

    @Test
    public void countTotalSum() {
        List<Check> checks = Arrays.asList(Dummies.getDummyCheck(1L, 1L),
                Dummies.getDummyCheck(2L, 2L), Dummies.getDummyCheck(2L, 2L));

        assertEquals(1501.35, reportService.countTotalSum(checks), 0);
    }
}
