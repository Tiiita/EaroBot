package de.tiiita.earobot;

import de.tiiita.earobot.command.CommandManager;
import de.tiiita.earobot.listener.GuildJoinListener;
import de.tiiita.earobot.listener.MessageReceiveListener;
import de.tiiita.earobot.listener.UserWelcomeListener;
import de.tiiita.earobot.playerlogs.PlayerConnectionListener;
import de.tiiita.earobot.playerlogs.PlayerLogManager;
import de.tiiita.earobot.ticketsystem.TicketActionListener;
import de.tiiita.earobot.ticketsystem.TicketManager;
import de.tiiita.earobot.ticketsystem.ticket.followup.FollowUpMenuListener;
import de.tiiita.earobot.util.Config;
import de.tiiita.earobot.util.database.DataManager;
import de.tiiita.earobot.util.database.SQLite;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public final class EaroBot extends Plugin {

    private JDA jda;
    private DataManager dataManager;
    private TicketManager ticketManager;
    private SQLite database;
    private Config config;
    private CommandManager commandManager;
    private PlayerLogManager playerLogManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().log(Level.INFO, "The Discord bot is starting...");
        loadConfig("config.yml");
        this.config = new Config("config.yml", getDataFolder(), this);
        this.database = new SQLite(this);
        this.dataManager = new DataManager(database);
        setupBot(config.getString("token"));
        this.ticketManager = new TicketManager(jda, dataManager);
        this.commandManager = new CommandManager(this);
        this.commandManager.registerCommands();

        this.playerLogManager = new PlayerLogManager(config, jda);


        registerBungeeListener();
        getLogger().log(Level.INFO, "Done! Discord bot is ready!");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        //false means offline
        if (database != null) {
            database.close();
        }

        ticketManager.closeAllTickets();
    }


    private void registerBungeeListener() {
        PluginManager pm = getProxy().getPluginManager();

        pm.registerListener(this, new PlayerConnectionListener(playerLogManager));
    }

    private void setupBot(String token) {
        connectToDiscord(token);
        registerGuilds().whenComplete((unused, throwable) -> {
            registerListener();
        });
    }

    private CompletableFuture<Void> registerGuilds() {
        List<CompletableFuture<Void>> registrationFutures = new ArrayList<>();
        Set<String> registeredGuildIds = new HashSet<>();

        for (Guild guild : jda.getGuilds()) {

            CompletableFuture<List<Member>> loadMembersFuture = CompletableFuture.supplyAsync(() -> guild.loadMembers().get());

            CompletableFuture<Void> processMembersFuture = loadMembersFuture.thenAcceptAsync(members -> {
                /*for (Member guildUser : members) {
                    CompletableFuture<Void> registerWarnFuture = dataManager.registerWarnUser(dataManager.getWarnDatabaseKey(guild, guildUser));
                    registrationFutures.add(registerWarnFuture);
                }*/
                registeredGuildIds.add(guild.getId());
            });

            registrationFutures.add(processMembersFuture);
        }

        for (Guild guild : jda.getGuilds()) {
            if (!registeredGuildIds.contains(guild.getId())) {
                CompletableFuture<Void> registerGuildFuture = dataManager.registerGuild(guild.getId());
                registrationFutures.add(registerGuildFuture);
            }
        }

        return CompletableFuture.allOf(registrationFutures.toArray(new CompletableFuture[0]));
    }


    private void registerListener() {
        jda.addEventListener(new GuildJoinListener(commandManager));
        jda.addEventListener(new MessageReceiveListener(dataManager));
        if (config.getBoolean("welcome-messages")) jda.addEventListener(new UserWelcomeListener(dataManager, jda));
        jda.addEventListener(new TicketActionListener(ticketManager, dataManager));
        //jda.addEventListener(new ButtonListener(dataManager));
        jda.addEventListener(new FollowUpMenuListener(ticketManager));
    }

    private void connectToDiscord(String token) {
        //Currently buggy, commend out
        /*if (alreadyOnline()) {
            getLogger().log(Level.SEVERE, "*** Another host is already connected to this bot ***");
            getLogger().log(Level.SEVERE, "*** Bot will NOT connect, plugin will be disabled! ***");
            this.onDisable();
            return;
        }*/
        try {
            this.jda = JDABuilder.createDefault(token)
                    .setEnabledIntents(Arrays.asList(GatewayIntent.values()))
                    .setBulkDeleteSplittingEnabled(false)
                    .setActivity(Activity.playing(config.getString("bot-activity")))
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

    private boolean alreadyOnline() {
        String token = config.getString("token");
        String id = config.getString("user-id");

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://discord.com/api/v10/users/" + id)
                .header("Authorization", "Bot " + token)
                .build();

        boolean online = false;
        try {
            Response response = client.newCall(request).execute();
            int statusCode = response.code();

            //Status code 200 means bot is online...
            online = statusCode == 200;
            response.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return online;
    }


    public DataManager getDataManager() {
        return dataManager;
    }

    public SQLite getDatabase() {
        return database;
    }

    public Config getConfig() {
        return config;
    }

    public JDA getJda() {
        return jda;
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }


    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PlayerLogManager getPlayerLogManager() {
        return playerLogManager;
    }
}
