package de.tiiita.earobot.listener;

import de.tiiita.earobot.EaroBot;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * Created on Mai 07, 2023 | 02:19:22
 * (●'◡'●)
 */
public class GuildJoinListener extends ListenerAdapter {


    private final EaroBot earoBot;
    private final DataManager dataManager;

    public GuildJoinListener(EaroBot earoBot) {
        this.earoBot = earoBot;
        this.dataManager = earoBot.getDataManager();
    }

    public void onGuildJoin(@Nonnull GuildJoinEvent event) {
        earoBot.registerNewGuild(event.getGuild().getId());
    }
}
