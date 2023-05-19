package de.tiiita.earobot.command.console.commands;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.console.ConsoleCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

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

        int waitSeconds = 3;
        ProxyServer.getInstance().getLogger().log(Level.INFO, "Waiting " + waitSeconds + " seconds until reboot...");
        ProxyServer.getInstance().getScheduler().schedule(earoBot, earoBot::onEnable, waitSeconds, TimeUnit.SECONDS);
        ProxyServer.getInstance().getLogger().log(Level.INFO, "Reboot complete!");
    }
}
