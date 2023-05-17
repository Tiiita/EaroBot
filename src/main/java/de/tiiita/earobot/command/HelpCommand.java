package de.tiiita.earobot.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * Created on Mai 13, 2023 | 20:01:35
 * (●'◡'●)
 */
public class HelpCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("help")) return;
        if (event.getMember() == null) return;
        if (!event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            event.reply("You do not have permissions for that!").setEphemeral(true).submit();
            return;
        }


        int guilds = event.getJDA().getGuilds().size();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Help");
        embedBuilder.setColor(Color.WHITE);
        embedBuilder.setDescription("Here are some important information!");
        embedBuilder.addField("Configure Bot", "The bot has _4_ main features: \n\n"
                + "- Send Welcome messages (1)\n"
                + "- Reacting to ideas (2)\n"
                + "- Announce Updates (3)\n"
                + "- Clear discord from raids (4)\n\n"
                + "(1) Go into the channel you want the messages and type **/set-welcome-channel**\n"
                + "Done? Every user join and leave will be send in this channel!\n\n"
                + "(2) You can choose the reaction channel with **/set-ideas-channel**\n\n"
                + "(3) You can announce updates in the channel you are in with **/update**\n\n"
                + "(4) With **/clear-spam** you can remove raid messages", false);

        embedBuilder.addField("Bot Information",
                "Author: Tiiita\n"
                + "Servers: " + guilds + "\n"
                + "Prefix: /",false);


        embedBuilder.addField("Help", "You can get help or report bugs by friend request **Tiiita#1044**!\n", false);
        embedBuilder.setImage(event.getJDA().getSelfUser().getAvatarUrl());
        event.replyEmbeds(embedBuilder.build()).setEphemeral(true).submit();
    }
}
