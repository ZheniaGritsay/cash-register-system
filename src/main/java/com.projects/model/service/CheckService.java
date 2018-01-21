package com.projects.model.service;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckService extends AbstractService<Check, Long> {
    List<Check> findAllByDate(String date) throws DaoException;

    List<Check> findAllByReportId(long reportId) throws DaoException;
    
    List<Check> findAllByEmployeeId(long employeeId) throws DaoException;

    boolean addProduct(long checkId, long productId, int boughtQuantity) throws DaoException;

    boolean removeProduct(long checkId, long productId) throws DaoException;

    boolean updateProductQuantity(int boughtQuantity, long checkId, long productId) throws DaoException;

    double countSum(List<Product> products);
}
