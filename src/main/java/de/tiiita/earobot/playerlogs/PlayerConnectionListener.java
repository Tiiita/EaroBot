package de.tiiita.earobot.playerlogs;

import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

/**
 * Created on Mai 18, 2023 | 20:43:35
 * (●'◡'●)
 */
public class PlayerConnectionListener implements Listener {

    private final PlayerLogManager playerLogManager;

    public PlayerConnectionListener(PlayerLogManager playerLogManager) {
        this.playerLogManager = playerLogManager;
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        playerLogManager.broadcastLog(LogType.LOGIN, event.getPlayer());
    }


    @EventHandler
    public void onPlayerQuit(PlayerDisconnectEvent event) {
        playerLogManager.broadcastLog(LogType.LOGOUT, event.getPlayer());
    }
}
