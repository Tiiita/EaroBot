package de.tiiita.earobot.util.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.md_5.bungee.api.plugin.Plugin;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class SQLite {

    private final Plugin plugin;
    private final String databaseFile;
    private final HikariConfig config = new HikariConfig();
    private HikariDataSource dataSource;

    public SQLite(Plugin plugin) {
        this.plugin = plugin;
        this.databaseFile = plugin.getDataFolder().getAbsolutePath() + File.separator + "database";

        connect();
        try {
            initDb();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            config.setJdbcUrl("jdbc:sqlite:" + databaseFile + ".db");
            config.setMaximumPoolSize(30);
            dataSource = new HikariDataSource(config);
            dataSource.getConnection();
            plugin.getLogger().log(Level.INFO, "Successfully connected to SQLite database!");
        } catch (SQLException e) {
            plugin.getLogger().log(Level.SEVERE, "Cannot connect to SQLite database!", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            if (dataSource.getConnection() != null) {
                dataSource.getConnection().close();
                dataSource.close();
                plugin.getLogger().log(Level.INFO, "The SQLite database connection has been closed!");
            }
        } catch (SQLException e) {
            plugin.getLogger().log(Level.INFO, "Cannot close SQLite database connection! Error: " + e.getMessage());
        }
    }

    private void initDb() throws SQLException, IOException {
        // check if database file exists
        Path path = Paths.get(databaseFile + ".db");
        if (!Files.exists(path)) {
            plugin.getLogger().log(Level.INFO, "Creating new SQLite database file: " + databaseFile);
            Files.createFile(path);
        }

        // read database setup file
        String setup;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("dbsetup.sql")) {
            setup = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not read SQLite database setup file.", e);
            throw e;
        }

        // split setup file into queries
        String[] queries = setup.split(";");

        // execute each query
        for (String query : queries) {
            if (query.isEmpty()) continue;
            try (Connection conn = dataSource.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.execute();
            }
        }
        plugin.getLogger().info("SQLite database setup complete.");
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
