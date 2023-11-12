package de.tiiita.earobot.ticketsystem;


import de.tiiita.earobot.ticketsystem.ticket.Ticket;
import de.tiiita.earobot.ticketsystem.ticket.TicketType;
import de.tiiita.earobot.util.EmbedUtil;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Created on März 17, 2023 | 16:00:50
 * (●'◡'●)
 */
public class TicketActionListener extends ListenerAdapter {

    private final TicketManager ticketManager;
    private final DataManager dataManager;

    public TicketActionListener(TicketManager ticketManager, DataManager dataManager) {
        this.ticketManager = ticketManager;
        this.dataManager = dataManager;
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if (event.getButton().getId() == null || event.getGuild() == null) return;
        switch (event.getButton().getId()) {
            case "ticketCloseButton": {
                CompletableFuture<Role> ticketRoleFuture = ticketManager.getTicketRole(event.getGuild().getId());
                if (ticketRoleFuture == null) return;

                ticketRoleFuture.whenComplete((role, throwable) -> {
                    if (Objects.requireNonNull(event.getMember()).getRoles().contains(role)) {
                        Ticket ticket = ticketManager.getTicketByChannelId(event.getChannel().getId());
                        if (ticket == null) return;
                        ticket.setCloser(event.getMember());
                        ticketManager.closeTicket(ticket); //Async
                    } else event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();

                });
                break;
            }


            case "ticketClaimButton": {
                Member claimer = event.getMember();
                ticketManager.getTicketRole(event.getGuild().getId()).whenComplete((role, throwable) -> {
                    if (!claimer.getRoles().contains(role))  {
                        event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();
                        return;
                    }

                    Ticket ticket = ticketManager.getTicketByChannelId(event.getChannel().getId());
                    if (ticket == null) return;
                    if (ticket.getCreator().getId().equals(claimer.getId())) {
                        event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();
                        return;
                    }


                    if (ticket.getClaimer() != null) {
                        event.reply("Ticket is already claimed by " + ticket.getClaimer().getAsMention() + ".")
                                .setEphemeral(true)
                                .queue();
                        return;
                    }

                    ticket.setClaimer(claimer.getUser());
                    event.reply(claimer.getAsMention() + " has claimed the ticket!").queue();
                    event.editButton(event.getButton().asDisabled()).queue();

                });


                break;
            }
        }
    }

    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        String componentId = event.getComponentId();

        if (!componentId.equalsIgnoreCase("ticketSelectionMenu")) return;

        for (TicketType ticketType : TicketType.values()) {
            if (event.getValues().contains(ticketType.getId())) {
                CompletableFuture<Ticket> ticketFuture = ticketManager.createTicket(event.getMember(), ticketType, event.getGuild());
                if (ticketFuture == null) {
                    event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "You already have an opened ticket!")).setEphemeral(true).queue();
                    event.editSelectMenu(event.getSelectMenu()).queue();
                    return;
                }
                ticketFuture.whenComplete((ticket, throwable) -> {
                    if (ticket.getTicketChannel() == null) {
                        return;
                    }
                    event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "You opened your ticket! Your ticket channel: " + ticket.getTicketChannel().getAsMention())).setEphemeral(true).queue();
                    event.editSelectMenu(event.getSelectMenu()).queue();
                });
            }
        }
    }
}
