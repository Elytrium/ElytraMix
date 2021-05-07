package dev.hevav.essentialsplugin.gui.pages.scenario;

import dev.hevav.essentialsplugin.Plugin;
import dev.hevav.essentialsplugin.gui.MenuHandler;
import dev.hevav.essentialsplugin.gui.Menu;
import dev.hevav.essentialsplugin.gui.MenuItem;
import dev.hevav.essentialsplugin.scenarios.Scenario;
import dev.hevav.essentialsplugin.scenarios.ScenarioManager;
import dev.hevav.essentialsplugin.utils.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioMenu extends Menu {
    private final Collection<Scenario> scenarios;
    private final Menu mainMenu = MenuHandler.getInstance().getMainMenu();
    private final Menu instance = this;

    public ScenarioMenu(String category) {
        super(category, false, 0);
        this.scenarios = ScenarioManager.getInstance().getScenarios(category);
    }

    @Override
    public List<MenuItem> getItems() {
        return scenarios.stream().map((scenario) -> {
            ItemStack item = new ItemStack(scenario.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + scenario.getName());
            ArrayList<String> lore = new ArrayList<>(scenario.getDescription());
            lore.add(ChatColor.GRAY + scenario.getConfigName());
            meta.setLore(lore);
            item.setItemMeta(meta);
            return new MenuItem(item, (e) -> scenario.toggle());
        }).collect(Collectors.toList());
    }

    public void switchScenario(InventoryClickEvent event, Scenario scenario) {
        if (event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
            boolean enabled = scenario.toggle();
            ItemUtils.enchantItem(event.getCursor(), !enabled);
        } else if (event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    new ConfigurationsMenu(scenario, instance).open((Player) event.getWhoClicked(), false);
                }
            }.runTaskLater(Plugin.plugin, 1);
        }
    }

    @Override
    public void onClick(InventoryInteractEvent event) {
        mainMenu.open((Player) event.getWhoClicked(), false);
    }
}
