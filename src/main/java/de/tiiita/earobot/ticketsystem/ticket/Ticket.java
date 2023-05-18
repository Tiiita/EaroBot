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
                    ProxyServer.getInstance().getLogger().log(Level.SEVERE, "NULL!");

                    return;
                }

                this.creator = creator;
                this.ticketChannel = guild.createTextChannel(ticketType.getTicketDisplay() + "-" + creator.getUser().getAsTag())
                        .addPermissionOverride(creator, EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
                        .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                        .complete();
                this.ticketChannelId = ticketChannel.getId();

                EmbedBuilder embed = new EmbedBuilder();
                embed.setFooter(TimeUtil.getTime(null), jda.getSelfUser().getAvatarUrl());
                embed.setColor(Color.WHITE);
                if (ticketType == TicketType.SUPPORT) {
                    embed.setTitle("Support");
                } else embed.setTitle("Rank Application");


                switch (ticketType) {
                    case MEDIA_APPLY: {
                        embed.setDescription("This is your **media application** ticket!\n"
                                + "Only you and our staff can see and write in it.\n\n"
                                + "Please write your application here. You should give us following information:\n"
                                + "- Your content link (twitch.tv... or youtube)\n"
                                + "- Your Minecraft name and UUID. (https://namemc.com/)\n"
                                + "- Do you have content of our network?\n"
                                + "- Why should we accept _you_?\n"
                                + "\nPlease be patient. This ticket will be closed\nwithout further reply if your application is rejected!");
                        break;
                    }

                    case MODERATOR_APPLY: {
                        embed.setDescription("This is your **moderator application** ticket!\n"
                                + "Only you and our staff can see and write in it.\n\n"
                                + "Please write your application here. You should give us following information:\n"
                                + "- Name and age (Do not lie!)\n"
                                + "- Your Minecraft name and UUID. (https://namemc.com/)\n"
                                + "- Do you know how to detect cheats?\n"
                                + "- Have you been moderator on another server?\n"
                                + "- How did you get to know our server?\n"
                                + "- Why do you want to be moderator on our network?\n"
                                + "- Why should we accept _you_?\n"
                                + "- How many hours can you be online in a week?\n"
                                + "\nPlease be patient. This ticket will be closed\nwithout further reply if your application is rejected!");
                        break;
                    }

                    case UNBAN_APPLY: {
                        embed.setDescription("This is your **unban appeal** ticket!\n"
                                + "Only you and our staff can see and write in it.\n\n"
                                + "Please write your application here. You should give us following information:\n"
                                + "- Were you rightly or wrongly banned?\n"
                                + "- The time of your ban\n"
                                + "- Your account name\n"
                                + "- Why should we unban you?\n"
                                + "\nPlease be patient. This ticket will be closed\nwithout further reply if your appeal is rejected!");

                        break;
                    }
                    case SUPPORT: {
                        embed.setDescription("This is your **support** ticket!\n"
                                + "Please tell us your problem. Try to add as many details as possible!");
                        break;
                    }
                }


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
