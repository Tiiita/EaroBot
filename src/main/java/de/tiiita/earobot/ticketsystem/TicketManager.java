package de.tiiita.earobot.ticketsystem;

import de.tiiita.earobot.ticketsystem.ticket.Ticket;
import de.tiiita.earobot.ticketsystem.ticket.TicketType;
import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Created on März 17, 2023 | 13:54:08
 * (●'◡'●)
 */
public class TicketManager {

    private final JDA jda;
    private final DataManager dataManager;


    public TicketManager(JDA jda, DataManager dataManager) {
        this.jda = jda;
        this.dataManager = dataManager;
    }

    //Current open tickets.
    private final Set<Ticket> tickets = new HashSet<>();

    /**
     * Create tickets to get contact to a staff member.
     *
     * @param creator the creator of the ticket.
     * @return the ticket if it works and the ticket was created, null if the creator has a ticket open already.
     */
    @Nullable
    public Ticket createTicket(Member creator, TicketType ticketType, Guild guild) {
        if (!hasOpenTicket(creator)) {
            Ticket ticket = new Ticket(ticketType, jda, this);
            tickets.add(ticket);
            ticket.open(creator, guild);
            return ticket;
        } else return null;
    }


    /**
     * Every guild that wants to use the ticket system must configure a ticket role. That is simply a role
     * that can manage tickets. Mostly admin roles.
     * @param guildId the guildId where the system searches in the database.
     * @return The role if found. If the role was not set or was not found with jda it returns null.
     */
    @Nullable
    public CompletableFuture<Role> getTicketRole(String guildId) {
        return dataManager.getIDData(guildId, Columns.TICKET_ROLE.get()).thenApply(optional -> optional.map(jda::getRoleById).orElse(null));
    }
    /**
     * @param creator that creates a ticket! A User can only create once a time, so you can get the current open one!
     * @return found ticket from list, or null if the user has not open ticket.
     */
    @Nullable
    public Ticket getTicketByCreator(Member creator) {
        for (Ticket ticket : tickets) {
            if (ticket.getCreator() == null) return null;
            if (ticket.getCreator().getId().equals(creator.getId())) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * Get a ticket by its channel id.
     * @param channelId channel id where the function gets a ticket from.
     * @return the found ticket or null of no ticket was found.
     */
    @Nullable
    public Ticket getTicketByChannelId(String channelId) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketChannelId() == null) return null;
            if (ticket.getTicketChannelId().equals(channelId)) {
                return ticket;
            }
        }
        return null;
    }

    /**
     * This checks if a user has an open ticket!
     *
     * @param user user that should be checked.
     * @return true if the user has an open ticket, false if not.
     */
    public boolean hasOpenTicket(Member user) {
        return getTicketByCreator(user) != null;
    }

    /**
     * Close the tickets and the channels will be deleted too!
     *
     * @param ticket ticket that you want to close.
     */
    public void closeTicket(Ticket ticket) {
        ticket.close();
        tickets.remove(ticket);
    }

    /**
     * Close all open tickets with this!
     * @return number of closed tickets.
     */
    public int closeAllTickets() {
        int closedTickets = 0;
        for (Ticket ticket : tickets) {
            closeTicket(ticket);
            closedTickets++;
        }

        return closedTickets;
    }
}
