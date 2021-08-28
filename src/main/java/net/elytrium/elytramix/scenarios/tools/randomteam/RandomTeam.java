package net.elytrium.elytramix.scenarios.tools.randomteam;

import net.elytrium.elytramix.scenarios.Scenario;
import net.elytrium.elytramix.scenarios.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomTeam extends Scenario {
    public RandomTeam() {
        super("Рандомные команды", "random_team", "SKULL_ITEM", "tool", "Рандомно делит игроков на команды,", "Количество игроков в команде регулируется командой", "/mix random_team player_limit <лимит>");
        addConfig(player_limit);
        addConfig(team_prefix);
    }

    private final Configuration<Integer> player_limit = new Configuration<>("player_limit", "SKULL_ITEM", this, "Игроков в одной команде");
    private final Configuration<String> team_prefix = new Configuration<>("team_prefix", "COMMAND", this, "Префикс в команде Minecraft");

    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    public void start(Player player) {
        List<List<String>> teams = getTeams(new ArrayList<>(Bukkit.getOnlinePlayers()), getPlayerLimit());

        for (int i = 0; i < teams.size(); i++) {
            Team team = scoreboard.getTeam(getTeamPrefix() + i);

            if (team == null) {
                team = scoreboard.registerNewTeam(getTeamPrefix() + i);

                ChatColor[] colors = ChatColor.values();
                team.setColor(colors[i % colors.length]);
            }

            team.getEntries().forEach(team::removeEntry);
            teams.get(i).forEach(team::addEntry);
        }
    }

    public void stop() {
        scoreboard.getTeams().stream().filter(team -> team.getName().contains(getTeamPrefix()))
                .forEach(team -> team.getEntries().forEach(team::removeEntry));
    }

    public int getPlayerLimit() {
        return player_limit.value();
    }
    public String getTeamPrefix() {
        return team_prefix.value();
    }

    public List<List<String>> getTeams(List<Player> all, int limit){
        int playersCount = all.size();
        int teamsCount = (playersCount + limit - 1) / limit;

        List<List<String>> teams = new ArrayList<>();
        for (int i = 0; i < teamsCount; ++i){
            teams.add(new ArrayList<>());
        }

        Collections.shuffle(all);
        for (int i = 0; i < playersCount; ++i){
            teams.get(i % teamsCount).add(all.get(i).getName());
        }

        return teams;
    }
}
