package com.projects.controller.command.manager.impl;

import com.projects.controller.command.Command;
import com.projects.controller.command.impl.*;
import com.projects.controller.command.impl.action.*;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.*;

@RunWith(JUnitParamsRunner.class)
public class CommandManagerTest {
    private CommandManagerImpl manager = CommandManagerImpl.getInstance();

    @Before
    public void setUp() throws IOException {
        manager.initCommands();
    }

    private Object[][] parametersForGetCommand() {
        return new Object[][]{{"/home", CommonCommand.class}, {"/login", LoginCommand.class}, {"/logout", LogoutCommand.class},
                {"/registration", RegistrationCommand.class}, {"/edit-account", EditAccountCommand.class},
                {"/checks", ActionCheckCommand.class}, {"/products", ActionProductCommand.class},
                {"/employees", ActionEmployeeCommand.class}, {"/reports", ActionReportCommand.class},
                {"/users", ActionUserCommand.class}};
    }

    @Test
    @Parameters
    public void getCommand(String path, Class commandClass) {
        Command command = manager.getCommand(path);

        assertEquals(commandClass, command.getClass());
    }
}
