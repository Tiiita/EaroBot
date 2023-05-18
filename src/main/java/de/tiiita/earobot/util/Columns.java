package de.tiiita.earobot.util;

/**
 * Created on Mai 18, 2023 | 17:27:53
 * (●'◡'●)
 */
public enum Columns {

    WELCOME_CHANNEL("welcome_channel"),
    IDEAS_CHANNEL("ideas_channel"),
    TICKET_ROLE("ticket_role");

    private final String databaseColumn;

    Columns(String databaseColumn) {
        this.databaseColumn = databaseColumn;
    }

    public String get() {
        return databaseColumn;
    }
}
