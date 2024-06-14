package de.tiiita.earobot.command.commands;

import de.tiiita.minecraft.bungee.config.BungeeConfig;
import de.tiiita.util.Collections;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

/**
 * Created on Mai 20, 2023 | 19:31:11
 * (●'◡'●)
 */
public class DoenerCommand extends ListenerAdapter {

    private final BungeeConfig config;

    public DoenerCommand(BungeeConfig config) {
        this.config = config;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("doener")) return;
        if (event.getMember() == null) return;

        String randomAnswer = Collections.getRandom(config.getStringList("doener-answers"));
        if (randomAnswer == null) {
            event.reply("Sorry, mir fällt so spontan nichts ein :/").queue();
            return;
        }

        event.reply(randomAnswer).queue();

    }

}
