package com.projects.controller.command.impl.action;

import com.projects.controller.command.Command;
import com.projects.controller.exception.InternalServerException;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.Entity;
import com.projects.model.service.AbstractService;
import com.projects.model.validation.Validator;
import com.projects.model.validation.Violation;
import com.projects.model.validation.exception.ValidationException;
import com.projects.model.validation.impl.ValidatorFactoryImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractActionCommand<T extends Entity> implements Command {

    abstract AbstractService getEntityService();

    abstract String getView();

    abstract String getEntityName();

    abstract String getEntitiesName();

    abstract int getDefaultRecordsPerPage();

    abstract Map<String, String> getEntityMessages();

    abstract T getEntityForUpdateCreate(HttpServletRequest request);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException {
        String view;
        try {
            String path = request.getPathInfo();
            if (path.equals("/" + getEntitiesName() + "/view")) {
                request.setAttribute("entity", getEntitiesName());
                int noOfRecords = getEntityService().getRecords();
                Integer recordsPerPage;
                if (request.getParameter("recordsPerPage") != null) {
                    recordsPerPage = Integer.parseInt(request.getParameter("recordsPerPage"));
                } else {
                    recordsPerPage = getDefaultRecordsPerPage();
                }
                int numberOfPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);
                request.setAttribute("recordsPerPage", recordsPerPage);
                request.setAttribute("numberOfPages", numberOfPages);
                return getView();
            } else if (path.equals("/" + getEntitiesName() + "/messages")) {
                Map<String, String> messages = getEntityMessages();
                messages.putAll(getCommonMessages());
                String jsonMessages = JsonUtil.createJson(messages);
                response.setHeader("Content-Type", "application/json");
                response.getWriter().write(jsonMessages);
                return "";
            }

            String method = request.getMethod();
            switch (method) {
                case "GET":
                    view = getAction(request, response);
                    break;
                case "POST":
                    view = createAction(request, response);
                    break;
                case "PUT":
                    view = updateAction(request, response);
                    break;
                case "DELETE":
                    view = deleteAction(request);
                    break;
                default:
                    view = "405";
                    break;
            }
        } catch (DaoException | IOException e) {
            throw new InternalServerException(e.getMessage(), e);
        }

        return view;
    }

    protected String createAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        T entity = getEntityForUpdateCreate(request);
        if (!checkForViolations(entity, response)) {
            getEntityService().create(entity);
        }
        return "";
    }

    protected String updateAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        T entity = getEntityForUpdateCreate(request);
        if (!checkForViolations(entity, response)) {
            getEntityService().update(entity);
        }
        return "";
    }

    protected String deleteAction(HttpServletRequest request) throws DaoException {
        String id = getRequestParam(request);
        getEntityService().delete(Long.parseLong(id));
        return "";
    }

    protected String getAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        List<Entity> entityList = null;
        Entity entity = null;
        String path = request.getPathInfo();
        if (path.matches("^/" + getEntitiesName() + "/\\d+$")) {
            String id = getRequestParam(request);
            entity = getEntityService().findById(Long.parseLong(id));
        } else if (path.matches("^/" + getEntitiesName() + "/?$")) {
            if (request.getParameter("recordsPerPage") != null)
                return pagination(request, response);

            entityList = getEntityService().findAll();
        }

        String json;
        if (entity != null)
            json = constructFullJson(entity);
        else
            json = constructFullJson(entityList.toArray(new Entity[entityList.size()]));

        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(json);

        return "";
    }

    protected String pagination(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        int page = 1;
        int recordsPerPage = Integer.parseInt(request.getParameter("recordsPerPage"));
        if (request.getParameter("page") != null)
            page = Integer.parseInt(request.getParameter("page"));
        List<T> list = getEntityService().getForPage(recordsPerPage, (page - 1) * recordsPerPage);
        int noOfRecords = getEntityService().getRecords();
        int noOfPages = (int) Math.ceil((double) noOfRecords / recordsPerPage);

        request.setAttribute(getEntitiesName(), list);
        request.setAttribute("entity", getEntitiesName());
        request.setAttribute("numberOfPages", noOfPages);
        request.setAttribute("recordsPerPage", getDefaultRecordsPerPage());


        String commonJson = constructFullJson(list.toArray(new Entity[list.size()]));

        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(commonJson);

        return "";
    }

    protected Map<String, String> getCommonMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("save", Internationalization.getText("button.save"));
        messages.put("add", Internationalization.getText("button.add"));
        messages.put("edit", Internationalization.getText("button.edit"));
        messages.put("delete", Internationalization.getText("button.delete"));
        messages.put("recordsPerPage", Internationalization.getText("label.records.per.page"));
        messages.put("close", Internationalization.getText("label.close"));
        messages.put("closeCheck", Internationalization.getText("label.close.check"));
        messages.put("noResults", Internationalization.getText("label.no.results"));

        return messages;
    }

    protected String getRequestParam(HttpServletRequest request) {
        int islash = request.getPathInfo().lastIndexOf("/");
        return request.getPathInfo().substring(islash + 1);
    }

    protected String constructFullJson(Entity... entities) {
        Map<String, String> messages = getEntityMessages();
        messages.putAll(getCommonMessages());
        String entitiesJson;
        String entitiesJsonObjectName;
        if (entities.length == 1) {
            entitiesJson = JsonUtil.createJson(entities[0]);
            entitiesJsonObjectName = getEntityName();
        } else {
            entitiesJson = JsonUtil.createJson(entities);
            entitiesJsonObjectName = getEntitiesName();
        }

        String messagesJson = JsonUtil.createJson(messages);

        return JsonUtil.joinJson(entitiesJson, entitiesJsonObjectName, messagesJson, "messages");
    }

    protected boolean checkForViolations(T entity, HttpServletResponse response) throws IOException {
        Validator validator = ValidatorFactoryImpl.getInstance().getValidator();
        Set<Violation> entityViolations = null;
        try {
            entityViolations = validator.validate(entity);
        } catch (ValidationException ex) {

        }

        if (!entityViolations.isEmpty()) {
            String commonJson = constructFullJson(entity);
            Map<String, String> errors = new HashMap<>();
            entityViolations.forEach(v -> errors.put(v.getField(), Internationalization.getText(v.getMessage())));
            String errorsJson = JsonUtil.createJson(errors);
            commonJson = JsonUtil.addToJson(commonJson, errorsJson, "errors");

            response.setHeader("Content-Type", "application/json");
            response.getWriter().write(commonJson);
            return true;
        }

        return false;
    }
}
