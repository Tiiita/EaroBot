package de.tiiita.earobot;

import de.tiiita.earobot.command.CommandManager;
import de.tiiita.earobot.command.commands.*;
import de.tiiita.earobot.command.console.ConsoleCommandManager;
import de.tiiita.earobot.listener.GuildJoinListener;
import de.tiiita.earobot.listener.MessageReceiveListener;
import de.tiiita.earobot.listener.UserJoinLeaveListener;
import de.tiiita.earobot.playerlogs.PlayerConnectionListener;
import de.tiiita.earobot.playerlogs.PlayerLogManager;
import de.tiiita.earobot.ticketsystem.command.SendTicketMessageCommand;
import de.tiiita.earobot.ticketsystem.command.SetTicketRoleCommand;
import de.tiiita.earobot.ticketsystem.TicketButtonListener;
import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.util.database.DataManager;
import de.tiiita.earobot.util.database.SQLite;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import de.tiiita.earobot.util.Config;
import net.md_5.bungee.api.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class EaroBot extends Plugin {
    private JDA jda;
    private Config config;
    private DataManager dataManager;
    private SQLite database;
    private TicketManager ticketManager;
    private ConsoleCommandManager consoleCommandManager;
    private PlayerLogManager playerLogManager;
    private CommandManager commandManager;

    private final Collection<Command> commands = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().log(Level.INFO, "The Discord bot is starting...");
        loadConfig("config.yml");
        this.config = new Config("config.yml", getDataFolder(), this);
        this.database = new SQLite(this);
        this.dataManager = new DataManager(database);


        setupBot(config.getString("token"), config.getString("bot-activity"));

        this.ticketManager = new TicketManager(jda, dataManager);
        this.playerLogManager = new PlayerLogManager(config, jda);
        this.consoleCommandManager = new ConsoleCommandManager(this);
        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommands();
        getProxy().getPluginManager().registerListener(this, new PlayerConnectionListener(playerLogManager));
        getProxy().getPluginManager().registerListener(this, consoleCommandManager);
        getLogger().log(Level.INFO, "Done! Discord bot is ready!");
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        ticketManager.closeAllTickets();
        database.close();
        jda.shutdown();
        this.jda = null;
        getLogger().log(Level.INFO, "Shutting down...");
    }

    private void setupBot(String token, String activity) {
        connectToDiscord(token, activity);
        registerGuilds().whenComplete((unused, throwable) -> {
            registerListener();
        });
    }

    private CompletableFuture<Void> registerGuilds() {
        List<CompletableFuture<Void>> registrationFutures = new ArrayList<>();

        for (Guild guild : jda.getGuilds()) {
            CompletableFuture<Boolean> isRegisteredFuture = dataManager.isRegistered(guild.getId());
            CompletableFuture<Void> registerFuture = isRegisteredFuture.thenComposeAsync(isRegistered -> {
                if (!isRegistered) {
                    return dataManager.registerGuild(guild.getId());
                }
                return CompletableFuture.completedFuture(null);
            });
            registrationFutures.add(registerFuture);
        }
        return CompletableFuture.allOf(registrationFutures.toArray(new CompletableFuture[0]));
    }


    private void registerListener() {
        jda.addEventListener(new GuildJoinListener(commandManager));
        jda.addEventListener(new UserJoinLeaveListener(dataManager, jda));
        jda.addEventListener(new MessageReceiveListener(dataManager));
        jda.addEventListener(new TicketButtonListener(ticketManager));
    }

    private void connectToDiscord(String token, String activity) {
        try {
            this.jda = JDABuilder.createDefault(token)
                    .setEnabledIntents(Arrays.asList(GatewayIntent.values()))
                    .setBulkDeleteSplittingEnabled(false)
                    .setActivity(Activity.playing(activity))
                    .setStatus(OnlineStatus.ONLINE)
                    .build();
        } catch (InvalidTokenException e) {
            getLogger().log(Level.SEVERE, "*** The Token setup in the config is not valid. Cannot connect to the bot! ***");
            return;
        }
        try {
            this.jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadConfig(String name) {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }

        File file = new File(this.getDataFolder(), name);

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream(name)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public DataManager getDataManager() {
        return dataManager;
    }

    public JDA getJda() {
        return jda;
    }

    public Config getConfig() {
        return config;
    }
}
