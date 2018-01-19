package com.projects.model.domain.dto;

import com.projects.model.domain.Entity;
import com.projects.model.domain.constant.CheckStatus;

import java.time.LocalDateTime;
import java.util.List;

public class Check extends Entity {
    private final Employee employee;
    private final List<Product> products;
    private final Double sum;
    private final LocalDateTime date;
    private final CheckStatus status;

    public Check(Builder builder) {
        super(builder.id);
        this.employee = builder.employee;
        this.products = builder.products;
        this.sum = builder.sum;
        this.date = builder.date;
        this.status = builder.status;
    }

    public Check(Long id, Employee employee, List<Product> products, Double sum, LocalDateTime date, CheckStatus status) {
        super(id);
        this.employee = employee;
        this.products = products;
        this.sum = sum;
        this.date = date;
        this.status = status;
    }

    public static class Builder {
        private Long id;
        private Employee employee;
        private List<Product> products;
        private Double sum;
        private LocalDateTime date;
        private CheckStatus status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder employee(Employee employee) {
            this.employee = employee;
            return this;
        }

        public Builder products(List<Product> products) {
            this.products = products;
            return this;
        }

        public Builder sum(Double sum) {
            this.sum = sum;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder status(CheckStatus status) {
            this.status = status;
            return this;
        }

        public Check build() {
            return new Check(this);
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Double getSum() {
        return sum;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public CheckStatus getStatus() {
        return status;
    }
}
