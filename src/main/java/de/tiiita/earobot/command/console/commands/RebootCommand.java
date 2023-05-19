package de.tiiita.earobot.command.console.commands;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.console.Command;
import net.md_5.bungee.api.ProxyServer;

/**
 * Created on Mai 19, 2023 | 18:23:16
 * (●'◡'●)
 */
public class RebootCommand extends Command {
    private final EaroBot earoBot;

    public RebootCommand(EaroBot earoBot) {
        super("reboot", "Restart the bot without restarting the server");
        this.earoBot = earoBot;
        setActionOnRun(this::action);
    }


    private void action() {
        earoBot.onDisable();
        earoBot.onEnable();
    }
}
