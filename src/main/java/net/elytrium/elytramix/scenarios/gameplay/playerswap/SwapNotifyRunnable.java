package net.elytrium.elytramix.scenarios.gameplay.playerswap;

import net.elytrium.elytramix.utils.PlayerUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SwapNotifyRunnable extends BukkitRunnable {
    public void run() {
        TextComponent component = new TextComponent("Скоро вы поменяетесь местами с другим игроком..");

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(PlayerUtils::isValid)
                .map(Player::spigot)
                .forEach(e -> e.sendMessage(ChatMessageType.ACTION_BAR, component));
    }
}