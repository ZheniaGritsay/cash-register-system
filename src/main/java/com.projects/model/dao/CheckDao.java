package com.projects.model.dao;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Check;

import java.util.List;

public interface CheckDao extends AbstractDao<Check, Long> {
    List<Check> getAllByDate(String date) throws DaoException;

    List<Check> getAllByReportId(long reportId) throws DaoException;
    
    List<Check> getAllByEmployeeId(long employeeId) throws DaoException;

    boolean detachProduct(long checkId, long productId) throws DaoException;

    boolean attachProduct(long checkId, long productId, int boughtQuantity) throws DaoException;

    boolean updateProductQuantity(int boughtQuantity, long checkId, long productId) throws DaoException;
}
