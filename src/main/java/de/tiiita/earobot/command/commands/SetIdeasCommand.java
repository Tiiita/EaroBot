package de.tiiita.earobot.command.commands;

import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.Config;
import de.tiiita.earobot.util.EmbedUtil;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on März 16, 2023 | 17:52:37
 * (●'◡'●)
 */
public class SetIdeasCommand extends ListenerAdapter {
    private final DataManager dataManager;

    public SetIdeasCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("set-ideas-channel")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }
        String channelId = event.getChannel().getId();
        String guildId = event.getGuild().getId();

        dataManager.setIDData(guildId, Columns.IDEAS_CHANNEL.get(), channelId).whenComplete((unused, throwable) -> {
            MessageEmbed embed = EmbedUtil.getSimpleEmbed(null, "You set the ideas channel to: " + event.getChannel().getAsMention() + "\n"
                    + "Every message will get some vote reactions.");
            event.replyEmbeds(embed).setEphemeral(true).submit();
        });
    }
}
