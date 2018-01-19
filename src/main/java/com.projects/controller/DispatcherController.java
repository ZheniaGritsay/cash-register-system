package com.projects.controller;

import com.projects.controller.command.Command;
import com.projects.controller.command.manager.CommandManager;
import com.projects.controller.command.manager.impl.CommandManagerImpl;
import com.projects.controller.exception.InternalServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet({"/index.html", "/app/*"})
public class DispatcherController extends HttpServlet {
    private static Logger logger = LoggerFactory.getLogger(DispatcherController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String prefix = "/WEB-INF/jsp/";
        String suffix = ".jsp";
        try {
            System.out.println(request.getPathInfo());
            System.out.println(request.getRequestURI());
            System.out.println(request.getServletPath());
            String commandName = request.getPathInfo();
            if (commandName == null)
                commandName = request.getRequestURI();
            if (commandName.equals("/")) {
                request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
                return;
            }

            CommandManager commandManager = CommandManagerImpl.getInstance();
            Command command = commandManager.getCommand(commandName);
            if (command != null) {
                String view = command.execute(request, response);
                if (view.isEmpty())
                    return;
                view = prefix + view + suffix;
                RequestDispatcher dispatcher = request.getRequestDispatcher(view);
                dispatcher.forward(request, response);
            } else {
                request.getRequestDispatcher(prefix + "404" + suffix);
//                response.sendError(404);
            }
        } catch (InternalServerException e) {
            logger.error("internal server error arose", e);
            request.getRequestDispatcher(prefix + "500" + suffix);
//            response.sendError(500);
        }
    }
}
