package net.elytrium.elytramix.scenarios.gameplay.randomgive;

import net.elytrium.elytramix.Plugin;
import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RandomGive extends Scenario {
    public RandomGive() {
        super("Рандомная выдача предметов", "random_give", "DIAMOND", "scenario","Выдает определенное количество рандомных блоков", "за определенное время и из определенного", "списка (по умолчанию все блоки и предметы)");
        addConfig(block_limit);
        addConfig(interval);
        addConfig(middle_multiplier);
    }

    private final Configuration<Integer> block_limit = new Configuration<>("block_limit", "SKULL_ITEM", this, "Количество блоков за раз");
    private final Configuration<Integer> interval = new Configuration<>("interval", "WATCH", this, "Интервал выдачи блоков в секундах");
    private final Configuration<Boolean> middle_multiplier = new Configuration<>("middle_multiplier", "COMPASS", this, "Выдавать ли больше блоков", "в центре зоны");

    private BukkitRunnable runnable = new GiveRunnable(this);

    public void start(Player player) {
        runnable.runTaskTimer(Plugin.getInstance(), 0L, interval.getValue() * 20L);
    }

    public void stop() {
        runnable.cancel();
        runnable = new GiveRunnable(this);
    }

    public int getLimit(){
        return block_limit.getValue();
    }

    public boolean isMiddleMultiplier(){
        return middle_multiplier.getValue();
    }
}
