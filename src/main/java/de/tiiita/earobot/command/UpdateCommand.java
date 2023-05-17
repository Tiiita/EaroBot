package de.tiiita.earobot.command;

import de.tiiita.earobot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import de.tiiita.earobot.EaroBot;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

/**
 * Created on März 16, 2023 | 21:16:02
 * (●'◡'●)
 */
public class UpdateCommand extends ListenerAdapter {

    private final EaroBot plugin;

    public UpdateCommand(EaroBot plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("update")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }

        OptionMapping germanOption = event.getOption("german");
        OptionMapping englishOption = event.getOption("english");
        OptionMapping portugueseOption = event.getOption("portuguese");


        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Information");
        embed.setColor(Color.WHITE);;
        embed.addField("» English", englishOption.getAsString(), false);
        embed.addField("» Deutsch", germanOption.getAsString(), false);
        if (portugueseOption != null) {
            embed.addField("» Português", portugueseOption.getAsString(), false);
        }
        embed.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());


        event.getChannel().sendMessage(Objects.requireNonNull(event.getGuild()).getPublicRole().getAsMention()).submit();

        event.getChannel().sendMessageEmbeds(embed.build()).submit();


        MessageEmbed reply = EmbedUtil.getSimpleEmbed(Color.WHITE, "You have published the update!");
        event.replyEmbeds(reply).setEphemeral(true).submit();
    }
}
