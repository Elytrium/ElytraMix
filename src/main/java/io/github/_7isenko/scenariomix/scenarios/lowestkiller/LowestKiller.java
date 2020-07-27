package io.github._7isenko.scenariomix.scenarios.lowestkiller;

import io.github._7isenko.scenariomix.ScenarioMix;
import io.github._7isenko.scenariomix.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

public class LowestKiller extends Scenario {

    BukkitTask task;

    public LowestKiller() {
        super("Низшему не жить", new String[]{"Каждую минуту самый", "нижний игрок умирает"}, Material.BRICK_STAIRS);
    }

    @Override
    public void start() {
       task = Bukkit.getScheduler().runTaskTimer(ScenarioMix.plugin, new KillerRunnable(), 20, 1200);
    }

    @Override
    public void stop() {
        task.cancel();
        task = null;
    }
}
