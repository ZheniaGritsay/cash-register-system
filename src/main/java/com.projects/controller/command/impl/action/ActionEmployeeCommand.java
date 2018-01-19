package com.projects.controller.command.impl.action;

import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.Position;
import com.projects.model.domain.dto.Employee;
import com.projects.model.service.AbstractService;
import com.projects.model.service.EmployeeService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ActionEmployeeCommand extends AbstractActionCommand<Employee> {
    private final EmployeeService employeeService = ServiceFactoryImpl.getInstance().getEmployeeService();

    @Override
    AbstractService getEntityService() {
        return employeeService;
    }

    @Override
    String getView() {
        return "edit-view";
    }

    @Override
    String getEntityName() {
        return "employee";
    }

    @Override
    String getEntitiesName() {
        return "employees";
    }

    @Override
    int getDefaultRecordsPerPage() {
        return 5;
    }

    @Override
    Map<String, String> getEntityMessages() {
        return null;
    }

    @Override
    Employee getEntityForUpdateCreate(HttpServletRequest request) {
        String employeeJson = JsonUtil.readFromRequest(request);
        return JsonUtil.parseJson(employeeJson, Employee.class);
    }
}
