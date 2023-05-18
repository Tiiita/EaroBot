package de.tiiita.earobot.ticket;

import de.tiiita.earobot.ticket.ticket.Ticket;
import de.tiiita.earobot.ticket.ticket.TicketType;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Created on März 17, 2023 | 16:00:50
 * (●'◡'●)
 */
public class TicketButtonListener extends ListenerAdapter {

    private final TicketManager ticketManager;

    public TicketButtonListener(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId() == null) return;

        if (event.getButton().getId().equals("ticketCloseButton")) {
            String guildId = event.getGuild().getId();
            Objects.requireNonNull(ticketManager.getTicketRole(guildId)).whenComplete((role, throwable) -> {
                if (Objects.requireNonNull(event.getMember()).getRoles().contains(role)) {
                    Ticket ticket = ticketManager.getTicketByChannelId(event.getChannel().getId());
                    if (ticket == null) return;
                    ticketManager.closeTicket(ticket);

                } else event.reply("You do not have permissions for that!").setEphemeral(true).submit();

            });
            return;
        }

        for (TicketType ticketType : TicketType.values()) {
            if (event.getButton().getId().equals(ticketType.getButtonId())) {
                Ticket createdTicket = ticketManager.createTicket(event.getMember(), ticketType, event.getGuild());
                if (createdTicket == null) {
                    event.reply("You already have an open ticket!").setEphemeral(true).submit();
                    return;
                }

                if (createdTicket.getTicketChannel() == null) return;
                event.reply("You opened your ticket! Your ticket channel: " + createdTicket.getTicketChannel().getAsMention()).setEphemeral(true).submit();
                return;
            }
        }
    }
}