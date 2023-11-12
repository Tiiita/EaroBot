package de.tiiita.earobot.ticketsystem.ticket.followup;
import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.ticketsystem.ticket.Ticket;
import de.tiiita.earobot.ticketsystem.ticket.TicketType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

/**
 * Author: Tiiita
 * (●'◡'●)
 */
public class FollowUpSetter {

    private final JDA jda;
    private final TicketManager ticketManager;


    public FollowUpSetter(JDA jda, TicketManager ticketManager) {
        this.jda = jda;
        this.ticketManager = ticketManager;
        setFollowUps();
    }

    private void setFollowUps() {
        for (TicketType ticketType : TicketType.values()) {
            switch (ticketType) {
                case APPLICATION: {
                    FollowUp followUp = new FollowUp(channel -> {
                        StringSelectMenu.Builder selectionMenu = StringSelectMenu.create("applicationSelectionMenu");
                        for (FollowUpType followUpType : FollowUpType.values()) {
                            selectionMenu.addOption(followUpType.getDisplayName(), followUpType.getId(), followUpType.getDescription(), followUpType.getEmoji());
                        }

                        StringSelectMenu menu = selectionMenu.build();

                        Ticket ticket = ticketManager.getTicketByChannelId(channel.getId());
                        channel.sendMessage(ticket.getCreator().getAsMention() + ", please select an application type: ").addActionRow(menu).queue();
                    });
                    ticketType.setFollowUp(followUp);
                    break;
                }

                default: {
                    ticketType.setFollowUp(null);
                    break;
                }
            }
        }
    }
}
