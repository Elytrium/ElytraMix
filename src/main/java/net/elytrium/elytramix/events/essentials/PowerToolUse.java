package net.elytrium.elytramix.events.essentials;

import net.elytrium.elytramix.Plugin;
import net.elytrium.elytramix.utils.HitBoundingBox;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class PowerToolUse implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void ToolInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        String itemID = item.getType().name();
        String uuid = player.getUniqueId().toString();
        String toolCommand = Plugin.getInstance().getPlayerConfig().getString(uuid+".powertools."+itemID);

        if(Plugin.getInstance().getPlayerConfig().contains(uuid+".powertools."+itemID)){
            if(toolCommand.contains("{player}")){
                Player target = findPlayerInSight(player);

                if(target != null){
                    toolCommand = toolCommand.replace("{player}", target.getName());
                    toolCommand = toolCommand.replace("{world}", player.getWorld().getName());
                    player.performCommand(toolCommand);
                } else {
                    event.setCancelled(true);
                }
            } else {
                toolCommand = toolCommand.replace("{world}", player.getWorld().getName());
                player.performCommand(toolCommand);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void ToolDamageEntity(EntityDamageByEntityEvent event){
        if (event.getDamager() instanceof Player){
            Player damager = (Player) event.getDamager();
            ItemStack item = damager.getItemInHand();
            String itemID = item.getType().name();
            String uuid = damager.getUniqueId().toString();
            String toolCommand = Plugin.getInstance().getPlayerConfig().getString(uuid+".powertools."+itemID);

            if(Plugin.getInstance().getPlayerConfig().contains(uuid) && Plugin.getInstance().getPlayerConfig().contains(uuid+".powertools."+itemID)){
                if(toolCommand.contains("{player}")){
                    if(event.getEntity() instanceof Player){
                        Player target = findPlayerInSight(damager);

                        if(target != null){
                            toolCommand = toolCommand.replace("{player}", target.getName());
                        }
                        toolCommand = toolCommand.replace("{world}", damager.getWorld().getName());
                        damager.performCommand(toolCommand);
                    } else {
                        event.setCancelled(true);
                    }
                } else {
                    event.setCancelled(true);
                }
            }
        }
    }

    private Player findPlayerInSight(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Vector origin = eyeLocation.toVector();
        Vector direction = eyeLocation.getDirection().multiply(100);

        Player closest = null;
        double closestDistance = Double.MAX_VALUE;

        // FIXME I feel like this call can be improved by limiting the bounding box...
        for (Entity e : player.getNearbyEntities(5, 5, 5)) {
            if (!(e instanceof Player)) continue; // Only care about Players

            Player other = (Player)e;
            Location otherLoc = other.getLocation();

            // Determine bounds of 1x2x1 AABB
            Vector minB = new Vector(otherLoc.getX() - 0.5, otherLoc.getY(), otherLoc.getZ() - 0.5);
            Vector maxB = new Vector(otherLoc.getX() + 0.5, otherLoc.getY() + 2.0, otherLoc.getZ() + 0.5);

            if (HitBoundingBox.hitBoundingBox(minB, maxB, origin, direction, null)) {
                // Player is within crosshairs
                // If they're closer than the current closest, remember them
                double distanceSquared = origin.distanceSquared(otherLoc.toVector());
                if (distanceSquared < closestDistance) {
                    closest = other;
                    closestDistance = distanceSquared;
                }
            }
        }

        return closest;
    }

}
