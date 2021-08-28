package net.elytrium.elytramix.scenarios.commands.nametagVisibility;

import net.elytrium.elytramix.scenarios.Scenario;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NametagVisibility extends Scenario {
    public NametagVisibility() {
        super("Видимость ников", "nametag_toggle", "NAME_TAG", "tool","Изменяет видимость", "никнеймов игроков");
        addListener(new JoinListener());
    }

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    HashMap<Team, Team.OptionStatus> oldValues = new HashMap<>(); // Backup of previous nametags values

    @Override
    public void start(Player player) {
        List<String> list = new ArrayList();

        // If player don't have a team, add this dude to nametag team
        scoreboard.getTeams().stream().map(Team::getEntries).forEach(list::addAll);
        Bukkit.getOnlinePlayers().stream().map(Player::getName).filter(q -> !list.contains(q)).forEach(getNametagTeam()::addEntry);

        // Disable nametags for every team
        scoreboard.getTeams().forEach(team -> {
            // Backup current nametag value
            oldValues.put(team, team.getOption(Team.Option.NAME_TAG_VISIBILITY));

            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        });
    }

    @Override
    public void stop() {
        // Clear team members
        getNametagTeam().getEntries().forEach(entry -> getNametagTeam().removeEntry(entry));

        // Return previous values to teams
        scoreboard.getTeams().forEach(team -> team.setOption(Team.Option.NAME_TAG_VISIBILITY, oldValues.remove(team)));
    }

    public static Team getNametagTeam(){
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getTeam("em-nametag");

        if(team == null){
            team = scoreboard.registerNewTeam("em-nametag");
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
        }

        return team;
    }
}
