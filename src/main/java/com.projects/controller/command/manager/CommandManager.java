package com.projects.controller.command.manager;

import com.projects.controller.command.Command;

public interface CommandManager {
    Command getCommand(String commandName);
}
