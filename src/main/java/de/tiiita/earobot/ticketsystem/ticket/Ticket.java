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

/**
 * Created on März 17, 2023 | 13:54:52
 * (●'◡'●)
 */
public class Ticket {

    private Member creator;
    private String ticketChannelId;
    private TextChannel ticketChannel;
    private final JDA jda;
    private final TicketManager ticketManager;
    private final java.util.logging.Logger logger;
    private final TicketType ticketType;

    public Ticket(TicketType ticketType, JDA jda, TicketManager ticketManager) {
        this.jda = jda;
        this.ticketType = ticketType;
        this.ticketManager = ticketManager;
        this.logger = ProxyServer.getInstance().getLogger();
    }


    public void open(Member creator, Guild guild) {
        try {
            ticketManager.getTicketRole(guild.getId()).whenComplete((role, throwable) -> {
                if (role == null) {
                    return;
                }

                this.creator = creator;
                this.ticketChannel = guild.createTextChannel(ticketType.getTicketName() + "-" + creator.getUser().getAsTag())
                        .addPermissionOverride(creator, EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .complete();
                this.ticketChannelId = ticketChannel.getId();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setFooter(TimeUtil.getTime(null), jda.getSelfUser().getAvatarUrl());
                embed.setColor(Color.WHITE);

                embed.setTitle(ticketType.getSelectionDisplay());
                embed.setDescription(ticketType.getTicketContent());
                embed.setThumbnail(jda.getSelfUser().getAvatarUrl());
                ticketChannel.sendMessage(role.getAsMention()).submit();
                ticketChannel.sendMessageEmbeds(embed.build())
                        .addActionRow(Button.danger("ticketCloseButton", "Close"))
                        .submit();


            });
        } catch (NullPointerException ignore) {}
    }

    public void close() {
        if (this.ticketChannelId == null) {
            return;
        }

        TextChannel ticketChannel = jda.getTextChannelById(ticketChannelId);
        if (ticketChannel == null) return;
        ticketChannel.delete().submit();
        creator.getUser().openPrivateChannel().submit().whenComplete((privateChannel, throwable) -> {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setFooter(TimeUtil.getTime(null), jda.getSelfUser().getAvatarUrl());
            embed.setColor(Color.WHITE);
            embed.setTitle("Your Ticket");
            embed.setDescription("Your ticket has been closed!\n" +
                    "You can now create new tickets.");
            embed.addField("Ticket Type", "Your Ticket Type: " + ticketType.getSelectionDisplay(), false);
            embed.setThumbnail(jda.getSelfUser().getAvatarUrl());
            privateChannel.sendMessageEmbeds(embed.build()).submit();
        });
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
    public String getTicketChannelId() {
        return ticketChannelId;
    }
}
