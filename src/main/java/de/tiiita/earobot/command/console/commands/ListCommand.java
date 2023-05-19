package de.tiiita.earobot.command.console.commands;

import de.tiiita.earobot.command.console.ConsoleCommand;
import jdk.nashorn.internal.scripts.JD;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created on Mai 19, 2023 | 19:46:22
 * (●'◡'●)
 */
public class ListCommand extends ConsoleCommand {
    private final JDA jda;
    public ListCommand(JDA jda) {
        super("earobot-list", "List every server where the earobot is present.");
        this.jda = jda;
        setActionOnRun(this::action);
    }

    private void action() {
        int guildSize = jda.getGuilds().size();

        log(ChatColor.GRAY + "The EaroBot is on " + ChatColor.DARK_PURPLE + guildSize + ChatColor.GRAY + " discord servers!");
        log(ChatColor.GRAY + "> Servers:");

        int counter = 1;
        for (Guild guild : jda.getGuilds()) {
            log("" + ChatColor.DARK_PURPLE + counter + ChatColor.GRAY + ". " + guild.getName());
            counter++;
        }
    }

    private void log(String text) {
        ProxyServer.getInstance().getLogger().log(Level.INFO, text);
    }
}
