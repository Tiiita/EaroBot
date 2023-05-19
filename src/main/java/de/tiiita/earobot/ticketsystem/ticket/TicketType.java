package de.tiiita.earobot.ticketsystem.ticket;

/**
 * Created on März 19, 2023 | 18:57:58
 * (●'◡'●)
 */
public enum TicketType {


    SUPPORT("support", "Support", "supportTicketButton", "This is your **support** ticket!\n"
            + "Please tell us your problem. Try to add as many details as possible!"),

    MEDIA_APPLY("media-apply", "Media Apply", "mediaApplyButton", "This is your **media application** ticket!\n"
            + "Only you and our staff can see and write in it.\n\n"
            + "Please write your application here. You should give us following information:\n"
            + "- Your content link (twitch.tv... or youtube)\n"
            + "- Your Minecraft name and UUID. (https://namemc.com/)\n"
            + "- Do you have content of our network?\n"
            + "- Why should we accept _you_?\n"
            + "\nPlease be patient. This ticket will be closed\nwithout further reply if your application is rejected!"),

    MODERATOR_APPLY("mod-apply", "Moderator Apply", "moderatorApplyButton", "This is your **moderator application** ticket!\n"
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
            + "\nPlease be patient. This ticket will be closed\nwithout further reply if your application is rejected!"),

    UNBAN_APPLY("unban-appeal", "Unban Appeal", "unbanAppealButton", "This is your **unban appeal** ticket!\n"
            + "Only you and our staff can see and write in it.\n\n"
            + "Please write your appeal here. You should give us following information:\n"
            + "- Were you rightly or wrongly banned?\n"
            + "- The time of your ban\n"
            + "- Your account name\n"
            + "- Why should we unban you?\n"
            + "\nPlease be patient. This ticket will be closed\nwithout further reply if your appeal is rejected!");

    private final String ticketName;
    private final String selectionDisplay;
    private final String buttonId;
    private final String ticketContent;


    TicketType(String ticketName, String selectionDisplay, String buttonId, String ticketContent) {
        this.ticketName = ticketName;
        this.selectionDisplay = selectionDisplay;
        this.buttonId = buttonId;
        this.ticketContent = ticketContent;
    }


    public String getTicketContent() {
        return ticketContent;
    }

    public String getTicketName() {
        return ticketName;
    }

    public String getSelectionDisplay() {
        return selectionDisplay;
    }

    public String getButtonId() {
        return buttonId;
    }
}
