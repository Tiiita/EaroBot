package de.tiiita.earobot.command.console.commands;

import de.tiiita.earobot.command.console.ConsoleCommand;
import de.tiiita.earobot.command.console.ConsoleCommandManager;

/**
 * Created on Mai 19, 2023 | 19:17:13
 * (●'◡'●)
 */
public class EaroBotCommand extends ConsoleCommand {
    private final ConsoleCommandManager consoleCommandManager;

    public EaroBotCommand(ConsoleCommandManager consoleCommandManager) {
        super("earobot", "List every command.");
        this.consoleCommandManager = consoleCommandManager;
        setActionOnRun(this::action);
    }

    private void action() {
      consoleCommandManager.sendRegisteredCommands();
    }
}
