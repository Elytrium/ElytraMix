package net.elytrium.elytramix;

import net.elytrium.elytramix.cui.ConfigurationTabCompleter;
import net.elytrium.elytramix.cui.ScenarioMixCommand;
import net.elytrium.elytramix.cui.essentials.*;
import net.elytrium.elytramix.events.essentials.PositionListener;
import net.elytrium.elytramix.events.essentials.PowerToolUse;
import net.elytrium.elytramix.scenarios.ScenarioCategory;
import net.elytrium.elytramix.scenarios.ScenarioManager;
import net.elytrium.elytramix.scenarios.commands.build.Build;
import net.elytrium.elytramix.scenarios.commands.clearWhitelist.ClearWhitelist;
import net.elytrium.elytramix.scenarios.commands.collision.Collision;
import net.elytrium.elytramix.scenarios.commands.fallDamage.FallDamage;
import net.elytrium.elytramix.scenarios.commands.nametagVisibility.NametagVisibility;
import net.elytrium.elytramix.scenarios.commands.pvp.Pvp;
import net.elytrium.elytramix.scenarios.commands.use.Use;
import net.elytrium.elytramix.scenarios.commands.whitelist.Whitelist;
import net.elytrium.elytramix.scenarios.gameplay.apocalypse.Apocalypse;
import net.elytrium.elytramix.scenarios.gameplay.collideath.Collideath;
import net.elytrium.elytramix.scenarios.gameplay.lastsight.LastSight;
import net.elytrium.elytramix.scenarios.gameplay.lowestkiller.LowestKiller;
import net.elytrium.elytramix.scenarios.gameplay.nojump.NoJump;
import net.elytrium.elytramix.scenarios.gameplay.playerswap.PlayerSwap;
import net.elytrium.elytramix.scenarios.gameplay.pusher.Pusher;
import net.elytrium.elytramix.scenarios.gameplay.randomgive.RandomGive;
import net.elytrium.elytramix.scenarios.gameplay.security.Security;
import net.elytrium.elytramix.scenarios.gameplay.snowballs.Snowballs;
import net.elytrium.elytramix.scenarios.gameplay.snowfall.Snowfall;
import net.elytrium.elytramix.scenarios.gameplay.spiderpocalypse.Spiderpocalypse;
import net.elytrium.elytramix.scenarios.gameplay.throwtnt.ThrowTNT;
import net.elytrium.elytramix.scenarios.tools.autorespawn.AutoRespawn;
import net.elytrium.elytramix.scenarios.tools.autospectator.AutoSpectator;
import net.elytrium.elytramix.scenarios.tools.fightme.FightMe;
import net.elytrium.elytramix.scenarios.tools.fill.Fill;
import net.elytrium.elytramix.scenarios.tools.heightlimit.HeightLimit;
import net.elytrium.elytramix.scenarios.tools.playerride.PlayerRide;
import net.elytrium.elytramix.scenarios.tools.randomteam.RandomTeam;
import net.elytrium.elytramix.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Plugin extends JavaPlugin {
    private static Plugin instance;
    private static final String command = "mix";

    public File dataFile;
    private FileConfiguration playerData;

    private File messagesFile;
    private FileConfiguration messagesData;

    public File spawnFile;
    private FileConfiguration spawnData;

    public File scenarioFile;
    private FileConfiguration scenarioData;

    @Override
    public void onEnable() {
        instance = this;
        loadScenarioManager();
        this.getCommand(command).setExecutor(new ScenarioMixCommand(this));
        this.getCommand(command).setTabCompleter(new ConfigurationTabCompleter());

        // Creating configuration files
        createConfigs();
        checkConfigurationVersion();

        // Essentials commands
        this.getCommand("bc").setExecutor(new Broadcast());
        this.getCommand("day").setExecutor(new Day());
        this.getCommand("fly").setExecutor(new Fly());
        this.getCommand("gm").setExecutor(new Gamemode());
        this.getCommand("heal").setExecutor(new Heal());
        this.getCommand("kickall").setExecutor(new KickAll());
        this.getCommand("mobkill").setExecutor(new MobKill());
        this.getCommand("night").setExecutor(new Night());
        this.getCommand("rain").setExecutor(new Rain());
        this.getCommand("sun").setExecutor(new Sun());
        this.getCommand("powertool").setExecutor(new PowerTool());
        this.getCommand("tpall").setExecutor(new TeleportAll());
        this.getCommand("tphere").setExecutor(new TeleportHere());
        this.getCommand("spawn").setExecutor(new Spawn());
        this.getCommand("setspawn").setExecutor(new Spawn());
        this.getCommand("speed").setExecutor(new Speed());
        this.getCommand("back").setExecutor(new Back());

        Bukkit.getPluginManager().registerEvents(new PowerToolUse(), this);
        Bukkit.getPluginManager().registerEvents(new PositionListener(), this);
    }

    @Override
    public void onDisable() {
        ScenarioManager.getInstance().disableAll();
    }

    private void loadScenarioManager() {
        final ScenarioCategory CATEGORY_GAMEPLAY = new ScenarioCategory("Геймплей",
                ItemUtils.getMaterial("NOTE_BLOCK"));
        final ScenarioCategory CATEGORY_TOOL = new ScenarioCategory("Инструменты",
                ItemUtils.getMaterial("STONE_SWORD"));
        final ScenarioCategory CATEGORY_COMMANDS = new ScenarioCategory("Быстрые команды",
                ItemUtils.getMaterial("GLOWSTONE_DUST"));

        CATEGORY_TOOL.addScenario(new AutoSpectator());
        CATEGORY_TOOL.addScenario(new AutoRespawn());
        CATEGORY_TOOL.addScenario(new FightMe());
        CATEGORY_TOOL.addScenario(new HeightLimit());
        CATEGORY_TOOL.addScenario(new RandomTeam());
        CATEGORY_TOOL.addScenario(new Fill());
        CATEGORY_TOOL.addScenario(new PlayerRide());

        CATEGORY_GAMEPLAY.addScenario(new Pusher());
        CATEGORY_GAMEPLAY.addScenario(new LowestKiller());
        CATEGORY_GAMEPLAY.addScenario(new Snowballs());
        CATEGORY_GAMEPLAY.addScenario(new Snowfall());
        CATEGORY_GAMEPLAY.addScenario(new LastSight());
        CATEGORY_GAMEPLAY.addScenario(new Spiderpocalypse());
        CATEGORY_GAMEPLAY.addScenario(new Apocalypse());
        CATEGORY_GAMEPLAY.addScenario(new Collideath());
        CATEGORY_GAMEPLAY.addScenario(new NoJump());
        CATEGORY_GAMEPLAY.addScenario(new Security());
        CATEGORY_GAMEPLAY.addScenario(new RandomGive());
        CATEGORY_GAMEPLAY.addScenario(new ThrowTNT());
        CATEGORY_GAMEPLAY.addScenario(new PlayerSwap());

        CATEGORY_COMMANDS.addScenario(new Build());
        CATEGORY_COMMANDS.addScenario(new Pvp());
        CATEGORY_COMMANDS.addScenario(new Use());
        CATEGORY_COMMANDS.addScenario(new Whitelist());
        CATEGORY_COMMANDS.addScenario(new NametagVisibility());
        CATEGORY_COMMANDS.addScenario(new Collision());
        CATEGORY_COMMANDS.addScenario(new ClearWhitelist());
        CATEGORY_COMMANDS.addScenario(new FallDamage());

        ScenarioManager scenarioManager = ScenarioManager.getInstance();
        scenarioManager.addCategory(CATEGORY_GAMEPLAY);
        scenarioManager.addCategory(CATEGORY_TOOL);
        scenarioManager.addCategory(CATEGORY_COMMANDS);
    }

    public void createConfigs() {
        dataFile = new File(this.getDataFolder(), "player-data.yml");
        messagesFile = new File(this.getDataFolder(), "messages.yml");
        spawnFile = new File(this.getDataFolder(), "spawn.yml");
        scenarioFile = new File(this.getDataFolder(), "scenario-data.yml");

        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            this.saveResource("player-data.yml", false);
        }

        if(!spawnFile.exists()){
            spawnFile.getParentFile().mkdirs();
            this.saveResource("spawn.yml", false);
        }

        if(!messagesFile.exists()){
            dataFile.getParentFile().mkdirs();
            this.saveResource("messages.yml", false);
        }

        if(!scenarioFile.exists()){
            dataFile.getParentFile().mkdirs();
            this.saveResource("scenario-data.yml", false);
        }

        playerData = new YamlConfiguration();
        messagesData = new YamlConfiguration();
        spawnData = new YamlConfiguration();
        scenarioData = new YamlConfiguration();

        try {
            playerData.load(dataFile);
            messagesData.load(messagesFile);
            spawnData.load(spawnFile);
            scenarioData.load(scenarioFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    private void checkConfigurationVersion(){
        try{
            if(!messagesData.getString("config-version").equals(this.getDescription().getVersion())){
                messagesFile.getParentFile().mkdirs();
                this.saveResource("messages.yml", true);
                
                getLogger().warning("Legacy messages config detected! Messages config reset.");
            }
        } catch (NullPointerException e){
            messagesFile.getParentFile().mkdirs();
            this.saveResource("messages.yml", true);
            
            getLogger().warning("Legacy messages config detected! Messages config reset.");
        }

        try{
            if(!scenarioData.getString("config-version").equals(this.getDescription().getVersion())){
                scenarioFile.getParentFile().mkdirs();
                this.saveResource("scenario-data.yml", true);

                getLogger().warning("Legacy scenario config detected! Scenarios settings reset.");
            }
        } catch (NullPointerException e){
            scenarioFile.getParentFile().mkdirs();
            this.saveResource("scenario-data.yml", true);

            getLogger().warning("Legacy scenario config detected! Scenarios settings reset.");
        }
        
        getLogger().info("Config files check complete.");
    }

    public FileConfiguration getPlayerConfig() {
        return this.playerData;
    }

    public FileConfiguration getMessagesConfig() {
        return this.messagesData;
    }

    public FileConfiguration getSpawnConfig(){
        return this.spawnData;
    }
    
    public  FileConfiguration getScenarioConfig() { return this.scenarioData; }

    public String getMessageString(String path) {
        String s = this.getMessagesConfig().getString("prefix") + this.getMessagesConfig().getString(path);
        s = s.replace("&", "\u00A7");
        return s;
    }

    public String getPrefixString(){
        String s = this.getMessagesConfig().getString("prefix");
        s = s.replace("&", "\u00A7");
        return s;
    }

    public static Plugin getInstance() {
        return instance;
    }

    public static String getCommand() {
        return command;
    }
}