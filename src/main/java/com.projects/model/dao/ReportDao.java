package com.projects.model.dao;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Report;

import java.util.List;

public interface ReportDao extends AbstractDao<Report, Long> {
    List<Report> getAllByType(ReportType type) throws DaoException;

    boolean appendCheck(long reportId, long checkId) throws DaoException;

    boolean removeCheck(long reportId, long checkId) throws DaoException;
}
