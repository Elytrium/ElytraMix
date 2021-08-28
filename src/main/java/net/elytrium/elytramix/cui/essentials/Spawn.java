package net.elytrium.elytramix.cui.essentials;

import net.elytrium.elytramix.Plugin;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;

public class Spawn implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Данная команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) commandSender;

        if(command.getName().equals("setspawn")){
            Location loc = ((Player) commandSender).getLocation();
            FileConfiguration spawn = Plugin.getInstance().getSpawnConfig();

            loc.getWorld().setSpawnLocation(loc);
            spawn.set(loc.getWorld().getName(), loc.getX()+";"+loc.getY()+";"+ loc.getZ()+";"+loc.getYaw()+";"+loc.getPitch()+";");
            try {
                spawn.save(Plugin.getInstance().spawnFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            commandSender.sendMessage(Plugin.getInstance().getMessageString("elytramix.spawn-set")
                    .replace("{world}", loc.getWorld().getName()));

            return true;
        } else if(command.getName().equals("spawn")){
            String spawn = Plugin.getInstance().getSpawnConfig().getString(player.getWorld().getName());

            if(spawn == null){
                player.teleport(player.getWorld().getSpawnLocation());
                player.sendMessage(Plugin.getInstance().getMessageString("elytramix.spawn-default")
                    .replace("{world}", player.getWorld().getName()));
                return true;
            }

            String[] pos = spawn.split(";");

            player.teleport(new Location(player.getWorld(), Double.parseDouble(pos[0]), Double.parseDouble(pos[1]),
                    Double.parseDouble(pos[2]), Float.parseFloat(pos[3]), Float.parseFloat(pos[4])));

            return true;
        }

        return true;
    }
}
