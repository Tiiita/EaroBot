package de.tiiita.earobot.command.console.commands;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.console.ConsoleCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Created on Mai 19, 2023 | 18:23:16
 * (●'◡'●)
 */
public class RebootCommand extends ConsoleCommand {
    private final EaroBot earoBot;

    public RebootCommand(EaroBot earoBot) {
        super("earobot-reboot", "Reboot the bot without restarting the server");
        this.earoBot = earoBot;
        setActionOnRun(this::action);
    }


    private void action() {
        earoBot.onDisable();
        earoBot.onEnable();
    }
}
