package net.elytrium.elytramix.scenarios.gameplay.throwtnt;

import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import net.elytrium.elytramix.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ThrowTNT extends Scenario {
    public ThrowTNT() {
        super("Выброс TNT", "throw_tnt", "TNT", "scenario","Возможность выбрасывать TNT", "определенным предметом", "Задача игроков - не", "попасть в воду.");
        addConfig(item_id);
        addConfig(delay);
        addConfig(velocity);
        addConfig(kill_radius);
        addConfig(player_velocity);
        addListener(new WaterListener());
        addListener(new TNTListener(this));
    }

    private final Configuration<String> item_id = new Configuration<>("item", "END_ROD", this, "Предмет-бросалка TNT");
    private final Configuration<Integer> delay = new Configuration<>("delay", "WATCH", this, "Время взрыва TNT");
    private final Configuration<Integer> velocity = new Configuration<>("velocity", "TNT", this, "Скорость полета TNT");
    private final Configuration<Integer> kill_radius = new Configuration<>("kill_radius", "BARRIER", this, "Радиус работы TNT");
    private final Configuration<Integer> player_velocity = new Configuration<>("player_velocity",  "FEATHER", this, "Скорость игрока при отбросе");

    public void start(Player player) {

    }

    public void stop() {

    }

    public Material getItem(){
        return ItemUtils.getMaterial(item_id.value().toUpperCase());
    }

    public Integer getDelay(){
        return delay.value();
    }

    public Integer getVelocity(){
        return velocity.value();
    }

    public Integer getKillRadius(){
        return kill_radius.value();
    }

    public Integer getPlayerVelocity(){
        return player_velocity.value();
    }
}
