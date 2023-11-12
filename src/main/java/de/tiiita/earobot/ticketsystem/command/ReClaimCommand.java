package de.tiiita.earobot.ticketsystem.command;

import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.ticketsystem.ticket.Ticket;
import de.tiiita.earobot.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

public class ReClaimCommand extends ListenerAdapter {


    private final TicketManager ticketManager;

    public ReClaimCommand(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("ticket-reclaim")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();
            return;
        }

        Ticket ticket = ticketManager.getTicketByChannelId(event.getChannel().getId());

        if (ticket == null) {
            event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "You can only use this command in tickets!")).setEphemeral(true).queue();
            return;
        }



        if (ticket.getClaimer() == null) {
            event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "Ticket has not been claimed yet. Please claim before you can re-claim!")).setEphemeral(true).queue();
            return;
        }

        if (ticket.getClaimer().getId().equals(event.getMember().getId())) {
            event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "You already claimed this ticket!")).setEphemeral(true).queue();
            return;
        }

        ticket.setClaimer(event.getMember().getUser());
        event.reply(event.getMember().getAsMention() + " re-claimed this ticket!").queue();
    }
}
