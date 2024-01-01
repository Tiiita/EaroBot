package de.tiiita.earobot.command.commands;
import de.tiiita.earobot.util.EmbedUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Created on März 16, 2023 | 21:16:02
 * (●'◡'●)
 */
public class MessageCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("message")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(EmbedUtil.getNoPermissionMessage()).setEphemeral(true).queue();
            return;
        }

        //Modal
        String input = event.getOption("input").getAsString();
        event.getChannel().sendMessage(input).queue();
        event.replyEmbeds(EmbedUtil.getSimpleEmbed(null, "Sent custom message...")).setEphemeral(true).queue();

    }
}
