package com.projects.controller.filter;


import com.projects.model.domain.constant.Position;
import com.projects.model.domain.constant.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        Role role = (Role) session.getAttribute("userRole");
        Position position = (Position) session.getAttribute("employeePosition");
        String path = req.getPathInfo();
        path = path == null ? "/" : path;
        String httpMethod = req.getMethod();

        boolean proceed = true;
        if (userId == null && path.matches("^/(home.*)|(edit-account)$")) {
            resp.sendError(401);
            proceed = false;
        } else {
            if (path.matches("^/products.*")) {
                if (!(proceed = verifyProductsAccess(httpMethod, position))) {
                    resp.sendError(403);
                }
            } else if (path.matches("^/checks.*")) {
                if (!(proceed = verifyChecksAccess(httpMethod, position))) {
                    resp.sendError(403);
                }
            } else if (path.matches("^/reports.*")) {
                if (!(proceed = verifyReportsAccess(position))) {
                    resp.sendError(403);
                }
            } else if (path.matches("^/employees.*")) {
                if (!(proceed = verifyEmployeesAccess(position))) {
                    resp.sendError(403);
                }
            } else if (path.matches("^/users.*")) {
                if (!(proceed = verifyUsersAccess(httpMethod, role))) {
                    resp.sendError(403);
                }
            }
        }

        if (proceed)
            chain.doFilter(request, response);
    }

    private boolean verifyProductsAccess(String httpMethod, Position position) {
        return position != null && (httpMethod.equals("GET") || position == Position.COMMODITIES_EXPERT);

    }

    private boolean verifyChecksAccess(String httpMethod, Position position) {
        if (httpMethod.matches("^(PUT)|(DELETE)$") && position != Position.SENIOR_CASHIER) {
            return false;
        }
        return !httpMethod.equals("POST") || position == Position.CASHIER || position == Position.SENIOR_CASHIER;
    }

    private boolean verifyReportsAccess(Position position) {
        return position == Position.SENIOR_CASHIER || position == Position.ADMINISTRATOR;
    }

    private boolean verifyEmployeesAccess(Position position) {
        return position == Position.ADMINISTRATOR;
    }

    private boolean verifyUsersAccess(String httpMethod, Role role) {
        return httpMethod.matches("^(GET)|(DELETE)$") && role == Role.ADMIN;
    }
}
