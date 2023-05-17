package de.tiiita.earobot.listener;

import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import de.tiiita.earobot.util.Config;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.logging.Level;

/**
 * Created on März 15, 2023 | 21:46:50
 * (●'◡'●)
 */
public class MessageReceiveListener extends ListenerAdapter {

    private final DataManager dataManager;

    public MessageReceiveListener(DataManager dataManager) {
        this.dataManager = dataManager;
    }


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String guildId = event.getGuild().getId();
        dataManager.getIdeasChannel(guildId).whenComplete((optionalId, throwable) -> {
            if (optionalId.isPresent()) {
                TextChannel ideasChannel = event.getJDA().getTextChannelById(optionalId.get());
                if (ideasChannel == null) {
                    return;
                }
                if (event.getChannel().equals(ideasChannel)) {
                    event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDC4D")).submit();
                    event.getMessage().addReaction(Emoji.fromUnicode("\uD83D\uDC4E")).submit();
                }
            }
        });
    }
}
