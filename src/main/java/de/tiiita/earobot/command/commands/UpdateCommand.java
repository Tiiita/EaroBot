package de.tiiita.earobot.command.commands;

import de.tiiita.earobot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import de.tiiita.earobot.EaroBot;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.FileUpload;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;

/**
 * Created on März 16, 2023 | 21:16:02
 * (●'◡'●)
 */
public class UpdateCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("update")) return;

        if (event.getMember() == null) {
            return;
        }

        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }

        //Modal
        TextInput updateTitleInput = TextInput.create("title-input", "Update Title", TextInputStyle.SHORT)
                .setRequired(true)
                .setMinLength(1)
                .setMaxLength(30)
                .setPlaceholder("Your Update title here.")
                .build();
        TextInput englishInput = TextInput.create("english-input", "English", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setMaxLength(4000)
                .setRequired(true)
                .setPlaceholder("Write here the information in english :)")
                .build();
        TextInput germanInput = TextInput.create("german-input", "Deutsch", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setMaxLength(4000)
                .setRequired(true)
                .setPlaceholder("Write here the information in german :)")
                .build();
        TextInput portugueseInput = TextInput.create("portuguese-input", "Português", TextInputStyle.PARAGRAPH)
                .setMinLength(1)
                .setRequired(false)
                .setMaxLength(4000)
                .setPlaceholder("Write here the information in portuguese (Write nothing if you just want german and english!)")
                .build();

        Modal modal = Modal.create("update-modal", "Create Update Message")
                .addActionRows(ActionRow.of(updateTitleInput), ActionRow.of(englishInput), ActionRow.of(germanInput), ActionRow.of(portugueseInput))
                .build();
        event.replyModal(modal).submit();
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (!event.getModalId().equals("update-modal")) return;


        String titleInput = event.getValue("title-input").getAsString();
        String englishInput = event.getValue("english-input").getAsString();
        String germanInput = event.getValue("german-input").getAsString();
        String portugueseInput = event.getValue("portuguese-input").getAsString();


        //Check if its empty (Just spaces also counts as empty)
        if (portugueseInput.trim().length() == 0) portugueseInput = null;

        if (event.getGuild() == null) return;

        String publicRole = event.getGuild().getPublicRole().getAsMention();
        String message = buildMessage(titleInput, englishInput, germanInput, portugueseInput, publicRole);
        event.getChannel().sendMessage(message).submit();
        event.reply("You successfully published a new update!").setEphemeral(true).submit();

    }

    private String buildMessage(@NotNull String title, @NotNull String english, @NotNull String german, @Nullable String portuguese, String publicRole) {
        String message = publicRole + "\n" + "## " + title + "\n\n  "
                + "**»** \uD83C\uDDE9\uD83C\uDDEA \n"
                + "> " + german
                + "\n\n"
                + "**»** \uD83C\uDDEC\uD83C\uDDE7 \n"
                + "> " +  english;


        if (portuguese != null) {
            message = message + "\n\n **»** \uD83C\uDDE7\uD83C\uDDF7 \n"
                    + "> " + portuguese;

        }

        return message;
    }
}
