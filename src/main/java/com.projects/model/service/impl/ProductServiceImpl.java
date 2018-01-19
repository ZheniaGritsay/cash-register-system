package com.projects.model.service.impl;

import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Product;
import com.projects.model.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductServiceImpl extends AbstractServiceImpl<ProductDao, Product, Long> implements ProductService {
    private static Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductDao dao) {
        super(dao);
    }

    @Override
    public List<Product> findByTitle(String title) throws DaoException {
        List<Product> productsByTitle = dao.getByTitle(title);
        logger.info("retrieved a product by title");
        return productsByTitle;
    }

    @Override
    public Product findByCode(Long code) throws DaoException {
        Product productByCode = dao.getByCode(code);
        logger.info("retrieved a product by code");
        return productByCode;
    }

    @Override
    public List<Product> findByCodeMatching(Long code) throws DaoException {
        List<Product> productsByCodeMatching = dao.getByCodeMatching(code);
        logger.info("retrieved products by code matching");
        return productsByCodeMatching;
    }

    @Override
    public List<Product> findAllByCheckId(long checkId) throws DaoException {
        List<Product> productsByCheckId = dao.getAllByCheckId(checkId);
        logger.info("retrieved products by check id");
        return productsByCheckId;
    }

    @Override
    public List<Product> checkImagePresence(HttpServletRequest req, Product... products) {
        List<Product> result = new ArrayList<>();
        Arrays.stream(products).forEach(product -> {
            if (product.getImage().length == 0) {
                String emptyImagePath = req.getServletContext().getRealPath("/");
                emptyImagePath += "/resources/image/noimg.jpg";
                byte[] emptyImageBytes = null;
                try (FileInputStream fis = new FileInputStream(emptyImagePath)) {

                    byte[] bytes = new byte[(int) fis.getChannel().size()];
                    while (fis.read(bytes) != -1) ;

                    emptyImageBytes = bytes;

                } catch (IOException e) {
                    logger.error("failed to load empty image", e);
                }

                Product p = new Product.Builder()
                        .id(product.getId())
                        .title(product.getTitle())
                        .code(product.getCode())
                        .price(product.getPrice())
                        .quantityType(product.getQuantityType())
                        .quantityOnStock(product.getQuantityOnStock())
                        .boughtQuantity(product.getBoughtQuantity())
                        .image(emptyImageBytes)
                        .build();

                result.add(p);
            } else {
                result.add(product);
            }
        });

        return result;
    }
}
