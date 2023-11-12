package de.tiiita.earobot.command.commands;

import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.EmbedUtil;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.jetbrains.annotations.NotNull;

public class SetAutoRoleCommand extends ListenerAdapter {

    private final DataManager dataManager;

    public SetAutoRoleCommand(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("set-autorole")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();
            return;
        }


        Role role = event.getOption("role").getAsRole();
        dataManager.setIDData(event.getGuild().getId(), Columns.AUTO_ROLE, role.getId());
        event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "Every new user now gets this role automatically: " + role.getAsMention())).queue();

    }
}
