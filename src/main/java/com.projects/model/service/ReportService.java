package com.projects.model.service;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Report;

import java.util.List;

public interface ReportService extends AbstractService<Report, Long> {
    List<Report> findAllByType(ReportType type) throws DaoException;

    boolean addCheck(long reportId, long checkId) throws DaoException;

    boolean removeCheck(long reportId, long checkId) throws DaoException;

    double countTotalSum(List<Check> checks);
}
