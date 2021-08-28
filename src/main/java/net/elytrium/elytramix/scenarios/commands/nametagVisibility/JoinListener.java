package net.elytrium.elytramix.scenarios.commands.nametagVisibility;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class JoinListener implements Listener {
    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    Team team = scoreboard.getTeam("em-nametag");

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        List<String> list = new ArrayList();

        scoreboard.getTeams().stream().map(Team::getEntries).forEach(list::addAll);
        list.stream().filter(q -> !list.contains(p.getName())).forEach(team::addEntry);
    }
}
