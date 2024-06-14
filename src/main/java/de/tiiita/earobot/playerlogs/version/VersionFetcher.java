package de.tiiita.earobot.playerlogs.version;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;

public class VersionFetcher {
    private final String URL = "https://raw.githubusercontent.com/PrismarineJS/minecraft-data/master/data/pc/common/protocolVersions.json";
    public CompletableFuture<String> getVersionName(int protocolId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(URL).openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(2500);


            } catch (IOException e) {
                ProxyServer.getInstance().getLogger().log(Level.SEVERE, "Unable to fetch version by id. Switching to backup versions...");
                return Versions.getVersionById(protocolId) + " (Backup)";
            }
        });
    }

}
