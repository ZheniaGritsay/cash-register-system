package com.projects.controller.command.impl;

import com.projects.controller.command.Command;
import com.projects.controller.exception.InternalServerException;
import com.projects.controller.util.PagesView;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.Position;
import com.projects.model.domain.constant.Role;
import com.projects.model.domain.dto.Employee;
import com.projects.model.domain.dto.User;
import com.projects.model.service.EmployeeService;
import com.projects.model.service.UserService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import com.projects.model.transaction.TransactionManager;
import com.projects.model.transaction.exception.TransactionException;
import com.projects.model.transaction.impl.TransactionManagerService;
import com.projects.model.validation.Validator;
import com.projects.model.validation.Violation;
import com.projects.model.validation.exception.ValidationException;
import com.projects.model.validation.impl.ValidatorFactoryImpl;
import com.projects.model.util.Encryptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Stream;

public class RegistrationCommand implements Command {
    private final Logger logger = LogManager.getLogger();
    private final UserService userService = ServiceFactoryImpl.getInstance().getUserService();
    private final EmployeeService employeeService = ServiceFactoryImpl.getInstance().getEmployeeService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException {
        if ("GET".equals(request.getMethod())) {
            return PagesView.REGISTRATION;
        }

        String login = request.getParameter("login");
        String password = request.getParameter("password").trim();
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");

        try {
            if (userService.findByLogin(login) != null) {
                request.setAttribute("password", password);
                request.setAttribute("firstName", firstName);
                request.setAttribute("lastName", lastName);
                request.setAttribute("email", email);
                request.setAttribute("loginExists", true);

                return PagesView.REGISTRATION;
            }
        } catch (DaoException e) {
            throw new InternalServerException();
        }

        Employee employee = new Employee.Builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .salary(0.00)
                .position(Position.CASHIER)
                .build();

        User user = new User(0L, login, password, Role.USER, employee);

        Validator validator = ValidatorFactoryImpl.getInstance().getValidator();
        Set<Violation> employeeViolations;
        Set<Violation> userViolations;
        try {
            employeeViolations = validator.validate(employee);
            userViolations = validator.validate(user);
        } catch (ValidationException e) {
            logger.error(e);
            throw new InternalServerException();
        }

        if (!employeeViolations.isEmpty() || !userViolations.isEmpty()) {
            employeeViolations.forEach(v -> request.setAttribute(v.getField() + "Error", v.getMessage()));
            userViolations.forEach(v -> request.setAttribute(v.getField() + "Error", v.getMessage()));
            request.setAttribute("errors", true);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("login", login);
            userInfo.put("password", password);
            userInfo.put("firstName", firstName);
            userInfo.put("lastName", lastName);
            userInfo.put("email", email);

            Stream.concat(userViolations.stream(), employeeViolations.stream())
                    .map(Violation::getField).forEach(field -> {
                        if (userInfo.containsKey(field)) {
                            userInfo.remove(field);
                        }
                    });

            userInfo.forEach(request::setAttribute);

            return PagesView.REGISTRATION;
        }

        TransactionManager tm = TransactionManagerService.getTransactionManager();
        try {
            tm.begin();
            employee = employeeService.create(employee);
            String encryptedPass = Encryptor.encrypt(user.getPassword(), Encryptor.SHA256);
            user = new User(user.getId(), user.getLogin(), encryptedPass, user.getRole(), employee);
            userService.create(user);
            tm.commit();
        } catch (DaoException | TransactionException e) {
            try {
                tm.rollback();
            } catch (TransactionException ex) {
                logger.error(ex);
                throw new InternalServerException();
            }
            logger.error(e);
            throw new InternalServerException();
        }

        request.setAttribute("redirect", "true");

        return PagesView.REGISTRATION;
    }
}
