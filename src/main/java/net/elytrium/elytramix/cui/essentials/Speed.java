package net.elytrium.elytramix.cui.essentials;

import net.elytrium.elytramix.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Speed implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(!(commandSender instanceof Player)) {
            commandSender.sendMessage(ChatColor.RED + "Данная команда доступна только игрокам!");
            return true;
        }

        Player player = (Player) commandSender;
        float speed = Float.parseFloat(strings[0]);

        if(speed > 10){
            player.sendMessage(Plugin.getInstance().getMessageString("elytramix.speed-bad-int")
                .replace("{num}", String.valueOf(speed)));
            return true;
        }

        if(strings.length == 1){
            toggleSpeed(player, speed);
            return true;
        } else if(strings.length == 2){
            if(strings[1].equals("*")){
                Bukkit.getOnlinePlayers().forEach(target -> {
                    toggleSpeed(target, Float.parseFloat(strings[0]));
                });
            } else {
                Player target = Bukkit.getPlayer(strings[1]);
                if (target == null) {
                    player.sendMessage(Plugin.getInstance().getMessageString("not-found"));
                    return true;
                }
                toggleSpeed(target, Float.parseFloat(strings[0]));
            }
            return true;
        } else {
            return false;
        }
    }

    private void toggleSpeed(Player player, float speed){
        if (player.isFlying()) {
            player.setFlySpeed(getRealMoveSpeed(speed, true));
            player.sendMessage(Plugin.getInstance().getMessageString("elytramix.speed-flying")
                    .replace("{speed}", String.valueOf(speed)));
        } else {
            player.setWalkSpeed(getRealMoveSpeed(speed, false));
            player.sendMessage(Plugin.getInstance().getMessageString("elytramix.speed-walk")
                    .replace("{speed}", String.valueOf(speed)));
        }
    }

    private float getRealMoveSpeed(final float userSpeed, final boolean isFly)
    {
        final float defaultSpeed = isFly ? 0.1f : 0.2f;
        float maxSpeed = 1f;

        if (userSpeed < 1f) {
            return defaultSpeed * userSpeed;
        } else {
            float ratio = ((userSpeed - 1) / 9) * (maxSpeed - defaultSpeed);
            return ratio + defaultSpeed;
        }
    }
}
