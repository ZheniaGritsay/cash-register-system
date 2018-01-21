package com.projects.model.dao.jdbc;

import com.projects.model.dao.ProductDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.QuantityType;
import com.projects.model.domain.dto.Product;
import com.projects.util.SQLQueries;
import org.postgresql.jdbc.PgBlob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.rowset.serial.SerialBlob;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcProductDaoImpl extends JdbcAbstractDaoImpl<Product, Long> implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(JdbcProductDaoImpl.class);

    @Override
    protected SQLQueries getSQLQueries() {
        return new SQLQueries(SQLQueries.PRODUCT_QUERIES);
    }

    @Override
    protected void preparedStatementForCreate(PreparedStatement pStatement, Product product) {
        try {
            pStatement.setString(1, product.getTitle());
            pStatement.setLong(2, product.getCode());
            pStatement.setDouble(3, product.getPrice());
            pStatement.setString(4, product.getQuantityType().name());
            pStatement.setInt(5, product.getQuantityOnStock());
            pStatement.setBytes(6, product.getImage());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for create: " + e.getMessage());
        }
    }

    @Override
    protected void preparedStatementForUpdate(PreparedStatement pStatement, Product product) {
        try {
            preparedStatementForCreate(pStatement, product);
            pStatement.setLong(7, product.getId());
        } catch (SQLException e) {
            logger.error("failed setting prepared statement for update: " + e.getMessage());
        }
    }

    @Override
    protected List<Product> parseResultSet(ResultSet resultSet) {
        List<Product> productList = new ArrayList<>();

        try {
            while (resultSet.next()) {
                Product product = new Product.Builder()
                        .id(resultSet.getLong("id"))
                        .title(resultSet.getString("title"))
                        .code(resultSet.getLong("code"))
                        .price(resultSet.getDouble("price"))
                        .quantityType(QuantityType.valueOf(resultSet.getString("quantity_type")))
                        .boughtQuantity(resultSet.getInt("bought_quantity"))
                        .quantityOnStock(resultSet.getInt("quantity_on_stock"))
                        .image(resultSet.getBytes("image"))
                        .build();

                productList.add(product);
            }
        } catch (SQLException e) {
            logger.error("failed parse result set: " + e.getMessage());
        }

        return productList;
    }

    @Override
    public List<Product> getByTitle(String title) throws DaoException {
        List<Product> products = preparedStatementExec(c -> {
            PreparedStatement pStatement = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_BY_TITLE));
            pStatement.setString(1, title + "%");
            return pStatement;
        });

        return products;
    }

    @Override
    public Product getByCode(Long code) throws DaoException {
        Product product = null;
        List<Product> list = preparedStatementExec(c -> {
            PreparedStatement pStatement = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_BY_CODE));
            pStatement.setLong(1, code);
            return pStatement;
        });

        if (!list.isEmpty()) {
            product = list.get(0);
        }

        return product;
    }

    @Override
    public List<Product> getByCodeMatching(Long code) throws DaoException {
        List<Product> products = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_BY_CODE_MATCHING));
            ps.setString(1, code + "%");
            return ps;
        });

        return products;
    }

    @Override
    public List<Product> getAllByCheckId(long checkId) throws DaoException {
        List<Product> products = preparedStatementExec(c -> {
            PreparedStatement ps = c.prepareStatement(getSQLQueries().getQuery(SQLQueries.GET_ALL_BY_CHECK_ID));
            ps.setLong(1, checkId);
            return ps;
        });

        return products;
    }
}
