package net.elytrium.elytramix.events.essentials;

import net.elytrium.elytramix.Plugin;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.io.IOException;
import java.util.HashMap;

public class PositionListener implements Listener {
    FileConfiguration config = Plugin.getInstance().getPlayerConfig();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();

        if(!(config.contains(player.getUniqueId().toString()))) createColumn(player);
        else setPosition(player, player.getLocation());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        setPosition(e.getPlayer(), e.getPlayer().getLocation());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        setPosition(e.getPlayer(), e.getFrom());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        setPosition(e.getEntity(), e.getEntity().getLocation());
    }

    private void createColumn(Player player) {
        Location loc = player.getLocation();

        HashMap<String, String> map = new HashMap<>();
        map.put("last-position", loc.getWorld().getName()+";"+loc.getX()+";"+loc.getY()+";"+loc.getZ()+";"+loc.getYaw()+";"+loc.getPitch());

        config.createSection(player.getUniqueId().toString(), map);
        saveData();
    }

    private void saveData(){
        try {
            config.save(Plugin.getInstance().dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Plugin.getInstance().createConfigs();
    }

    private void setPosition(Player player, Location loc){
        config.set(player.getUniqueId().toString()+".last-position", loc.getWorld().getName()+";"+loc.getX()+";"+loc.getY()+";"+loc.getZ()+";"+loc.getYaw()+";"+loc.getPitch());
        saveData();
    }
}
