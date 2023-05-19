package de.tiiita.earobot.command.console;

import de.tiiita.earobot.command.console.commands.EaroBotCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.lang.reflect.Proxy;

/**
 * Created on Mai 19, 2023 | 19:06:49
 * (●'◡'●)
 */
public abstract class ConsoleCommand extends Command {


    private final String name;
    private final String explanation;
    private Runnable action;


    public ConsoleCommand(String name, String explanation) {
        super(name);
        this.name = name;
        this.explanation = explanation;
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender != ProxyServer.getInstance().getConsole()) return;
        if (action != null) {
            action.run();
        }
    }


    public void setActionOnRun(Runnable action) {
        this.action = action;
    }

    public Runnable getAction() {
        return action;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getExplanation() {
        return explanation;
    }
}
