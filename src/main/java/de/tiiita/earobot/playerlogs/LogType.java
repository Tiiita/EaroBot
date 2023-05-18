package de.tiiita.earobot.playerlogs;

/**
 * Created on Mai 18, 2023 | 20:18:38
 * (●'◡'●)
 */
public enum LogType {


    LOGIN("Login"),
    LOGOUT("Logout");

    private final String display;

    LogType(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }
}
