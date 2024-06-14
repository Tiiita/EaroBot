package de.tiiita.earobot.listener;

import de.tiiita.earobot.util.Columns;
import de.tiiita.earobot.util.database.DataManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * Created on März 16, 2023 | 20:48:46
 * (●'◡'●)
 */
public class UserWelcomeListener extends ListenerAdapter {

    private final DataManager dataManager;
    private final JDA jda;

    public UserWelcomeListener(DataManager dataManager, JDA jda) {
        this.dataManager = dataManager;
        this.jda = jda;
    }

    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String guildId = event.getGuild().getId();
        Objects.requireNonNull(getWelcomeChannel(guildId)).whenComplete((welcomeChannel, throwable) -> {
            User user = event.getUser();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.WHITE);
            embed.setThumbnail(user.getAvatarUrl());
            embed.setTitle("New Member");
            embed.setDescription("HeyHo👋 " + user.getAsMention() + ", welcome to this Discord!");
            welcomeChannel.sendMessageEmbeds(embed.build()).queue();
        });


        dataManager.getIDData(guildId, Columns.AUTO_ROLE).whenComplete((autoRoleId, throwable) -> {
            if (!autoRoleId.isPresent()) return;

            Role autoRole = event.getGuild().getRoleById(autoRoleId.get());
            if (autoRole == null) return;

            event.getGuild().addRoleToMember(event.getMember(), autoRole).queue();
        });
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        String guildId = event.getGuild().getId();
        Objects.requireNonNull(getWelcomeChannel(guildId)).whenComplete((welcomeChannel, throwable) -> {
            if (welcomeChannel == null) return;

            User user = event.getUser();
            EmbedBuilder embed = new EmbedBuilder();
            embed.setColor(Color.WHITE);
            embed.setThumbnail(user.getAvatarUrl());
            embed.setTitle("Left");
            embed.setDescription("**" + user.getAsTag() + "** left the server!");
            welcomeChannel.sendMessageEmbeds(embed.build()).queue();
        });
    }


    @Nullable
    private CompletableFuture<TextChannel> getWelcomeChannel(String guildId) {
        return dataManager.getIDData(guildId, Columns.WELCOME_CHANNEL).handle((optionalId, throwable) -> {
            return optionalId.map(jda::getTextChannelById).orElse(null);
        });
    }
}
