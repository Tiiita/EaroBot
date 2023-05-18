package de.tiiita.earobot.util.database;

import java.sql.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author tiiita_
 * Created on Januar 06, 2023 | 15:45:53
 * (●'◡'●)
 */
public class DataManager {

    private final SQLite database;
    private final String table = "guild_data";

    public DataManager(SQLite database) {
        this.database = database;
    }


    public CompletableFuture<Boolean> isRegistered(String guildId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                    "select guildId from " + table + " where guildId = ?;")) {
                statement.setString(1, guildId);
                ResultSet result = statement.executeQuery();
                return result.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> registerGuild(String guildId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + table + "(guildId) VALUES(?);")) {
                statement.setString(1, guildId);
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Optional<String>> getIDData(String guildId, String column) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(
                    "select ? from " + table + " where guildId = ?;"
            )) {
                statement.setString(1, column);
                statement.setString(2, guildId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) return Optional.ofNullable(resultSet.getString(column));
                return Optional.empty();
            } catch (SQLException e) {
                return Optional.empty();
            }
        });
    }
    public CompletableFuture<Void> setIDData(String guildId, String column, String valueId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(
                    "UPDATE " + table + " SET ? = ? WHERE guildId = ?;"
            )) {
                statement.setString(1, column);
                statement.setString(2, valueId);
                statement.setString(3, guildId);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private Connection getConnection() {
        try {
            return database.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
