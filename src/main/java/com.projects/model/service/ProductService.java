package com.projects.model.service;

import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductService extends AbstractService<Product, Long> {
    List<Product> findByTitle(String title) throws DaoException;

    Product findByCode(Long code) throws DaoException;

    List<Product> findByCodeMatching(Long code) throws DaoException;

    List<Product> findAllByCheckId(long checkId) throws DaoException;

    List<Product> checkImagePresence(HttpServletRequest req, Product... products);
}
