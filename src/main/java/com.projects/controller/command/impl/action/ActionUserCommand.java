package com.projects.controller.command.impl.action;

import com.projects.controller.util.PagesView;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.Role;
import com.projects.model.domain.dto.User;
import com.projects.model.service.AbstractService;
import com.projects.model.service.UserService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class ActionUserCommand extends AbstractActionCommand<User> {
    private final UserService userService = ServiceFactoryImpl.getInstance().getUserService();

    @Override
    AbstractService getEntityService() {
        return userService;
    }

    @Override
    String getView() {
        return PagesView.EDIT_VIEW;
    }

    @Override
    String getEntityName() {
        return "user";
    }

    @Override
    String getEntitiesName() {
        return "users";
    }

    @Override
    int getDefaultRecordsPerPage() {
        return 5;
    }

    @Override
    Map<String, String> getEntityMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("login", Internationalization.getText("label.login"));
        messages.put("role", Internationalization.getText("label.role"));
        messages.put("employee", Internationalization.getText("label.employee"));
        messages.put("firstName", Internationalization.getText("label.first.name"));
        messages.put("lastName", Internationalization.getText("label.last.name"));

        return messages;
    }

    @Override
    User getEntityForUpdateCreate(HttpServletRequest request) {
        return null;
    }
}
