package com.projects.model.holders;

import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.factory.impl.DaoFactoryImpl;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class CheckHolder extends Check {
    private static final Logger logger = LoggerFactory.getLogger(CheckHolder.class);
    private ProductDao productDao;
    private List<Product> productList;

    public CheckHolder(Builder builder) {
        super(builder);
        this.productDao = DaoFactoryImpl.getInstance().getProductDao();
        this.productList = new ArrayList<>();
    }

    public static class BuilderHolder extends Builder {

        @Override
        public Check build() {
            return new CheckHolder(this);
        }
    }

    @Override
    public List<Product> getProducts() {
        if (super.getProducts() == null || super.getProducts().isEmpty()) {
            if (productList.isEmpty()) {
                try {
                    productList = productDao.getAllByCheckId(getId());
                } catch (DaoException e) {
                    logger.error("failed to get check's products: " + e.getMessage());
                }
            }
            return productList;
        }
        return super.getProducts();
    }
}
