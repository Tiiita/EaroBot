package de.tiiita.earobot.util.database;
import de.tiiita.earobot.util.Columns;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * @author tiiita_
 * Created on Januar 06, 2023 | 15:45:53
 * (●'◡'●)
 */
public class DataManager {

    private final SQLite database;
    private final String guildDataTable = "guild_data";
    private final String warnsTable = "guild_warns";
    public DataManager(SQLite database) {
        this.database = database;
    }


    public CompletableFuture<Boolean> isRegistered(String guildId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                    "select guildId from " + guildDataTable + " where guildId = ?;")) {
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
                    "INSERT INTO " + guildDataTable + "(guildId) VALUES(?);")) {
                statement.setString(1, guildId);
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Optional<String>> getIDData(String guildId, Columns column) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(
                    "select " + column + " from " + guildDataTable + " where guildId = ?;"
            )) {
                statement.setString(1, guildId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) return Optional.ofNullable(resultSet.getString(column.get()));
                return Optional.empty();
            } catch (SQLException e) {
                return Optional.empty();
            }
        });
    }
    public CompletableFuture<Void> setIDData(String guildId, Columns column, String valueId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(
                    "UPDATE " + guildDataTable + " SET " + column.get() + " = ? WHERE guildId = ?;"
            )) {
                statement.setString(1, valueId);
                statement.setString(2, guildId);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    /*
    public CompletableFuture<Optional<Integer>> getWarnData(String guildUserId, Columns column) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(
                    "select " + column + " from " + warnsTable + " where guild_user_id = ?;"
            )) {
                statement.setString(1, guildUserId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) return Optional.of(resultSet.getInt(column.get()));
                return Optional.empty();
            } catch (SQLException e) {
                return Optional.empty();
            }
        });
    }

    public CompletableFuture<Void> setWarnData(String guildUserId, Columns column, int valueId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(
                    "UPDATE " + warnsTable + " SET " + column.get() + " = ? WHERE guild_user_id = ?;"
            )) {
                statement.setInt(1, valueId);
                statement.setString(2, guildUserId);
                statement.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }


    public CompletableFuture<Boolean> isWarnUserRegistered(String guildUserId) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                    "select guild_user_id from " + warnsTable + " where guild_user_id = ?;")) {
                statement.setString(1, guildUserId);
                ResultSet result = statement.executeQuery();
                return result.next();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<Void> registerWarnUser(String guildUserId) {
        return CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection(); PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO " + warnsTable + "(guild_user_id) VALUES(?);")) {
                statement.setString(1, guildUserId);
                statement.execute();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public String getWarnDatabaseKey(Guild guild, UserSnowflake user) {
        return guild.getId() + ":" + user.getId();
    }*/
    private Connection getConnection() {
        try {
            return database.getDataSource().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
