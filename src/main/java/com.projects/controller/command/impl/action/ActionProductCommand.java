package com.projects.controller.command.impl.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.projects.controller.util.PagesView;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.QuantityType;
import com.projects.model.domain.dto.Product;
import com.projects.model.service.AbstractService;
import com.projects.model.service.ProductService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ActionProductCommand extends AbstractActionCommand<Product> {
    private static final Logger logger = LoggerFactory.getLogger(ActionProductCommand.class);
    private final ProductService productService = ServiceFactoryImpl.getInstance().getProductService();

    @Override
    AbstractService getEntityService() {
        return productService;
    }

    @Override
    String getView() {
        return PagesView.EDIT_VIEW;
    }

    @Override
    String getEntityName() {
        return "product";
    }

    @Override
    String getEntitiesName() {
        return "products";
    }

    @Override
    int getDefaultRecordsPerPage() {
        return 3;
    }

    @Override
    Map<String, String> getEntityMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("title", Internationalization.getText("label.title"));
        messages.put("code", Internationalization.getText("label.code"));
        messages.put("price", Internationalization.getText("label.price"));
        messages.put("image", Internationalization.getText("label.image"));
        messages.put("quantityType", Internationalization.getText("label.quantity.type"));
        messages.put("quantity", Internationalization.getText("label.quantity"));
        messages.put("boughtQuantity", Internationalization.getText("label.bought.quantity"));
        messages.put("quantityOnStock", Internationalization.getText("label.quantity.on.stock"));
        messages.put("productSearch", Internationalization.getText("label.product.search"));
        messages.put("closeCheck", Internationalization.getText("label.close.check"));
        messages.put("sum", Internationalization.getText("label.sum"));
        messages.put("productUnavailable", Internationalization.getText("error.product.unavailable"));

        return messages;
    }

    @Override
    Product getEntityForUpdateCreate(HttpServletRequest request) {
        String productJson = JsonUtil.readFromRequest(request);
        JsonNode productTree = JsonUtil.getJsonTree(productJson);

        byte[] img = new byte[0];
        if (!productTree.path("image").textValue().trim().isEmpty()) {
            try {
                img = productTree.path("image").binaryValue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Product product = new Product.Builder()
                .id(productTree.path("id").longValue())
                .title(productTree.path("title").textValue())
                .code(productTree.path("code").longValue())
                .price(productTree.path("price").doubleValue())
                .quantityType(QuantityType.valueOf(productTree.path("quantityType").textValue()))
                .boughtQuantity(productTree.path("quantityType").intValue())
                .quantityOnStock(productTree.path("quantityOnStock").intValue())
                .image(img)
                .build();

        return product;

    }

    @Override
    protected String createAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Product product = getEntityForUpdateCreate(request);

        if (checkProductCodeExists(product, response) || checkForViolations(product, response)) {
            return "";
        }

        productService.create(product);

        return "";
    }

    @Override
    protected String updateAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Product product = getEntityForUpdateCreate(request);

        if (checkProductCodeExists(product, response) || checkForViolations(product, response)) {
            return "";
        }

        productService.update(product);

        return "";
    }

    @Override
    protected String getAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Product product = null;
        List<Product> productList = null;
        String path = request.getPathInfo();
        String param = getRequestParam(request);
        if (path.matches("^/" + getEntitiesName() + "/findByCode$"))
            productList = productService.findByCodeMatching(Long.parseLong(request.getParameter("code")));
        else if (path.matches("^/" + getEntitiesName() + "/findByTitle$"))
            productList = productService.findByTitle(request.getParameter("title"));
        else if (path.matches("^/" + getEntitiesName() + "/findByCheckId$"))
            productList = productService.findAllByCheckId(Long.parseLong(request.getParameter("checkId")));
        else if (path.matches("^/" + getEntitiesName() + "/\\d+"))
            product = productService.findById(Long.parseLong(param));
        else if (path.matches("^/" + getEntitiesName() + "/?$")) {
            if (request.getParameter("recordsPerPage") != null)
                return pagination(request, response);

            productList = productService.findAll();
        }

        String json;
        if (product != null) {
            product = productService.checkImagePresence(request, product).get(0);
            json = constructFullJson(product);
        } else {
            productList = productService.checkImagePresence(request, productList.toArray(new Product[productList.size()]));
            json = constructFullJson(productList.toArray(new Product[productList.size()]));
        }

        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(json);

        return "";
    }

    @Override
    protected String pagination(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        int page = 1;
        int recordsPerPage = Integer.parseInt(request.getParameter("recordsPerPage"));
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        List<Product> productList = productService.getForPage(recordsPerPage, (page - 1) * recordsPerPage);
        productList = productService.checkImagePresence(request, productList.toArray(new Product[productList.size()]));
        int noOfRecords = productService.getRecords();
        int noOfPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);

        // TODO: 1/18/2018  
        request.setAttribute(getEntitiesName(), productList);
        request.setAttribute("entity", getEntitiesName());
        request.setAttribute("numberOfPages", noOfPages);
        request.setAttribute("recordsPerPage", getDefaultRecordsPerPage());

        String commonJson = constructFullJson(productList.toArray(new Product[productList.size()]));

        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(commonJson);

        return "";
    }

    private boolean checkProductCodeExists(Product product, HttpServletResponse response) throws DaoException, IOException {
        Product p = productService.findByCode(product.getCode());
        if (p != null) {
            if (p.getId().equals(product.getId()))
                return false;

            String commonJson = constructFullJson(product);
            Map<String, String> errors = new HashMap<>();
            errors.put("codeExists", Internationalization.getText("error.code.exists"));
            String errorsJson = JsonUtil.createJson(errors);
            commonJson = JsonUtil.addToJson(commonJson, errorsJson, "errors");

            response.setHeader("Content-Type", "application/json");
            response.getWriter().write(commonJson);

            return true;
        } else {
            return false;
        }
    }
}