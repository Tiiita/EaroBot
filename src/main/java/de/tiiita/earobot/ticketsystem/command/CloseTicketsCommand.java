package de.tiiita.earobot.ticketsystem.command;

import de.tiiita.earobot.ticketsystem.TicketManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;

/**
 * Created on Mai 18, 2023 | 19:21:30
 * (●'◡'●)
 */
public class CloseTicketsCommand extends ListenerAdapter {

    private final TicketManager ticketManager;

    public CloseTicketsCommand(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("close-tickets")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }

        int closedTickets = ticketManager.closeAllTickets();
        event.reply("You have closed " + closedTickets + " tickets!").submit();
    }
}
