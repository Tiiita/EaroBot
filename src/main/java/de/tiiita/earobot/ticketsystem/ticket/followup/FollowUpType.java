package de.tiiita.earobot.ticketsystem.ticket.followup;

import net.dv8tion.jda.api.entities.emoji.Emoji;

/**
 * Author: Tiiita
 * (●'◡'●)
 */
public enum FollowUpType {
    //APPLICATION
    MOD("Moderator", "discordModFollowUp", Emoji.fromUnicode("👀"), "mod-", "Select discord mod as type!"),
    BUILDER("Builder", "builderFollowUp", Emoji.fromUnicode("🏗️"), "builder-", "Select minecraft builder type!"),
    JAVA_DEVELOPER("Java Developer", "javaDeveloperFollowUp", Emoji.fromUnicode("💻"), "java-dev-", "Select the java developer type!"),
    WEB_DEVELOPER("Web Developer", "wevDeveloperFollowUp", Emoji.fromUnicode("💻"), "web-dev-", "Select the web developer type!");

    private final String displayName;
    private final String id;
    private final Emoji emoji;
    private final String ticketSubName;
    private final String description;

    FollowUpType(String displayName, String id, Emoji emoji, String ticketSubName, String description) {
        this.displayName = displayName;
        this.id = id;
        this.emoji = emoji;
        this.ticketSubName = ticketSubName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getId() {
        return id;
    }

    public Emoji getEmoji() {
        return emoji;
    }

    public String getDescription() {
        if (description.toCharArray().length >= 50) return "Error - To many Character!";
        return description;
    }

    public String getTicketSubName() {
        return ticketSubName;
    }
}
