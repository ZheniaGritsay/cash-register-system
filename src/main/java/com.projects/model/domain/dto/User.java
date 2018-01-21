package com.projects.model.domain.dto;

import com.projects.model.domain.Entity;
import com.projects.model.domain.constant.Role;
import com.projects.model.validation.annotation.NotNull;
import com.projects.model.validation.annotation.Pattern;

public class User extends Entity {
    @Pattern(regex = {"^\\w{2,}$", "^\\S+$"}, message = "error.empty")
    private final String login;

    @Pattern(regex = "^.{5,}$", message = "error.should.be.greater")
    private final String password;

    @NotNull(message = "error.not.null")
    private final Role role;

    @NotNull(message = "error.not.null")
    private final Employee employee;

    public User(Long id, String login, String password, Role role, Employee employee) {
        super(id);
        this.login = login;
        this.password = password;
        this.role = role;
        this.employee = employee;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public Employee getEmployee() {
        return employee;
    }
}
