package de.tiiita.earobot.playerlogs;

import de.tiiita.earobot.util.TimeUtil;
import de.tiiita.minecraft.bungee.VersionNameFetcher;
import de.tiiita.minecraft.bungee.config.BungeeConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Created on Mai 17, 2023 | 20:03:33
 * (●'◡'●)
 */
public class PlayerLogManager {

    private final BungeeConfig config;
    private final JDA jda;

    public PlayerLogManager(BungeeConfig config, JDA jda) {
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

        getLogMessage(logType, player).whenComplete((messageEmbed, throwable) -> {
            channel.sendMessageEmbeds(messageEmbed).queue();
        });
    }

    private CompletableFuture<MessageEmbed> getLogMessage(LogType logType, ProxiedPlayer player) {
        return CompletableFuture.supplyAsync(() -> {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(logType.getDisplay());
            embedBuilder.setColor(Color.WHITE);
            embedBuilder.addField("Name", player.getName(), true);
            embedBuilder.addField("UUID", player.getUniqueId().toString(), true);
            embedBuilder.addField("Forge User", getIsForgeMsg(player), true);

            int protocolId = player.getPendingConnection().getVersion();
            embedBuilder.addField("Version", VersionNameFetcher.getVersionName(protocolId), true);

            String rawIP = player.getSocketAddress().toString().replaceAll("/", "");
            String[] displayIPArray = rawIP.split(":");
            String displayIP = displayIPArray[0];

            embedBuilder.addField("IP", displayIP, true);
            embedBuilder.addField("Online", "" + ProxyServer.getInstance().getOnlineCount(), true);
            embedBuilder.setFooter(TimeUtil.getTime(null));
            return embedBuilder.build();
        });
    }

    private String getIsForgeMsg(ProxiedPlayer player) {
        if (player.isForgeUser()) {
            return "Yes";
        } else return "No";
    }
}
