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
    private final Configuration<Integer> notifyTime = new Configuration<>("notify_time", "SIGN", this, "Время в секундах, за которое игроки", "получат уведомление о свапе", "(-1 для отключения)");

    private BukkitRunnable runnable = new SwapRunnable();
    private BukkitRunnable notifyRunnable = new SwapNotifyRunnable();

    public void start(Player player) {
        long time = interval.getValue() * 20L;
        runnable.runTaskTimer(Plugin.getInstance(), time, time);

        if (notifyTime.getValue() != -1) {
            long toNotify = notifyTime.getValue() * 20L;
            notifyRunnable.runTaskTimer(Plugin.getInstance(), time - toNotify, time);
        }
    }

    public void stop() {
        runnable.cancel();
        runnable = new SwapRunnable();

        notifyRunnable.cancel();
        notifyRunnable = new SwapNotifyRunnable();
    }
}