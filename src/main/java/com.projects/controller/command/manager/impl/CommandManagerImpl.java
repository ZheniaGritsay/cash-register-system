package com.projects.controller.command.manager.impl;

import com.projects.controller.command.Command;
import com.projects.controller.command.impl.*;
import com.projects.controller.command.impl.action.*;
import com.projects.controller.command.manager.CommandManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommandManagerImpl implements CommandManager {
    private static final CommandManagerImpl instance = new CommandManagerImpl();
    private Map<String, Command> commands;

    private CommandManagerImpl() {
        commands = new HashMap<>();
    }

    @Override
    public Command getCommand(String commandName) {
        String commandPath = commands.keySet().stream()
                .filter(cm -> commandName.matches("^" + cm + ".*")).findFirst().orElse("");

        return commands.get(commandPath);
    }

    public static CommandManagerImpl getInstance() {
        return instance;
    }

    public void initCommands() throws IOException {
        Properties properties = new Properties();
        try(InputStream is = CommandManagerImpl.class.getClassLoader().getResourceAsStream("web/commands.properties")) {
            properties.load(is);
        }

        commands = new HashMap<>();
        commands.put(properties.getProperty("login"), new LoginCommand());
        commands.put(properties.getProperty("logout"), new LogoutCommand());
        commands.put(properties.getProperty("registration"), new RegistrationCommand());
        commands.put(properties.getProperty("common"), new CommonCommand());
        commands.put(properties.getProperty("checks"), new ActionCheckCommand());
        commands.put(properties.getProperty("products"), new ActionProductCommand());
        commands.put(properties.getProperty("reports"), new ActionReportCommand());
        commands.put(properties.getProperty("employees"), new ActionEmployeeCommand());
        commands.put(properties.getProperty("users"), new ActionUserCommand());
        commands.put(properties.getProperty("edit.account"), new EditAccountCommand());
    }
}
