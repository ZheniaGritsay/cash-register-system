package com.projects.controller.command.impl;

import com.projects.controller.command.Command;
import com.projects.controller.exception.InternalServerException;
import com.projects.controller.util.PagesView;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Product;
import com.projects.model.domain.dto.User;
import com.projects.model.service.ProductService;
import com.projects.model.service.UserService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CommonCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(CommonCommand.class);
    private UserService userService = ServiceFactoryImpl.getInstance().getUserService();
    private ProductService productService = ServiceFactoryImpl.getInstance().getProductService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException {
        String view;
        try {
            if (request.getPathInfo().equals("/home/control-panel")) {
                view = PagesView.CONTROL_PANEL;
            } else {
                String recordsPerPage = request.getParameter("recordsPerPage");
                String page = request.getParameter("page");
                List<Product> products;
                if (recordsPerPage != null && page != null) {
                    products = productService.getForPage(Integer.parseInt(recordsPerPage), Integer.parseInt(page));
                } else {
                    recordsPerPage = "6";
                    products = productService.getForPage(6, 0);
                }
                int noOfRecords = productService.getRecords();
                int numberOfPages = (int) Math.ceil((double) noOfRecords / Integer.parseInt(recordsPerPage));
                products = productService.checkImagePresence(request, products.toArray(new Product[products.size()]));
                Map<String, String> imgs = new HashMap<>();
                for (Product p : products) {
                    String imgBase64 = convertToBase64(p.getImage());
                    imgs.put(p.getTitle(), imgBase64);
                }

                Long userId = (Long) request.getSession().getAttribute("userId");
                if (userId != null) {
                    User user = userService.findById(userId);
                    request.setAttribute("employeeId", user.getEmployee().getId());
                    request.setAttribute("employeeFirstName", user.getEmployee().getFirstName());
                    request.setAttribute("employeeLastName", user.getEmployee().getLastName());
                }
                request.setAttribute("numberOfPages", numberOfPages);
                request.setAttribute("recordsPerPage", 6);
                request.setAttribute("products", products);
                request.setAttribute("images", imgs);
                request.setAttribute("closeCheck", Internationalization.getText("label.close.check"));
                request.setAttribute("sum", Internationalization.getText("label.sum"));
                request.setAttribute("entity", "products");
                view = PagesView.HOME;
            }
        } catch (DaoException ex) {
            throw new InternalServerException(ex.getMessage());
        }

        return view;
    }

    private String convertToBase64(byte[] image) {
        return Base64.getEncoder().encodeToString(image);
    }
}
