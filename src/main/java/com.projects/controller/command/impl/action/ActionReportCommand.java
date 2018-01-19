package com.projects.controller.command.impl.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.projects.controller.util.i18n.Internationalization;
import com.projects.controller.util.json.JsonUtil;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Report;
import com.projects.model.service.AbstractService;
import com.projects.model.service.ReportService;
import com.projects.model.service.factory.impl.ServiceFactoryImpl;
import com.projects.model.transaction.TransactionManager;
import com.projects.model.transaction.exception.TransactionException;
import com.projects.model.transaction.impl.TransactionManagerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ActionReportCommand extends AbstractActionCommand<Report> {
    private final ReportService reportService = ServiceFactoryImpl.getInstance().getReportService();

    @Override
    AbstractService getEntityService() {
        return reportService;
    }

    @Override
    String getView() {
        return "edit-view";
    }

    @Override
    String getEntityName() {
        return "report";
    }

    @Override
    String getEntitiesName() {
        return "reports";
    }

    @Override
    int getDefaultRecordsPerPage() {
        return 5;
    }

    @Override
    Map<String, String> getEntityMessages() {
        Map<String, String> messages = new HashMap<>();
        messages.put("sinceDate", Internationalization.getText("label.since.date"));
        messages.put("untilDate", Internationalization.getText("label.until.date"));
        messages.put("totalSum", Internationalization.getText("label.total.sum"));
        messages.put("creationDate", Internationalization.getText("label.creation.date"));
        messages.put("type", Internationalization.getText("label.type"));

        messages.put("employee", Internationalization.getText("label.employee"));
        messages.put("date", Internationalization.getText("label.date"));
        messages.put("status", Internationalization.getText("label.status"));
        messages.put("sum", Internationalization.getText("label.sum"));
        messages.put("checkSearch", Internationalization.getText("label.check.search"));

        return messages;
    }

    @Override
    Report getEntityForUpdateCreate(HttpServletRequest request) {
        String reportJson = JsonUtil.readFromRequest(request);
        JsonNode reportTree = JsonUtil.getJsonTree(reportJson);
        ArrayNode checksJsonArray = (ArrayNode) reportTree.path("checks");
        List<Check> checkList = new ArrayList<>();

        for (int i = 0; i < checksJsonArray.size(); i++) {
            JsonNode checkNode = checksJsonArray.get(i);
            Check check = new Check.Builder()
                    .id(checkNode.path("id").longValue())
                    .build();

            checkList.add(check);
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        Report report = new Report.Builder()
                .id(reportTree.path("id").longValue())
                .since(LocalDateTime.parse(reportTree.path("sinceDate").asText(), dtf))
                .until(LocalDateTime.parse(reportTree.path("sinceDate").asText(), dtf))
                .checks(checkList)
                .totalSum(reportTree.path("totalSum").doubleValue())
                .creationDate(LocalDateTime.parse(reportTree.path("creationDate").asText(), dtf))
                .type(ReportType.valueOf(reportTree.path("type").textValue()))
                .build();


        return report;
    }

    @Override
    protected String createAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Report report = getEntityForUpdateCreate(request);
        TransactionManager tm = TransactionManagerService.getTransactionManager();
        try {
            tm.begin();

            report = reportService.create(report);

            for (Check c : report.getChecks()) {
                reportService.addCheck(report.getId(), c.getId());
            }

        } catch (TransactionException ex) {
            try {
                tm.rollback();
            } catch (TransactionException e) {
                throw new DaoException(e.getMessage(), ex);
            }
            throw new DaoException(ex.getMessage(), ex);
        }

        return "";
    }

    @Override
    protected String updateAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Report newReport = getEntityForUpdateCreate(request);

        if (checkForViolations(newReport, response)) {
            return "";
        }

        Report currentReport = reportService.findById(newReport.getId());
        List<Check> currentCheckProducts = currentReport.getChecks();
        List<Check> allChecks = Stream.concat(currentCheckProducts.stream(), newReport.getChecks().stream())
                .collect(Collectors.toList());

        TransactionManager tm = TransactionManagerService.getTransactionManager();
        try {
            tm.begin();

            for (Check check : newReport.getChecks()) {
                boolean presentInCurrentReport = currentCheckProducts.stream().anyMatch(c -> c.getId().equals(check.getId()));
                boolean presentInNewReport = newReport.getChecks().stream().anyMatch(c -> c.getId().equals(check.getId()));

                if (!presentInCurrentReport) {
                    reportService.addCheck(newReport.getId(), check.getId());
                }
                if (!presentInNewReport) {
                    reportService.removeCheck(newReport.getId(), check.getId());
                }
            }

            reportService.update(newReport);

            tm.commit();
        } catch (TransactionException ex) {
            try {
                tm.rollback();
            } catch (TransactionException e) {
                throw new DaoException(e.getMessage(), ex);
            }
            throw new DaoException(ex.getMessage(), ex);
        }

        return "";
    }

    @Override
    protected String getAction(HttpServletRequest request, HttpServletResponse response) throws DaoException, IOException {
        Report report = null;
        List<Report> reportList = null;
        String path = request.getPathInfo();
        if (path.matches("^/" + getEntitiesName() + "/\\d+$")) {
            report = reportService.findById(Long.parseLong(getRequestParam(request)));
            report.getChecks();
        } else if (path.matches("^/" + getEntitiesName() + "/findAllByType$")) {
            ReportType type = ReportType.valueOf(request.getParameter("type"));
            reportList = reportService.findAllByType(type);
        } else {
            return super.getAction(request, response);
        }

        String json;
        if (report != null) {
            json = constructFullJson(report);
        } else {
            json = constructFullJson(reportList.toArray(new Report[reportList.size()]));
        }

        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(json);

        return "";
    }
}
