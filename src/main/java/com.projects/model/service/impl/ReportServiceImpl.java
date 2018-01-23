package com.projects.model.service.impl;

import com.projects.model.dao.ReportDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;
import com.projects.model.domain.dto.Report;
import com.projects.model.service.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ReportServiceImpl extends AbstractServiceImpl<ReportDao, Report, Long> implements ReportService {
    private static Logger logger = LoggerFactory.getLogger(ReportServiceImpl.class);

    public ReportServiceImpl(ReportDao dao) {
        super(dao);
    }

    @Override
    public boolean update(Report entity) throws DaoException {
        return super.update(entity);
    }


    @Override
    public List<Report> findAllByType(ReportType type) throws DaoException {
        List<Report> reportsByType = dao.getAllByType(type);
        logger.info("retrieved reports by type");
        return reportsByType;
    }

    @Override
    public boolean addCheck(long reportId, long checkId) throws DaoException {
        boolean added = dao.appendCheck(reportId, checkId);
        logger.info("a check added to the report");
        return added;
    }

    @Override
    public boolean removeCheck(long reportId, long checkId) throws DaoException {
        boolean removed = dao.removeCheck(reportId, checkId);
        logger.info("a check removed from the report");
        return removed;
    }

    @Override
    public double countTotalSum(List<Check> checks) {
        double totalSum = checks.stream().mapToDouble(Check::getSum).sum();
        logger.info("counted total sum of the checks");
        return totalSum;
    }
}
