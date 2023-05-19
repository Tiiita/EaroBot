package de.tiiita.earobot.ticketsystem;

import de.tiiita.earobot.ticketsystem.ticket.Ticket;
import de.tiiita.earobot.ticketsystem.ticket.TicketType;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Proxy;
import java.util.Objects;
import java.util.logging.Level;

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
                    ticketManager.closeTicket(ticket); //Async

                } else event.reply("You do not have permissions for that!").setEphemeral(true).submit();

            });
            return;
        }

        for (TicketType ticketType : TicketType.values()) {
            if (event.getButton().getId().equals(ticketType.getButtonId())) {
                ticketManager.createTicket(event.getMember(), ticketType, event.getGuild()).whenComplete((ticket, throwable) -> {
                    if (ticket == null) {
                        event.reply("You already have an open ticket!").setEphemeral(true).submit();
                        return;
                    }
                    if (ticket.getTicketChannel() == null) {
                        return;
                    }
                    event.reply("You opened your ticket! Your ticket channel: " + ticket.getTicketChannel().getAsMention()).setEphemeral(true).submit();
                });
            }
        }
    }
}
