package net.elytrium.elytramix.scenarios.gameplay.playerswap;

import net.elytrium.elytramix.Plugin;
import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSwap extends Scenario {
    public PlayerSwap() {
        super("Свап игроков", "player_swap", "LEATHER_BOOTS", "scenario","Меняет местоположение игроков", "местами по кд");
        addConfig(interval);
    }

    private final Configuration<Integer> interval = new Configuration<>("interval", "WATCH", this, "Интервал телепортации в секундах");

    private BukkitRunnable runnable = new SwapRunnable();

    public void start(Player player) {
        runnable.runTaskTimer(Plugin.getInstance(), 0L, interval.getValue() * 20L);
    }

    public void stop() {
        runnable.cancel();
        runnable = new SwapRunnable();
    }
}
