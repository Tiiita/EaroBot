package de.tiiita.earobot.command.console;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.console.commands.EaroBotCommand;
import de.tiiita.earobot.command.console.commands.ListCommand;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import javax.smartcardio.ATR;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Level;

/**
 * Created on Mai 19, 2023 | 18:23:01
 * (●'◡'●)
 * <p>
 * Register this class as minecraft bungeecord listener!
 */
public class ConsoleCommandManager implements Listener {

    private final EaroBot earoBot;
    private final Collection<ConsoleCommand> registeredCommands = new HashSet<>();


    public ConsoleCommandManager(EaroBot earoBot) {
        this.earoBot = earoBot;
        registerCommands();
    }


    private void registerCommands() {
        //Register every command like that
        registerCommand(new EaroBotCommand(this));
        registerCommand(new ListCommand(earoBot.getJda()));
    }

    private void registerCommand(ConsoleCommand command) {
        registeredCommands.add(command);
        ProxyServer.getInstance().getPluginManager().registerCommand(earoBot, command);
    }

    /**
     * Get a command just by its name.
     * @param name the name of the command you want to find.
     * @return the found command or null if nothing was found.
     */
    @Nullable
    public ConsoleCommand getCommandByName(String name) {
        for (ConsoleCommand command : registeredCommands) {
            if (command.getName().equalsIgnoreCase(name)) return command;
        }
        return null;
    }
    public void sendRegisteredCommands() {
        CommandSender console = ProxyServer.getInstance().getConsole();
        console.sendMessage(new TextComponent(ChatColor.GRAY + "Registered Commands:"));
        console.sendMessage(new TextComponent(" "));

        int counter = 1;

        for (ConsoleCommand command : getRegisteredCommands()) {
            console.sendMessage(new TextComponent("" + ChatColor.DARK_PURPLE + counter + ChatColor.GRAY + ". " + command.getName() + ChatColor.DARK_PURPLE + " | " + ChatColor.GRAY + command.getExplanation()));
            counter++;
        }
    }

    /**
     * Get every registered command as command object
     *
     * @return a COPY of the registered commands set. Changing things on this method won't change it on the real list!
     */
    Collection<ConsoleCommand> getRegisteredCommands() {
        return new HashSet<>(registeredCommands);
    }
}