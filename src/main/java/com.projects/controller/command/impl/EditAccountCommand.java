package com.projects.controller.command.impl;

import com.projects.controller.command.Command;
import com.projects.controller.exception.InternalServerException;
import com.projects.controller.util.PagesView;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Employee;
import com.projects.model.domain.dto.User;
import com.projects.model.service.CheckService;
import com.projects.model.service.EmployeeService;
import com.projects.model.service.UserService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import com.projects.model.validation.Validator;
import com.projects.model.validation.ValidatorFactory;
import com.projects.model.validation.Violation;
import com.projects.model.validation.exception.ValidationException;
import com.projects.model.validation.impl.ValidatorFactoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

public class EditAccountCommand implements Command {
    private UserService userService = ServiceFactoryImpl.getInstance().getUserService();
    private EmployeeService employeeService = ServiceFactoryImpl.getInstance().getEmployeeService();
    private CheckService checkService = ServiceFactoryImpl.getInstance().getCheckService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException {
        try {
            Long userId = (Long) request.getSession().getAttribute("userId");
            User user = userService.findById(userId);
            List<Check> employeesChecks = checkService.findAllByEmployeeId(user.getEmployee().getId());

            if (request.getMethod().equals("GET")) {

                request.setAttribute("user", user);
                request.setAttribute("employee", user.getEmployee());

                request.setAttribute("employeesChecks", employeesChecks);
                request.setAttribute("excludeFields",
                        Arrays.asList("products", "productDao", "productList", "logger"));

                return PagesView.EDIT_PROFILE;
            }

            Employee employee = new Employee.Builder()
                    .id(user.getEmployee().getId())
                    .firstName(request.getParameter("firstName"))
                    .lastName(request.getParameter("lastName"))
                    .email(request.getParameter("email"))
                    .salary(user.getEmployee().getSalary())
                    .position(user.getEmployee().getPosition())
                    .build();

            Validator validator = ValidatorFactoryImpl.getInstance().getValidator();

            Set<Violation> employeeViolations;
            try {
                employeeViolations = validator.validate(employee);
            } catch (ValidationException e) {
                throw new InternalServerException(e);
            }

            if (!employeeViolations.isEmpty()) {
                employeeViolations.forEach(v -> request.setAttribute(v.getField() + "Error", v.getMessage()));
            } else {
                employeeService.update(employee);
                employee = employeeService.findById(employee.getId());
            }


            request.setAttribute("employee", employee);
            request.setAttribute("user", user);

            request.setAttribute("employeesChecks", employeesChecks);
            request.setAttribute("excludeFields",
                    Arrays.asList("products", "productDao", "productList", "logger"));
        } catch (DaoException e) {
            throw new InternalServerException(e);
        }

        return PagesView.EDIT_PROFILE;
    }
}
