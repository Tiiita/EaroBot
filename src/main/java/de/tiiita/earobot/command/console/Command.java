package de.tiiita.earobot.command.console;

/**
 * Created on Mai 19, 2023 | 18:23:38
 * (●'◡'●)
 */
public abstract class Command {

    private final String name;
    private Runnable action;
    private final String explanation ;

    public Command(String name, String explanation) {
        this.name = name;
        this.explanation = explanation;
    }

    public String getName() {
        return name;
    }

    public void setActionOnRun(Runnable action) {
        this.action = action;
    }

    public String getExplanation() {
        return explanation;
    }

    public Runnable getAction() {
        return action;
    }
}
