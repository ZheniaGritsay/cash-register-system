package com.projects.model.domain.dto;

import com.projects.model.domain.Entity;
import com.projects.model.domain.constant.Position;
import com.projects.model.validation.annotation.DecimalMin;
import com.projects.model.validation.annotation.Min;
import com.projects.model.validation.annotation.NotNull;
import com.projects.model.validation.annotation.Pattern;

public class Employee extends Entity {
    @Pattern(regex = {"^\\w{2,}$", "^\\S+$"}, message = "error.empty")
    private final String firstName;

    @Pattern(regex = {"^\\w{2,}$", "^\\S+$"}, message = "error.empty")
    private final String lastName;

    @Pattern(regex = {"^[A-Za-z0-9.]+@[a-z]+\\.[a-z]{2,6}$"}, message = "error.email")
    private final String email;

    @DecimalMin(message = "error.lt.zero")
    private final Double salary;

    @NotNull(message = "error.not.null")
    private final Position position;

    public Employee(Builder builder) {
        super(builder.id);
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.email = builder.email;
        this.salary = builder.salary;
        this.position = builder.position;
    }

    public Employee(Long id, String firstName, String lastName, String email, Double salary, Position position) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.salary = salary;
        this.position = position;
    }

    public static class Builder {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private Double salary;
        private Position position;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder salary(Double salary) {
            this.salary = salary;
            return this;
        }

        public Builder position(Position position) {
            this.position = position;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Double getSalary() {
        return salary;
    }

    public Position getPosition() {
        return position;
    }
}
