package com.projects.controller.filter;


import com.projects.model.domain.constant.Position;
import com.projects.model.domain.constant.Role;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@WebFilter("/*")
public class AccessFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        Role role = (Role) session.getAttribute("role");
        Position position = (Position) session.getAttribute("employeePosition");
        String path = req.getPathInfo();
        path = path == null ? "/" : path;
        String httpMethod = req.getMethod();


        if (userId == null && !path.matches("^(/home/?)|(/)$")) {
            request.getRequestDispatcher("/WEB-INF/jsp/401.jsp").forward(req, resp);
            resp.sendError(401);
        } else if (path.matches("^/products.*")) {
            if (!httpMethod.equals("GET") && position == Position.CASHIER || position == Position.SENIOR_CASHIER) {
                request.getRequestDispatcher("/WEB-INF/jsp/401.jsp").forward(req, resp);
                resp.sendError(403);
            }
        } else if (path.matches("^/checks.*")) {
            if (!httpMethod.equals("GET") && position == Position.COMMODITIES_EXPERT) {
                request.getRequestDispatcher("/WEB-INF/jsp/401.jsp").forward(req, resp);
                resp.sendError(403);
            }
            if (httpMethod.equals("PUT") || httpMethod.equals("DELETE") && position != Position.SENIOR_CASHIER) {
                request.getRequestDispatcher("/WEB-INF/jsp/401.jsp").forward(req, resp);
                resp.sendError(403);
            }

        } else if (path.matches("^/reports.*")) {
            if (position != Position.SENIOR_CASHIER) {
                request.getRequestDispatcher("/WEB-INF/jsp/401.jsp").forward(req, resp);
                resp.sendError(403);
            }
        } else if (path.matches("^/employees.*") || path.matches("^/users.*")) {
            if (position != Position.SENIOR_CASHIER) {
                request.getRequestDispatcher("/WEB-INF/jsp/401.jsp").forward(req, resp);
                resp.sendError(403);
            }
        } else {
            chain.doFilter(request, response);
        }
    }
}
