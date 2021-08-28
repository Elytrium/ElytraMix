package net.elytrium.elytramix.cui.essentials;

import net.elytrium.elytramix.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Back implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED+"Данная команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) commandSender;

        String loc = Plugin.getInstance().getPlayerConfig().getString(player.getUniqueId().toString()+".last-position");

        String[] pos = loc.split(";");

        player.teleport(new Location(Bukkit.getWorld(pos[0]), Double.parseDouble(pos[1]), Double.parseDouble(pos[2]),
                Double.parseDouble(pos[3]), Float.parseFloat(pos[4]), Float.parseFloat(pos[5])));

        player.sendMessage(Plugin.getInstance().getMessageString("elytramix.back"));

        return true;
    }
}
