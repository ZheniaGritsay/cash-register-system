package com.projects.controller.command.impl.action;

import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.Role;
import com.projects.model.domain.dto.User;
import com.projects.model.service.AbstractService;
import com.projects.model.service.UserService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ActionUserCommand extends AbstractActionCommand<User> {
    private final UserService userService = ServiceFactoryImpl.getInstance().getUserService();

    @Override
    AbstractService getEntityService() {
        return userService;
    }

    @Override
    String getView() {
        return "edit-view";
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
        return null;
    }

    @Override
    User getEntityForUpdateCreate(HttpServletRequest request) {
        String userJson = JsonUtil.readFromRequest(request);
        return JsonUtil.parseJson(userJson, User.class);
    }
}
