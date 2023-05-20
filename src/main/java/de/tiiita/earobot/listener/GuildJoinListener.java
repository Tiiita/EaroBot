package de.tiiita.earobot.listener;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.command.CommandManager;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * Created on Mai 07, 2023 | 02:19:22
 * (●'◡'●)
 */
public class GuildJoinListener extends ListenerAdapter {


    private final CommandManager commandManager;


    public GuildJoinListener(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    @Override
    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        commandManager.registerCommandForNewGuild(event.getGuild().getId());
    }
}
