package de.tiiita.earobot.ticket.ticket;

/**
 * Created on März 19, 2023 | 18:57:58
 * (●'◡'●)
 */
public enum TicketType {


    SUPPORT("support", "Support", "supportTicketButton"),
    MEDIA_APPLY("media-apply", "Media Apply", "mediaApplyButton"),
    MODERATOR_APPLY("mod-apply", "Moderator Apply", "moderatorApplyButton"),
    UNBAN_APPLY("unban-appeal", "Unban Appeal", "unbanAppealButton");

    private final String ticketDisplay;
    private final String selectionDisplay;
    private final String buttonId;

    TicketType(String ticketDisplay, String selectionDisplay, String buttonId) {
        this.ticketDisplay = ticketDisplay;
        this.selectionDisplay = selectionDisplay;
        this.buttonId = buttonId;
    }

    public String getTicketDisplay() {
        return ticketDisplay;
    }

    public String getSelectionDisplay() {
        return selectionDisplay;
    }

    public String getButtonId() {
        return buttonId;
    }
}
