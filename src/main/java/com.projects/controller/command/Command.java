package com.projects.controller.command;

import com.projects.controller.exception.InternalServerException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Command {
    String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException;
}
