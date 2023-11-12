package de.tiiita.earobot.command;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.commands.ClearSpamCommand;
import de.tiiita.earobot.command.commands.DoenerCommand;
import de.tiiita.earobot.command.commands.SetIdeasCommand;
import de.tiiita.earobot.command.commands.SetWelcomeCommand;
import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.ticketsystem.command.ReClaimCommand;
import de.tiiita.earobot.util.Config;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created on Mai 20, 2023 | 11:51:35
 * (●'◡'●)
 */
public class CommandManager {

    private final DataManager dataManager;
    private final JDA jda;
    private final Config config;
    private final TicketManager ticketManager;

    public CommandManager(EaroBot bot) {
        this.dataManager = bot.getDataManager();
        this.jda = bot.getJda();
        this.config = bot.getConfig();
        this.ticketManager = bot.getTicketManager();

        jda.addEventListener(new SetWelcomeCommand(dataManager));
        jda.addEventListener(new SetIdeasCommand(dataManager));
        jda.addEventListener(new DoenerCommand(config));
        jda.addEventListener(new ClearSpamCommand());
        jda.addEventListener(new ReClaimCommand(ticketManager));
    }


    //Call this for every guild join!
    public void registerCommandForNewGuild(String guildId) {
        dataManager.registerGuild(guildId).whenComplete((unused, throwable) -> {
            registerCommandsForGuild(guildId);
        });
    }


    //Call this at start
    public void registerCommands() {
        List<Guild> guilds = jda.getGuilds();
        guilds.forEach(currentGuild -> {
            String guildId = currentGuild.getId();
            registerCommandsForGuild(guildId);
        });
    }

    private void registerCommandsForGuild(String guildId) {
        registerCommand(guildId, "set-welcome-channel", "With this command you can set the welcome channel for the bot!");
        registerCommand(guildId, "set-ideas-channel", "Set the channel where the bot should react to messages.");
        registerCommand(guildId, "doener", "Let the bot spend you a kebab :)");
        registerCommand(guildId, "clear-spam", "Clear the bot after a raid.");
        registerCommand(guildId, "ticket-reclaim", "Re-Claim a ticket.");
        registerCommand(guildId, "setup-ticket", "With this command you can setup the discord ticket system")
                .addOption(OptionType.ROLE, "ticket-role", "Setup the ticket listening role.", true)
                .queue();
    }

    private CommandCreateAction registerCommand(@NotNull String guildId, @NotNull String name, @NotNull String description) {
        Guild guildById = jda.getGuildById(guildId);
        if (guildById == null) throw new IllegalArgumentException("Could not find any guild with id: " + guildId);
        CommandCreateAction createdCommand = guildById.upsertCommand(name, description);
        createdCommand.queue();
        return createdCommand;
    }
}
