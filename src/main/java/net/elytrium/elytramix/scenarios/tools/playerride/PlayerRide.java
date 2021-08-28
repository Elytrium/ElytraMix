package net.elytrium.elytramix.scenarios.tools.playerride;

import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import org.bukkit.entity.Player;

public class PlayerRide extends Scenario {
    private final Configuration<Boolean> allowMany = new Configuration<>("allowMany", "LEATHER_BOOTS", this, "Разрешает игрокам сидеть", "большой кучей");
    private final Configuration<Boolean> allowLeave = new Configuration<>("allowLeave",  "IRON_BOOTS", this, "Разрешает слазить");
    private final Configuration<Boolean> onlyTeam = new Configuration<>("onlyTeam",  "EMPTY_MAP", this, "Разрешает садиться только", "игрокам одной команды");
    private final Configuration<Boolean> killOnLeave = new Configuration<>("killOnLeave",  "DIAMOND_SWORD", this, "Убивать когда кто-то встает");

    public PlayerRide() {
        super("Погнали", "playerride", "SADDLE", "tool","Теперь можно ездить на игроках");
        addConfig(allowMany);
        addConfig(allowLeave);
        addConfig(onlyTeam);
        addConfig(killOnLeave);
        addListener(new PlayerRidingListener(this));
    }

    @Override
    public void start(Player player) {

    }

    @Override
    public void stop() {

    }

    public boolean isAllowMany(){
        return allowMany.getValue();
    }

    public boolean isAllowLeave(){
        return allowLeave.getValue();
    }

    public boolean allowOnlyTeam(){
        return onlyTeam.getValue();
    }

    public boolean needKillOnLeave(){
        return killOnLeave.getValue();
    }
}
