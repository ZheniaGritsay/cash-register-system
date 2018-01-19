package com.projects.controller.command.impl;

import com.projects.controller.command.Command;
import com.projects.controller.exception.InternalServerException;
import com.projects.controller.util.PagesView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogoutCommand implements Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws InternalServerException {
        request.getSession().invalidate();
        return PagesView.LOGIN;
    }
}
