package net.elytrium.elytramix.scenarios.commands.collision;

import net.elytrium.elytramix.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Collision extends Scenario {
    public Collision() {
        super("Правило коллизии", "collision_toggle", "MAGMA_CREAM", "tool","Изменяет значение", "Collision Rule");
        addListener(new JoinListener());
    }

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    HashMap<Team, Team.OptionStatus> oldValues = new HashMap<>(); // Backup of previous collision values

    @Override
    public void start(Player player) {
        List<String> list = new ArrayList();

        // If player don't have a team, add this dude to collision team
        scoreboard.getTeams().stream().map(Team::getEntries).forEach(list::addAll);
        Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(q -> !list.contains(q)).forEach(getCollisionTeam()::addEntry);

        // Disable collision for every team
        scoreboard.getTeams().forEach(team -> {
            // Backup current collision value
            oldValues.put(team, team.getOption(Team.Option.COLLISION_RULE));

            team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        });
    }

    @Override
    public void stop() {
        // Clear team members
        getCollisionTeam().getEntries().forEach(entry -> getCollisionTeam().removeEntry(entry));

        // Return previous values to teams
        scoreboard.getTeams().forEach(team -> team.setOption(Team.Option.COLLISION_RULE, oldValues.remove(team)));
    }

    public static Team getCollisionTeam(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        Team team = scoreboard.getTeam("em-collision");

        if(team == null){
            team = scoreboard.registerNewTeam("em-collision");
            team.setOption(Team.Option.COLLISION_RULE , Team.OptionStatus.NEVER);
        }

        return team;
    }
}
