package com.projects.controller.command.impl;

import com.projects.controller.command.Command;
import com.projects.controller.exception.InternalServerException;
import com.projects.controller.util.PagesView;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.User;
import com.projects.model.service.UserService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCommand implements Command {
    private Logger logger = LoggerFactory.getLogger(LoginCommand.class);
    private UserService userService = ServiceFactoryImpl.getInstance().getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException {
        if ("GET".equals(request.getMethod())) {
            return PagesView.LOGIN;
        }
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        try {
            User user = userService.authentication(login, password);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("userRole", user.getRole());
                session.setAttribute("employeePosition", user.getEmployee().getPosition());
            } else {
                request.setAttribute("error", "error.login.or.password");
                return PagesView.LOGIN;
            }
        } catch (DaoException e) {
            logger.error("", e);
            throw new InternalServerException();
        }
        request.setAttribute("redirect", "true");
        return PagesView.LOGIN;
    }
}
