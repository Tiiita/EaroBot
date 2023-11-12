package de.tiiita.earobot.command.commands;

import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.EmbedUtil;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import de.tiiita.earobot.util.Config;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on März 16, 2023 | 17:29:03
 * (●'◡'●)
 */
public class SetWelcomeCommand extends ListenerAdapter {
    private final DataManager dataManager;

    public SetWelcomeCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("set-welcome-channel")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }
        String guildId = event.getGuild().getId();
        String channelId = event.getChannel().getId();

        dataManager.setIDData(guildId, Columns.WELCOME_CHANNEL, channelId).whenComplete((unused, throwable) -> {
            MessageEmbed embed = EmbedUtil.getSimpleEmbed(null, "You set the welcome channel to: " + event.getChannel().getAsMention() + "\n"
                    + "The welcome message will be send in here.");
            event.replyEmbeds(embed).setEphemeral(true).submit();
        });
    }
}
