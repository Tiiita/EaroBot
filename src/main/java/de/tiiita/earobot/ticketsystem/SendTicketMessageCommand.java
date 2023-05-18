package de.tiiita.earobot.ticketsystem;

import de.tiiita.earobot.ticketsystem.ticket.TicketType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created on März 17, 2023 | 16:10:02
 * (●'◡'●)
 */
public class SendTicketMessageCommand extends ListenerAdapter {


    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("send-ticket-message")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }


        EmbedBuilder ticketEmbed = new EmbedBuilder();
        ticketEmbed.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
        ticketEmbed.setTitle("Create Ticket");
        ticketEmbed.setColor(Color.GREEN);


        ticketEmbed.setDescription("Below you can see several buttons for different Tickets."
                + "\n Click on the button that is for your ticket type!");


        Collection<Button> buttons = new HashSet<>();
        for (TicketType ticketType : TicketType.values()) {
            buttons.add(createButton(ticketType.getButtonId(), ticketType.getSelectionDisplay()));
        }

        event.getChannel().sendMessageEmbeds(ticketEmbed.build())
                .addActionRow(buttons).submit();
        event.reply("You have sent the panel!").setEphemeral(true).submit();

        buttons.clear();
    }

    private Button createButton(String id, String label) {
        return Button.success(id, label);
    }
}
