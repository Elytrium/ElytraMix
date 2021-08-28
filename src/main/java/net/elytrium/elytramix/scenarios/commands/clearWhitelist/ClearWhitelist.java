package net.elytrium.elytramix.scenarios.commands.clearWhitelist;

import net.elytrium.elytramix.Plugin;
import net.elytrium.elytramix.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ClearWhitelist extends Scenario {
    public ClearWhitelist() {
        super("Очистить белый список", "clear-whitelist", "CLAY_BALL", "fast-command", "Очищает", "белый список");
    }

    @Override
    public void start(Player player){
        Bukkit.getWhitelistedPlayers().forEach(pl -> player.performCommand("whitelist remove "+pl.getName()));
        player.sendMessage(Plugin.getInstance().getPrefixString()+"Белый список очищен!");
    }

    @Override
    public void stop() {

    }
}
