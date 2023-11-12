package de.tiiita.earobot.ticketsystem;
import de.tiiita.earobot.ticketsystem.ticket.TicketType;
import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.EmbedUtil;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on Juni 21, 2023 | 13:41:52
 * (●'◡'●)
 */
public class SetupTicketCommand extends ListenerAdapter {
    private final DataManager dataManager;

    public SetupTicketCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("setup-ticket")) return;
        if (event.getMember() == null) return;
        /*if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();
            return;
        }*/


        Role ticketRole = event.getOption("ticket-role").getAsRole();
        String guildId = event.getGuild().getId();
        dataManager.setIDData(guildId, Columns.TICKET_ROLE, ticketRole.getId()).whenComplete((unused, throwable) -> {
            sendPanel(event.getChannel().asTextChannel(), event.getJDA());
        });

        event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "You successfully setup the ticket system.")).setEphemeral(true).queue();
    }

    private void sendPanel(TextChannel channel, JDA jda) {
        EmbedBuilder ticketEmbed = new EmbedBuilder();
        ticketEmbed.setThumbnail(jda.getSelfUser().getAvatarUrl());
        ticketEmbed.setTitle("Ticket System");
        ticketEmbed.setColor(Color.WHITE);

        ticketEmbed.setDescription("Welcome to the ticket system!\n\n"
                + "Here you can create a ticket to get in contact with staff!\n"
                + "You can apply for higher ranks or get support / report bugs.\n\n"
                + "Do not ping our staff or spam, this will end in a mute.");


        StringSelectMenu.Builder selectionMenu = StringSelectMenu.create("ticketSelectionMenu");
        for (TicketType ticketType : TicketType.values()) {
            selectionMenu.addOption(ticketType.getDisplayName(), ticketType.getId(), ticketType.getDescription(), ticketType.getEmoji());
        }


        channel.sendMessageEmbeds(ticketEmbed.build())
                .addActionRow(selectionMenu.build()).queue();
    }
}
