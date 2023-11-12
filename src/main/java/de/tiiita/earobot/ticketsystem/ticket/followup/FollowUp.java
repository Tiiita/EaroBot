package de.tiiita.earobot.ticketsystem.ticket.followup;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Author: Tiiita
 * (●'◡'●)
 */
public class FollowUp {

    private final Consumer<TextChannel> action;

    public FollowUp(@NotNull Consumer<TextChannel> action) {
        this.action = action;
    }

    public void run(TextChannel channel) {
        action.accept(channel);
    }

}
