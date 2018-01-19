package com.projects.model.dao;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Product;

import java.util.List;

public interface ProductDao extends AbstractDao<Product, Long> {
    List<Product> getByTitle(String title) throws DaoException;

    Product getByCode(Long code) throws DaoException;

    List<Product> getByCodeMatching(Long code) throws DaoException;

    List<Product> getAllByCheckId(long checkId) throws DaoException;
}
