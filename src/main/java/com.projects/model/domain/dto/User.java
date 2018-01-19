package com.projects.model.domain.dto;

import com.projects.model.domain.Entity;
import com.projects.model.domain.constant.Role;

public class User extends Entity {
    private final String login;
    private final String password;
    private final Role role;
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
