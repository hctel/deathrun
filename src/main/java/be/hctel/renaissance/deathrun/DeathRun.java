package be.hctel.renaissance.deathrun;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.ScoreboardManager;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.WorldManager;

import be.hctel.renaissance.cosmetics.CosmeticsManager;
import be.hctel.renaissance.deathrun.commands.StaffCommands;
import be.hctel.renaissance.deathrun.commands.TestTrapCommand;
import be.hctel.renaissance.deathrun.commands.TrapCommands;
import be.hctel.renaissance.deathrun.commands.completers.StaffCommandCompleter;
import be.hctel.renaissance.deathrun.commands.completers.TrapCommandCompleter;
import be.hctel.renaissance.deathrun.data.DRStats;
import be.hctel.renaissance.deathrun.engine.MainGameEngine;
import be.hctel.renaissance.deathrun.listeners.BlockListeners;
import be.hctel.renaissance.deathrun.listeners.ConnectionListeners;
import be.hctel.renaissance.deathrun.listeners.EntityTrapListeners;
import be.hctel.renaissance.deathrun.listeners.PlayerListener;
import be.hctel.renaissance.global.mapmanager.MapManager;
import be.hctel.renaissance.global.storage.SQLConnector;

public class DeathRun extends JavaPlugin {
	
	public String header = "§8▍ §4Death§cRun§8 ▏ ";
	
	public Plugin plugin;
	
	private SQLConnector connector;
	public DRStats stats;
	public CosmeticsManager cosmetics;
	
	public MultiverseCore core;
	public MultiverseCoreApi mvAPI;
	public WorldManager worldManager;
	
	public MapManager mapManager; 
	public MainGameEngine mainGameEngine;
	
	public ScoreboardManager scoreboardManager;
	
	private String sqlUser, sqlHost, sqlDatabase, sqlPassowrd;
	private int sqlPort;

	@Override
	public void onEnable() {
		getLogger().info("Enabling DeathRun");
		plugin = this;
		
		loadCredentials();
		connector = new SQLConnector(sqlUser, sqlHost, sqlPort, sqlDatabase, sqlPassowrd);
		stats = new DRStats(this, connector);
		cosmetics = new CosmeticsManager(connector, plugin);
		
		core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		mvAPI = core.getApi();
		worldManager = mvAPI.getWorldManager();
		mapManager = new MapManager(this);
		mainGameEngine = new MainGameEngine(this);
		scoreboardManager = getServer().getScoreboardManager();
		
		loadCommands();
		registerListeners();
		getLogger().info("Enabled DeathRun");
	}
	
	@Override
	public void onDisable() {
		mapManager.onDisable();
	}
	
	private void loadCredentials() {
		saveDefaultConfig();
		sqlUser = getConfig().getString("sql_credentials.user");
		sqlHost = getConfig().getString("sql_credentials.host");
		sqlDatabase = getConfig().getString("sql_credentials.database");
		sqlPassowrd = getConfig().getString("sql_credentials.password");
		sqlPort = getConfig().getInt("sql_credentials.port");
	}
	
	private void loadCommands() {
		StaffCommands staffCommands = new StaffCommands(this);
		TrapCommands trapCommands = new TrapCommands(this);
		TrapCommandCompleter trapCompleter = new TrapCommandCompleter();
		StaffCommandCompleter staffCompleter = new StaffCommandCompleter();
		getCommand("testtrap").setExecutor(new TestTrapCommand(this));
		getCommand("gm").setExecutor(staffCommands);
		getCommand("dms").setExecutor(staffCommands);
		getCommand("dms").setTabCompleter(staffCompleter);
		getCommand("cpd").setExecutor(staffCommands);
		getCommand("traptool").setExecutor(trapCommands);
		getCommand("traptool").setTabCompleter(trapCompleter);
		getCommand("savetrap").setExecutor(trapCommands);
		getCommand("trapmanager").setExecutor(trapCommands);
		getCommand("testheads").setExecutor(staffCommands);
		getCommand("preshow").setExecutor(staffCommands);
		getCommand("testspawn").setExecutor(staffCommands);
		getCommand("swt").setExecutor(staffCommands);
		getCommand("ssw").setExecutor(staffCommands);
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getServer().getPluginManager().registerEvents(new BlockListeners(), this);
		getServer().getPluginManager().registerEvents(new EntityTrapListeners(), this);
		getServer().getPluginManager().registerEvents(new ConnectionListeners(this), this);
	}
}
