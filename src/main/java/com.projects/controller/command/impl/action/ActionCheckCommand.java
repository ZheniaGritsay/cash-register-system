package com.projects.controller.command.impl.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.CheckStatus;
import com.projects.model.domain.constant.QuantityType;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Employee;
import com.projects.model.domain.dto.Product;
import com.projects.model.holders.CheckHolder;
import com.projects.model.service.AbstractService;
import com.projects.model.service.CheckService;
import com.projects.model.service.ProductService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import com.projects.model.transaction.TransactionManager;
import com.projects.model.transaction.exception.TransactionException;
import com.projects.model.transaction.impl.TransactionManagerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionCheckCommand extends AbstractActionCommand<Check> {
    private final CheckService checkService = ServiceFactoryImpl.getInstance().getCheckService();
    private final ProductService productService = ServiceFactoryImpl.getInstance().getProductService();

    @Override
    AbstractService getEntityService() {
        return checkService;
    }

    @Override
    String getView() {
        return "edit-view";
    }

    @Override
    String getEntityName() {
        return "check";
    }

    @Override
    String getEntitiesName() {
        return "checks";
    }

    @Override
    int getDefaultRecordsPerPage() {
        return 5;
    }

    @Override
    Map<String, String> getEntityMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("employee", Internationalization.getText("label.employee"));
        messages.put("date", Internationalization.getText("label.date"));
        messages.put("status", Internationalization.getText("label.status"));
        messages.put("sum", Internationalization.getText("label.sum"));
        messages.put("checkSearch", Internationalization.getText("label.check.search"));
        messages.put("quantityToAdd", Internationalization.getText("label.quantity.to.add"));
        messages.put("quantityToRemove", Internationalization.getText("label.quantity.to.remove"));

        messages.put("title", Internationalization.getText("label.title"));
        messages.put("code", Internationalization.getText("label.code"));
        messages.put("price", Internationalization.getText("label.price"));
        messages.put("quantityType", Internationalization.getText("label.quantity.type"));
        messages.put("boughtQuantity", Internationalization.getText("label.bought.quantity"));
        messages.put("quantityOnStock", Internationalization.getText("label.quantity.on.stock"));
        messages.put("image", Internationalization.getText("label.image"));
        messages.put("productSearch", Internationalization.getText("label.product.search"));

        return messages;
    }

    @Override
    Check getEntityForUpdateCreate(HttpServletRequest request) {
        String checkJson = JsonUtil.readFromRequest(request);
        JsonNode checkTree = JsonUtil.getJsonTree(checkJson);
        ArrayNode productJsonArray = (ArrayNode) checkTree.path("products");
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < productJsonArray.size(); i++) {
            JsonNode productNode = productJsonArray.get(i);
            Product p = null;
            try {
                p = new Product.Builder()
                        .id(productNode.path("id").longValue())
                        .title(productNode.path("title").textValue())
                        .price(productNode.path("price").doubleValue())
                        .code(productNode.path("code").longValue())
                        .quantityType(QuantityType.valueOf(productNode.path("quantityType").textValue()))
                        .boughtQuantity(productNode.path("boughtQuantity").intValue())
                        .quantityOnStock(productNode.path("quantityOnStock").intValue())
                        .image(productNode.path("image").binaryValue())
                        .build();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            productList.add(p);
        }

        JsonNode employeeNode = checkTree.path("employee");
        Employee employee = new Employee.Builder()
                .id(employeeNode.path("id").longValue())
                .build();


        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");
        Check check = new Check.Builder()
                .id(checkTree.path("id").longValue())
                .employee(employee)
                .products(productList)
                .date(LocalDateTime.parse(checkTree.path("date").asText(), dtf))
                .sum(checkService.countSum(productList))
                .status(CheckStatus.valueOf(checkTree.path("status").asText()))
                .build();

        return check;
    }

    @Override
    protected String createAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Check check = getEntityForUpdateCreate(request);
        TransactionManager tm = TransactionManagerService.getTransactionManager();
        try {
            tm.begin();

            check = checkService.create(check);

            for (Product p : check.getProducts()) {
                productService.update(p);
                checkService.addProduct(check.getId(), p.getId(), p.getBoughtQuantity());
            }

        } catch (TransactionException ex) {
            try {
                tm.rollback();
            } catch (TransactionException e) {
                throw new DaoException(e.getMessage(), ex);
            }
            throw new DaoException(ex.getMessage(), ex);
        }

        return "";
    }

    @Override
    protected String updateAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Check newCheck = getEntityForUpdateCreate(request);

        if (checkForViolations(newCheck, response)) {
            return "";
        }

        Check currentCheck = checkService.findById(newCheck.getId());
        List<Product> currentCheckProducts = currentCheck.getProducts();
        List<Product> productsToUpdate = new ArrayList<>();

        TransactionManager tm = TransactionManagerService.getTransactionManager();
        try {
            tm.begin();

            for (Product product : newCheck.getProducts()) {
                boolean absentInCurrentCheck = currentCheckProducts.stream().noneMatch(p -> p.getId().equals(product.getId()));
                boolean quantityOnStockChanged = currentCheckProducts.stream().anyMatch(p -> p.getId().equals(product.getId()) &&
                        !(p.getQuantityOnStock().equals(product.getQuantityOnStock())));

                if (quantityOnStockChanged) {
                    productsToUpdate.add(product);
                }

                if (absentInCurrentCheck) {
                    productsToUpdate.add(product);
                    checkService.addProduct(newCheck.getId(), product.getId(), product.getBoughtQuantity());
                }

                if (quantityOnStockChanged && product.getBoughtQuantity() != 0) {
                    checkService.updateProductQuantity(product.getBoughtQuantity(), newCheck.getId(), product.getId());
                }

                if (product.getBoughtQuantity() == 0) {
                    checkService.removeProduct(newCheck.getId(), product.getId());
                }
            }

            for (Product p : productsToUpdate) {
                productService.update(p);
            }

            checkService.update(newCheck);

            tm.commit();
        } catch (TransactionException ex) {
            try {
                tm.rollback();
            } catch (TransactionException e) {
                throw new DaoException("", ex);
            }
            throw new DaoException("", ex);
        }

        return "";
    }

    @Override
    protected String getAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Check check = null;
        List<Check> checkList = null;
        String path = request.getPathInfo();
        if (path.matches("^/" + getEntitiesName() + "/findAllByReportId$"))
            checkList = checkService.findAllByReportId(Long.parseLong(request.getParameter("reportId")));
        else if (path.matches("^/" + getEntitiesName() + "/findAllByDate$")) {
            String date = request.getParameter("date");
            checkList = checkService.findAllByDate(date);
        } else if (path.matches("^/" + getEntitiesName() + "/\\d+$"))
            check = checkService.findById(Long.parseLong(getRequestParam(request)));
        else if (path.matches("^/" + getEntitiesName() + "/?$")) {
            return super.getAction(request, response);
        }

        String json;
        if (check != null) {
            check.getProducts();
            json = constructFullJson(check);
        } else {
            json = constructFullJson(checkList.toArray(new Check[checkList.size()]));
        }

        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(json);

        return "";
    }
}
