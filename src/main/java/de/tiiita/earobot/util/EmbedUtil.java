package de.tiiita.earobot.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;

/**
 * Created on April 13, 2023 | 15:17:20
 * (●'◡'●)
 */
public class EmbedUtil {


    public static MessageEmbed getSimpleEmbed(String text) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(text);
        embedBuilder.setColor(Color.WHITE);
        return embedBuilder.build();
    }

    public static Color getDiscordBackgroundColor() {
        return new Color(43, 45, 49);

    }

    public static MessageEmbed getSimpleEmbed(Color color, String text) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(text);
        if (color == null) {
            embedBuilder.setColor(getDiscordBackgroundColor());
        } else embedBuilder.setColor(color);
        return embedBuilder.build();
    }

    public static MessageEmbed getNoPermissionMessage() {
        return getSimpleEmbed(getRed(), "You aren't allowed to do this!");
    }

    public static MessageEmbed getError() {
        return getSimpleEmbed(getRed(),"There was an error. Please report it by opening a ticket!");
    }

    public static Color getRed() {
        return new Color(255, 58, 61);

    }
}
