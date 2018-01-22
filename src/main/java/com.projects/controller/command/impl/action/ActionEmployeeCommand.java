package com.projects.controller.command.impl.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.projects.controller.util.PagesView;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.controller.util.json.JsonUtil;
import com.projects.model.domain.constant.Position;
import com.projects.model.domain.dto.Employee;
import com.projects.model.service.AbstractService;
import com.projects.model.service.EmployeeService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionEmployeeCommand extends AbstractActionCommand<Employee> {
    private final EmployeeService employeeService = ServiceFactoryImpl.getInstance().getEmployeeService();

    @Override
    AbstractService getEntityService() {
        return employeeService;
    }

    @Override
    String getView() {
        return PagesView.EDIT_VIEW;
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
        Map<String, String> messages = new HashMap<>();
        messages.put("firstName", Internationalization.getText("label.first.name"));
        messages.put("lastName", Internationalization.getText("label.last.name"));
        messages.put("email", Internationalization.getText("label.email"));
        messages.put("salary", Internationalization.getText("label.salary"));
        messages.put("position", Internationalization.getText("label.position"));

        return messages;
    }

    @Override
    Employee getEntityForUpdateCreate(HttpServletRequest request) {
        String employeeJson = JsonUtil.readFromRequest(request);
        JsonNode employeeTree = JsonUtil.getJsonTree(employeeJson);

        Employee employee = new Employee.Builder()
                .id(employeeTree.path("id").longValue())
                .firstName(employeeTree.path("firstName").textValue())
                .lastName(employeeTree.path("lastName").textValue())
                .email(employeeTree.path("email").textValue())
                .salary(employeeTree.path("salary").doubleValue())
                .position(Position.valueOf(employeeTree.path("position").textValue()))
                .build();

        return employee;
    }
}
