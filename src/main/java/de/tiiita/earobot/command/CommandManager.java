package de.tiiita.earobot.command;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.commands.*;
import de.tiiita.earobot.listener.GuildJoinListener;
import de.tiiita.earobot.listener.MessageReceiveListener;
import de.tiiita.earobot.listener.UserJoinLeaveListener;
import de.tiiita.earobot.ticketsystem.TicketButtonListener;
import de.tiiita.earobot.ticketsystem.command.SendTicketMessageCommand;
import de.tiiita.earobot.ticketsystem.command.SetTicketRoleCommand;
import de.tiiita.earobot.util.database.DataManager;
import jdk.nashorn.internal.scripts.JD;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on Mai 20, 2023 | 11:51:35
 * (●'◡'●)
 */
public class CommandManager {

    private final DataManager dataManager;
    private final JDA jda;

    private final ClearSpamCommand clearSpamCommand;
    private final SetWelcomeCommand setWelcomeCommand;
    private final HelpCommand helpCommand;
    private final UpdateCommand updateCommand;
    private final SetIdeasCommand setIdeasCommand;
    private final SendTicketMessageCommand sendTicketMessageCommand;
    private final SetTicketRoleCommand setTicketRoleCommand;

    public CommandManager(EaroBot earoBot) {
        this.dataManager = earoBot.getDataManager();
        this.jda = earoBot.getJda();

        this.clearSpamCommand = new ClearSpamCommand();
        this.setWelcomeCommand = new SetWelcomeCommand(dataManager);
        this.helpCommand = new HelpCommand();
        this.updateCommand = new UpdateCommand();
        this.setIdeasCommand = new SetIdeasCommand(dataManager);
        this.sendTicketMessageCommand = new SendTicketMessageCommand();
        this.setTicketRoleCommand = new SetTicketRoleCommand(dataManager);

        jda.addEventListener(clearSpamCommand);
        jda.addEventListener(setIdeasCommand);
        jda.addEventListener(helpCommand);
        jda.addEventListener(updateCommand);
        jda.addEventListener(sendTicketMessageCommand);
        jda.addEventListener(setTicketRoleCommand);
        jda.addEventListener(setWelcomeCommand);
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
        registerCommand(guildId, "clear-spam", "Clear channels from raid / spam messages", clearSpamCommand)
                .addOption(OptionType.STRING, "message", "The message that is equally to the one that should be deleted", true)
                .submit();
        registerCommand(guildId, "set-welcome-channel", "With this command you can set the welcome channel for the bot!", setWelcomeCommand);
        registerCommand(guildId, "help", "Get help or information!", helpCommand);
        registerCommand(guildId, "set-ideas-channel", "With this command you can set the channel where the player adds automatic vote reactions!", setIdeasCommand);
        registerCommand(guildId, "update", "With this command an admin can announce updates!", updateCommand);

        registerCommand(guildId, "send-ticket-message", "Send the ticket creation panel", sendTicketMessageCommand);
        registerCommand(guildId, "set-ticket-role", "Set the support role for tickets", setTicketRoleCommand)
                .addOption(OptionType.ROLE, "role", "Select the ticket listening role!", true)
                .submit();

    }

    private CommandCreateAction registerCommand(@NotNull String guildId, @NotNull String name, @NotNull String description, @NotNull Object command) {
        Guild guildById = jda.getGuildById(guildId);
        if (guildById == null) throw new IllegalArgumentException("Could not find any guild with id: " + guildId);
        CommandCreateAction createdCommand = guildById.upsertCommand(name, description);
        createdCommand.submit();
        return createdCommand;
    }
}
