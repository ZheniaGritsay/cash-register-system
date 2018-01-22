package com.projects.model.holders;

import com.projects.model.dao.CheckDao;
import com.projects.model.dao.exception.DaoException;
import com.projects.model.dao.factory.impl.DaoFactoryImpl;
import com.projects.model.domain.dto.Check;
import com.projects.model.domain.dto.Report;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ReportHolder extends Report {
    private static final Logger logger = LoggerFactory.getLogger(ReportHolder.class);
    private CheckDao checkDao;
    private List<Check> checkSet;

    public ReportHolder(Builder builder) {
        super(builder);
        this.checkDao = DaoFactoryImpl.getInstance().getCheckDao();
        this.checkSet = new ArrayList<>();
    }

    public static class BuilderHolder extends Report.Builder {

        @Override
        public Report build() {
            return new ReportHolder(this);
        }
    }

    @Override
    public List<Check> getChecks() {
        if (super.getChecks() == null || super.getChecks().isEmpty()) {
            if (checkSet.isEmpty()) {
                try {
                    List<Check> checkList = checkDao.getAllByReportId(getId());
                    checkSet = new ArrayList<>(checkList);
                } catch (DaoException e) {
                    logger.error("failed to get report's checks: " + e.getMessage());
                }
            }
            return checkSet;
        }
        return super.getChecks();
    }
}
