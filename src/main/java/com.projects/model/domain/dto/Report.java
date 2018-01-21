package com.projects.model.domain.dto;

import com.projects.model.domain.Entity;
import com.projects.model.domain.constant.ReportType;
import com.projects.model.validation.annotation.DecimalMin;
import com.projects.model.validation.annotation.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public class Report extends Entity {
    @NotNull(message = "error.not.null")
    private final LocalDateTime since;

    @NotNull(message = "error.not.null")
    private final LocalDateTime until;

    @NotNull(message = "error.not.null")
    private final List<Check> checks;

    @DecimalMin(message = "error.lt.zero")
    private final Double totalSum;

    @NotNull(message = "error.not.null")
    private final LocalDateTime creationDate;

    @NotNull(message = "error.not.null")
    private final ReportType type;

    public Report(Builder builder) {
        super(builder.id);
        this.since = builder.since;
        this.until = builder.until;
        this.checks = builder.checks;
        this.totalSum = builder.totalSum;
        this.creationDate = builder.creationDate;
        this.type = builder.type;
    }

    public Report(Long id, LocalDateTime since, LocalDateTime until, List<Check> checks, Double totalSum, LocalDateTime creationDate, ReportType type) {
        super(id);
        this.since = since;
        this.until = until;
        this.checks = checks;
        this.totalSum = totalSum;
        this.creationDate = creationDate;
        this.type = type;
    }

    public static class Builder {
        private Long id;
        private LocalDateTime since;
        private LocalDateTime until;
        private List<Check> checks;
        private Double totalSum;
        private LocalDateTime creationDate;
        private ReportType type;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder since(LocalDateTime since) {
            this.since = since;
            return this;
        }

        public Builder until(LocalDateTime until) {
            this.until = until;
            return this;
        }

        public Builder checks(List<Check> checks) {
            this.checks = checks;
            return this;
        }

        public Builder totalSum(Double totalSum) {
            this.totalSum = totalSum;
            return this;
        }

        public Builder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public Builder type(ReportType type) {
            this.type = type;
            return this;
        }

        public Report build() {
            return new Report(this);
        }
    }

    public LocalDateTime getSince() {
        return since;
    }

    public LocalDateTime getUntil() {
        return until;
    }

    public List<Check> getChecks() {
        return checks;
    }

    public Double getTotalSum() {
        return totalSum;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public ReportType getType() {
        return type;
    }
}
