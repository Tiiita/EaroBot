package de.tiiita.earobot.ticketsystem.ticket;
import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.ticketsystem.ticket.followup.FollowUp;
import de.tiiita.earobot.util.EmbedUtil;
import de.tiiita.earobot.util.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Created on März 17, 2023 | 13:54:52
 * (●'◡'●)
 */
public class Ticket {

    private Member creator;
    private String ticketChannelId;
    private TextChannel ticketChannel;
    private FollowUp followUp;
    private final JDA jda;
    private User claimer;
    private Member closer;
    private final TicketManager ticketManager;

    private final TicketType ticketType;

    public Ticket(TicketType ticketType, JDA jda, TicketManager ticketManager) {
        this.jda = jda;
        this.ticketType = ticketType;
        this.followUp = ticketType.getFollowUp();
        this.ticketManager = ticketManager;
    }


    public CompletableFuture<Void> open(Member creator, Guild guild) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ticketManager.getTicketRole(guild.getId()).thenAcceptAsync((role) -> {
            if (role == null) {
                future.complete(null);
                return;
            }

            this.creator = creator;
            guild.createTextChannel(ticketType.getTicketChannelName() + "-" + creator.getUser().getName())
                    .addPermissionOverride(creator, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(role, EnumSet.of(Permission.VIEW_CHANNEL), null)
                    .addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
                    .submit()
                    .thenAcceptAsync((channel) -> {
                        this.ticketChannel = channel;
                        this.ticketChannelId = channel.getId();

                        EmbedBuilder embed = new EmbedBuilder();
                        embed.setFooter(TimeUtil.getTime(null), jda.getSelfUser().getAvatarUrl());
                        embed.setColor(EmbedUtil.getDiscordBackgroundColor());

                        embed.setTitle(ticketType.getDisplayName());
                        embed.setDescription(ticketType.getTicketContent());
                        embed.setThumbnail(jda.getSelfUser().getAvatarUrl());
                        channel.sendMessage(role.getAsMention()).queue();
                        channel.sendMessageEmbeds(embed.build())
                                .addActionRow(Button.success("ticketClaimButton", "Claim"),
                                        Button.danger("ticketCloseButton", "Close"))
                                .queue();


                        if (followUp != null) {
                            followUp.run(ticketChannel);
                        }
                        future.complete(null);
                    });
        });
        return future;
    }

    public CompletableFuture<Void> close() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        if (this.ticketChannel == null) {
            future.complete(null);
            return future;
        }

        CompletableFuture<List<Message>> messagesFuture = ticketChannel.getHistory().retrievePast(100).submit();

        ticketChannel.delete().submit().thenAcceptAsync((unused) -> {
            creator.getUser().openPrivateChannel().submit().whenCompleteAsync((privateChannel, throwable) -> {

                messagesFuture.whenComplete((messages, throwable1) -> {
                    int sentMessages = (int) messages.stream()
                            .filter(message -> !message.getAuthor().isBot()).count();

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setFooter("Ticket System", jda.getSelfUser().getAvatarUrl());
                    embed.setColor(EmbedUtil.getDiscordBackgroundColor());
                    embed.setTitle("Your Ticket");
                    embed.setDescription("Your ticket has been closed!\n" +
                            "You can now create new tickets.");
                    embed.addField("Ticket Type", ticketType.getDisplayName(), false);
                    String closerValue;
                    if (closer != null) {
                        closerValue = closer.getUser().getName();
                    } else closerValue = "Automatic Ticket Closing";
                    embed.addField("Ticket Closer", closerValue, false);
                    embed.addField("Closing Time", TimeUtil.getTime("HH:mm"), false);

                    embed.addField("Sent Messages", sentMessages + " (max. 100)", false);
                    embed.setThumbnail(jda.getSelfUser().getAvatarUrl());
                    privateChannel.sendMessageEmbeds(embed.build())
                            .submit()
                            .thenAcceptAsync((message) -> {
                                future.complete(null);
                            })
                            .exceptionally((ex) -> {
                                future.completeExceptionally(ex);
                                return null;
                            });
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

    @Nullable
    public User getClaimer() {
        return claimer;
    }

    public void setClaimer(User claimer) {
        this.claimer = claimer;
    }

    public FollowUp getFollowUp() {
        return followUp;
    }
}