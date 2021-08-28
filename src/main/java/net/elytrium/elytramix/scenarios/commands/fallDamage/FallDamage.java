package net.elytrium.elytramix.scenarios.commands.fallDamage;

import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import net.elytrium.elytramix.scenarios.Scenario;
import org.bukkit.entity.Player;

import static net.elytrium.elytramix.utils.WorldGuardUtil.getLocatedRegion;
import static net.elytrium.elytramix.utils.WorldGuardUtil.toggleRegionFlag;

public class FallDamage extends Scenario {
    public FallDamage() {
        super("Урон от падения", "falldamage_toggle", "RABBIT_FOOT", "fast-command","Переключает флаг §eFALL-DAMAGE", "§oв текущем регионе");
    }

    @Override
    public void start(Player player) {
        ProtectedRegion region = getLocatedRegion(player.getLocation());
        toggleRegionFlag(region, Flags.FALL_DAMAGE, player);
    }

    @Override
    public void stop() {

    }
}
