package net.elytrium.elytramix.scenarios.gameplay.apocalypse;

import net.elytrium.elytramix.Plugin;
import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import net.elytrium.elytramix.utils.Border;
import net.elytrium.elytramix.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Apocalypse extends Scenario {
    private final Configuration<Integer> period = new Configuration<>("period","WATCH", this, "Время в тиках между", "спауном метеоров");
    private BukkitRunnable task;

    public Apocalypse() {
        super("Апокалипсис", "apocalypse", "MAGMA", "scenario","Запускает метеоритный дождь");
        addListener(new MeteorFallListener());
        addListener(new MeteorSpawner());
        addConfig(period);
    }

    @Override
    public void start(Player player) {
        updateTask();
    }

    @Override
    public void stop() {
        task.cancel();
        task = null;
    }

    public class MeteorSpawnerRunnable extends BukkitRunnable {
        private final Random random = new Random();

        @Override
        public void run() {
            if (!isStarted())
                return;
            Location location = Border.getRandomLocInBorder(Bukkit.getWorlds().get(0), random);
            location.setY(255);
            Block block = location.getBlock();
            if (block.getType() == Material.AIR) {
                Location add = block.getLocation().clone().add(0.5D, 0.0D, 0.5D);
                new Meteor(block.getWorld().spawnFallingBlock(add, new MaterialData(ItemUtils.getMaterial("MAGMA"))));
            } else block.getWorld().createExplosion(block.getLocation(), 5);
            updateTask();
        }
    }

    private void updateTask() {
        task = new MeteorSpawnerRunnable();
        task.runTaskLater(Plugin.getInstance(), period.getValue());
    }
}
