package de.tiiita.earobot.ticket.command;

import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

/**
 * Created on März 17, 2023 | 15:31:00
 * (●'◡'●)
 */
public class SetTicketRoleCommand extends ListenerAdapter {

    private final DataManager dataManager;

    public SetTicketRoleCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("set-ticket-role")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }

        OptionMapping roleOption = event.getOption("role");

        if (roleOption == null) return;
        Role ticketListeningRole = roleOption.getAsRole();
        String guildId = event.getGuild().getId();

        dataManager.setIDData(guildId, Columns.TICKET_ROLE.get(), ticketListeningRole.getId()).whenComplete((unused, throwable) -> {
            event.reply("You set the ticket listening role to: " + ticketListeningRole.getAsMention() + ".").setEphemeral(true).submit();
        });
    }
}
