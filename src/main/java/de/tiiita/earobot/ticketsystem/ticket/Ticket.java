package de.tiiita.earobot.ticketsystem.ticket;

import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.util.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.md_5.bungee.api.ProxyServer;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.EnumSet;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

/**
 * Created on März 17, 2023 | 13:54:52
 * (●'◡'●)
 */
public class Ticket {

    private Member creator;
    private String ticketChannelId;
    private TextChannel ticketChannel;
    private final JDA jda;
    private Member closer;
    private final TicketManager ticketManager;
    private final java.util.logging.Logger logger;
    private final TicketType ticketType;

    public Ticket(TicketType ticketType, JDA jda, TicketManager ticketManager) {
        this.jda = jda;
        this.ticketType = ticketType;
        this.ticketManager = ticketManager;
        this.logger = ProxyServer.getInstance().getLogger();
    }


    public CompletableFuture<Void> open(Member creator, Guild guild) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ticketManager.getTicketRole(guild.getId()).thenAcceptAsync((role) -> {
            if (role == null) {
                future.complete(null);
                return;
            }

            this.creator = creator;
            guild.createTextChannel(ticketType.getTicketName() + "-" + creator.getUser().getAsTag())
                    .addPermissionOverride(creator, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .submit()
                    .thenAcceptAsync((channel) -> {
                        this.ticketChannel = channel;
                        this.ticketChannelId = channel.getId();

                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setFooter(TimeUtil.getTime(null), jda.getSelfUser().getAvatarUrl());
                        embed.setColor(Color.WHITE);

                        embed.setTitle(ticketType.getSelectionDisplay());
                        embed.setDescription(ticketType.getTicketContent());
                        embed.setThumbnail(jda.getSelfUser().getAvatarUrl());
                        channel.sendMessage(role.getAsMention()).submit();
                        channel.sendMessageEmbeds(embed.build())
                                .addActionRow(Button.danger("ticketCloseButton", "Close"))
                                .submit();
                        future.complete(null);
                    });

        });

        return future;
    }

    public CompletableFuture<Void> close() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (this.ticketChannelId == null) {
            future.complete(null);
            return future;
        }

        TextChannel ticketChannel = jda.getTextChannelById(ticketChannelId);
        if (ticketChannel == null) {
            future.complete(null);
            return future;
        }

        ticketChannel.delete().submit().thenAcceptAsync((unused) -> {
            creator.getUser().openPrivateChannel().submit().whenCompleteAsync((privateChannel, throwable) -> {
                EmbedBuilder embed = new EmbedBuilder();
                embed.setFooter("EaroBot Ticket System", jda.getSelfUser().getAvatarUrl());
                embed.setColor(Color.WHITE);
                embed.setTitle("Your Ticket");
                embed.setDescription("Your ticket has been closed!\n" +
                        "You can now create new tickets.");
                embed.addField("Ticket Type", "Your Ticket Type: " + ticketType.getSelectionDisplay(), false);
                String closerValue;
                if (closer != null) {
                    closerValue = closer.getUser().getName();
                } else closerValue = "Automatic Ticket Closing";
                embed.addField("Ticket Closer", "Closer: " + closerValue, false);
                embed.addField("Closing Time", "Time: " + TimeUtil.getTime("H:mm a"), false);

                int messages = ticketChannel.getHistory().size();
                embed.addField("Sent Messages", "Messages: " + messages, false);
                embed.addField("Server", ticketChannel.getGuild().getName(), false);
                embed.setThumbnail(jda.getSelfUser().getAvatarUrl());
                privateChannel.sendMessageEmbeds(embed.build())
                        .submit()
                        .thenAcceptAsync((message) -> future.complete(null))
                        .exceptionally((ex) -> {
                            future.completeExceptionally(ex);
                            return null;
                        });
            });
        });

        return future;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    @Nullable
    public Member getCreator() {
        return creator;
    }

    @Nullable
    public TextChannel getTicketChannel() {
        return ticketChannel;
    }

    @Nullable
    public Member getCloser() {
        return closer;
    }

    public void setCloser(Member closer) {
        this.closer = closer;
    }

    @Nullable
    public String getTicketChannelId() {
        return ticketChannelId;
    }
}