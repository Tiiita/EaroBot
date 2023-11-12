package de.tiiita.earobot.ticketsystem.ticket;

import de.tiiita.earobot.ticketsystem.ticket.followup.FollowUp;
import net.dv8tion.jda.api.entities.emoji.Emoji;

/**
 * Created on März 19, 2023 | 18:57:58
 * (●'◡'●)
 */
public enum TicketType {

    SUPPORT("Support", "supportTicket", Emoji.fromUnicode("🤝"), "Click to open a support ticket", "This is your **support** ticket!\n"
            + "Please tell us your problem. Try to add as many details as possible!"),

    BUG_REPORT("Bug Report", "bugReportTicket", Emoji.fromUnicode("⚙️"), "Click to report a bug", "This is your **bug report** ticket!\n\n"
            + "Please give us following information:\n"
            + "- How can we reproduce it?\n"
            + "- What did you do that the bug occurred?\n"
            + "- Any error messages or warnings you got.\n"
            + "- [OPTIONAL] Screenshots\n\n"
            + "Please be patient. We will answer you shortly!\n"
            + "Thank you for reporting a bug!"),

    APPLICATION("Application", "applicationTicket", Emoji.fromUnicode("📪"), "Click to navigate to the sub selection", "This is your application ticket!\n" +
            "Please select an application type...");


    private final String ticketChannelName;
    private final String displayName;
    private final String id;
    private final Emoji emoji;
    private final String description;
    private final String ticketContent;
    private FollowUp followUp;

    TicketType(String displayName, String id, Emoji emoji, String description, String ticketContent) {
        this.displayName = displayName;
        this.ticketChannelName = displayName.replaceAll(" ", "-").toLowerCase();
        this.id = id;
        this.emoji = emoji;
        this.description = description;
        this.ticketContent = ticketContent;
    }

    public void setFollowUp(FollowUp followUp) {
        this.followUp = followUp;
    }

    public String getTicketContent() {
        return ticketContent;
    }

    public String getTicketChannelName() {
        return ticketChannelName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Emoji getEmoji() {
        return emoji;
    }


    public String getDescription() {
        return description;
    }

    public FollowUp getFollowUp() {
        return followUp;
    }

    public String getId() {
        return id;
    }
}
