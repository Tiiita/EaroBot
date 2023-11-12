package de.tiiita.earobot.ticketsystem.ticket.followup;
import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.ticketsystem.ticket.Ticket;
import de.tiiita.earobot.util.EmbedUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Author: Tiiita
 * (●'◡'●)
 */
public class FollowUpMenuListener extends ListenerAdapter {
    private final TicketManager ticketManager;

    public FollowUpMenuListener(TicketManager ticketManager) {
        this.ticketManager = ticketManager;
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        Ticket ticket = ticketManager.getTicketByChannelId(event.getChannel().getId());
        if (ticket == null || ticket.getCreator() == null) return;
        Member member = event.getMember();
        if (member == null) return;
        if (!member.getId().equals(ticket.getCreator().getId())) {
            event.reply("Only " + ticket.getCreator().getAsMention() + " can select the ticket type...").setEphemeral(true).queue();
            return;
        }


        for (FollowUpType followUpType : FollowUpType.values()) {
            if (event.getValues().contains(followUpType.getId())) {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(EmbedUtil.getDiscordBackgroundColor());
                embed.setTitle("Selected Type: " + followUpType.getDisplayName());

                TextChannel ticketChannel = ticket.getTicketChannel();
                ticketChannel.getManager().setName(followUpType.getTicketSubName() + ticketChannel.getName()).queue();
                event.replyEmbeds(embed.build()).queue();
                event.editSelectMenu(event.getSelectMenu().asDisabled()).queue();
            }
        }
    }
}
