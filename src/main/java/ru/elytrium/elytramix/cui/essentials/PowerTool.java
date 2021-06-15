package ru.elytrium.elytramix.cui.essentials;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.elytrium.elytramix.Plugin;

import java.io.IOException;
import java.util.HashMap;

public class PowerTool implements CommandExecutor {

    private final Plugin plugin;

    public PowerTool(Plugin plugin){ this.plugin = plugin; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Данная команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) commandSender;
        ItemStack itemStack = player.getItemInHand();

        String itemID = itemStack.getType().name();
        String uuid = player.getUniqueId().toString();

        String toolCommand = String.join(" ", strings);

        if(strings.length == 0){
            plugin.getPowertoolConfig().set(uuid+"."+itemID, null);
            savePowertool();
            commandSender.sendMessage(plugin.getMessageString("elytramix.powertool-clean")
                    .replace("{item}", itemID));
            return true;
        }

        if(!plugin.getPowertoolConfig().contains(uuid)){
            createColumn(toolCommand, itemStack, player);
        } else if(!plugin.getPowertoolConfig().contains(uuid+"."+itemID)){
            plugin.getPowertoolConfig().set(uuid+"."+itemID, toolCommand);
            savePowertool();
        } else if(plugin.getPowertoolConfig().contains(uuid+"."+itemID)){
            plugin.getPowertoolConfig().set(uuid+"."+itemID, toolCommand);
            savePowertool();
        }

        commandSender.sendMessage(plugin.getMessageString("elytramix.powertool-bind")
                .replace("{item}", itemID));

        return true;
    }

    private void createColumn(String toolCommand, ItemStack itemStack, Player player){
        HashMap<String, String> tool = new HashMap<>();
        tool.put(itemStack.getType().name(), toolCommand);

        plugin.getMessagesConfig().set(player.getUniqueId().toString(), tool);
        savePowertool();
    }

    private void savePowertool(){
        try {
            plugin.getPowertoolConfig().save(plugin.powertoolFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
