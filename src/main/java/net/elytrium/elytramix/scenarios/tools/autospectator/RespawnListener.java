package net.elytrium.elytramix.scenarios.tools.autospectator;

import net.elytrium.elytramix.Plugin;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

class RespawnListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onRespawn(PlayerDeathEvent event) {
        if (event.getEntity().getGameMode() == GameMode.SURVIVAL || event.getEntity().getGameMode() == GameMode.ADVENTURE) {
            // It is buggy
            new BukkitRunnable() {
                @Override
                public void run() {
                    event.getEntity().setGameMode(GameMode.SPECTATOR);
                }
            }.runTask(Plugin.getInstance());
        }
    }
}
