package com.projects.model.service.impl;

import com.projects.model.dao.CheckDao;
import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.factory.impl.DaoFactoryImpl;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;
import com.projects.model.service.CheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CheckServiceImpl extends AbstractServiceImpl<CheckDao, Check, Long> implements CheckService {
    private static Logger logger = LoggerFactory.getLogger(CheckServiceImpl.class);

    public CheckServiceImpl(CheckDao dao) {
        super(dao);
    }

    @Override
    public List<Check> findAllByDate(String date) throws DaoException {
        List<Check> checksByDate = dao.getAllByDate(date);
        logger.info("retrieved checks by date");
        return checksByDate;
    }

    @Override
    public List<Check> findAllByReportId(long reportId) throws DaoException {
        List<Check> checksByReportId = dao.getAllByReportId(reportId);
        logger.info("retrieved check by product id");
        return checksByReportId;
    }

    @Override
    public boolean addProduct(long checkId, long productId, int boughtQuantity) throws DaoException {
        boolean productAdded = dao.attachProduct(checkId, productId, boughtQuantity);
        logger.info("added a product to the check");
        return productAdded;
    }

    @Override
    public boolean removeProduct(long checkId, long productId) throws DaoException {
        boolean productRemoved = dao.detachProduct(checkId, productId);
        logger.info("removed a product from the check");
        return productRemoved;
    }

    @Override
    public boolean updateProductQuantity(int boughtQuantity, long checkId, long productId) throws DaoException {
        boolean productUpdated = dao.updateProductQuantity(boughtQuantity, checkId, productId);
        logger.info("updated a product in the check");
        return productUpdated;
    }

    @Override
    public double countSum(List<Product> products) {
        double sum = 0.00;
        for (Product p : products) {
            sum += p.getPrice() * p.getBoughtQuantity();
            sum = (double) Math.round(sum * 100) / 100;
        }
        logger.info("counted sum of the products");
        return sum;
    }
}
