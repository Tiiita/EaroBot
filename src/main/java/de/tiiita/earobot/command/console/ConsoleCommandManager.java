package de.tiiita.earobot.command.console;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.console.commands.RebootCommand;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import org.jetbrains.annotations.Nullable;

import javax.smartcardio.ATR;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created on Mai 19, 2023 | 18:23:01
 * (●'◡'●)
 * <p>
 * Register this class as minecraft bungeecord listener!
 */
public class ConsoleCommandManager implements Listener {

    private final EaroBot earoBot;
    private final Collection<Command> registeredCommands = new HashSet<>();


    public ConsoleCommandManager(EaroBot earoBot) {
        this.earoBot = earoBot;

        registerCommands();
    }


    void registerCommands() {
        //Register every command like that
        registerCommand(new RebootCommand(earoBot));
    }

    void registerCommand(Command command) {
        registeredCommands.add(command);
    }

    @EventHandler
    public void onConsoleChat(ChatEvent event) {
        if (event.getSender() != ProxyServer.getInstance().getConsole()) return;
        String message = event.getMessage();

        if (message.equalsIgnoreCase("earobot")) {
            sendRegisteredCommands();
            return;
        }
        if (message.startsWith("earobot ")) {
            String commandName = message.substring(8); // Extract the command name from the message
            Command command = getCommandByName(commandName);
            if (command != null) {
                command.getAction().run();
            }
        } else sendRegisteredCommands();
    }

    /**
     * Get a command just by its name.
     * @param name the name of the command you want to find.
     * @return the found command or null if nothing was found.
     */
    @Nullable
    Command getCommandByName(String name) {
        for (Command command : registeredCommands) {
            if (command.getName().equalsIgnoreCase(name)) return command;
        }
        return null;
    }
    void sendRegisteredCommands() {
        CommandSender console = ProxyServer.getInstance().getConsole();
        console.sendMessage(new TextComponent("Registered Commands:"));
        console.sendMessage(new TextComponent(" "));
        getRegisteredCommands().forEach(command -> {
            console.sendMessage(new TextComponent("- earobot " + command.getName() + " | " + command.getExplanation()));
        });
    }

    /**
     * Get every registered command as command object
     *
     * @return a COPY of the registered commands set. Changing things on this method wont change it on the real list!
     */
    Collection<Command> getRegisteredCommands() {
        return new HashSet<>(registeredCommands);
    }
}
