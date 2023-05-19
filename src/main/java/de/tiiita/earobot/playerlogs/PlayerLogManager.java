package de.tiiita.earobot.playerlogs;

import de.tiiita.earobot.playerlogs.version.Versions;
import de.tiiita.earobot.util.Config;
import de.tiiita.earobot.util.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Level;

/**
 * Created on Mai 17, 2023 | 20:03:33
 * (●'◡'●)
 */
public class PlayerLogManager {

    private final Config config;
    private final JDA jda;

    public PlayerLogManager(Config config, JDA jda) {
        this.config = config;
        this.jda = jda;
    }



    public void broadcastLog(LogType logType, ProxiedPlayer player) {
        String channelId = config.getString("player-log-channel");
        if (channelId == null) return;
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Player log channel not found... ID: " + channelId);
            return;
        }

        channel.sendMessageEmbeds(getLogMessage(logType, player)).submit();
    }

    private MessageEmbed getLogMessage(LogType logType, ProxiedPlayer player) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(logType.getDisplay());
        embedBuilder.setColor(Color.WHITE);
        embedBuilder.addField("Name", player.getName(), true);
        embedBuilder.addField("UUID", player.getUniqueId().toString(), true);
        embedBuilder.addField("Forge User", getIsForgeMsg(player), true);
        int protocolId = player.getPendingConnection().getVersion();
        embedBuilder.addField("Version", Versions.getVersionById(protocolId), true);

        String rawIP = player.getSocketAddress().toString().replaceAll("/", "");
        String[] displayIPArray = rawIP.split(":");
        String displayIP = displayIPArray[0];

        embedBuilder.addField("IP", displayIP, true);
        embedBuilder.addField("Online", "" + ProxyServer.getInstance().getOnlineCount(), true);
        embedBuilder.setFooter(TimeUtil.getTime(null));
        return embedBuilder.build();
    }

    private String getIsForgeMsg(ProxiedPlayer player) {
        if (player.isForgeUser()) {
            return "Yes";
        } else return "No";
    }
}
