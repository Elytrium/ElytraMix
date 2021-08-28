package net.elytrium.elytramix.scenarios.tools.fill;

import net.elytrium.elytramix.Plugin;
import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import net.elytrium.elytramix.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Fill extends Scenario {
    public Fill() {
        super("Заполнение", "fill", "LAVA_BUCKET", "tool","Постепенно заполняет мир", "выбранным блоком");
        addConfig(material);
        addConfig(delay);
        addConfig(amount);
        addConfig(current);
        addConfig(replace);
        addConfig(upside);
    }

    private final Configuration<String> material = new Configuration<>("material", "BOWL", this, "Чем заполнять");
    private final Configuration<Integer> delay = new Configuration<>("delay", "FEATHER", this, "Время между заполнениями", "(секунды)");
    private final Configuration<Integer> amount = new Configuration<>("amount", "COBBLESTONE_STAIRS", this, "Толщина блоков, заполняемых ", "за один раз");
    private final Configuration<Integer> current = new Configuration<>("current","SIGN", this, "Текущая высота", "заполнения");
    private final Configuration<Boolean> replace = new Configuration<>("replace","IRON_SPADE", this, "Заменять блоки", "любого типа");
    private final Configuration<Boolean> upside = new Configuration<>("upside", "SNOW_BALL", this, "Заполнение идёт сверху");
    private BukkitRunnable task = null;

    @Override
    public void start(Player player) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!isStarted()) {
                    task.cancel();
                    return;
                }
                World world;
                try {
                    world = ((Player) Bukkit.getOnlinePlayers().toArray()[0]).getWorld();
                } catch (IndexOutOfBoundsException e) {
                    Bukkit.broadcastMessage("никого нет...");
                    return;
                }
                int borderSize = (int) Math.ceil(world.getWorldBorder().getSize());
                if (borderSize > 1000) {
                    Bukkit.broadcast(ChatColor.RED + "Размер границ мира больше 1000, заполнение отменено. Нужен перезапуск", "scenariomix.debug");
                    task.cancel();
                    task = null;
                    return;
                }
                fill(world, borderSize);
                start(player);
            }
        };
        task.runTaskLater(Plugin.getInstance(), delay.value() * 20);
    }

    @Override
    public void stop() {
        if (task != null && !task.isCancelled()) {
            task.cancel();
            task = null;
        }
    }

    private void fill(World world, int size) {
        if (current.value() >= 256 || current.value() < 0)
            return;
        Location center = world.getWorldBorder().getCenter();
        for (int y = 0; y < amount.value(); y++) {
            for (int x = center.getBlockX() - size / 2; x <= center.getBlockX() + size / 2; x++) {
                for (int z = center.getBlockZ() - size / 2; z <= center.getBlockZ() + size / 2; z++) {
                    Block block = world.getBlockAt(x, current.value() + y, z);
                    if (!replace.value() && block.getType() != Material.AIR)
                        continue;
                    block.setType(ItemUtils.getMaterial(material.value().toUpperCase()), false);
                }
            }
        }
        if (upside.getValue()) {
            current.setValue(current.getValue() - amount.value());
        } else
            current.setValue(current.getValue() + amount.value());
    }
}
