package be.hctel.renaissance.deathrun;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.mvplugins.multiverse.core.MultiverseCore;
import org.mvplugins.multiverse.core.MultiverseCoreApi;
import org.mvplugins.multiverse.core.world.WorldManager;

import be.hctel.renaissance.deathrun.commands.StaffCommands;
import be.hctel.renaissance.deathrun.commands.TestTrapCommand;
import be.hctel.renaissance.deathrun.commands.TrapCommands;
import be.hctel.renaissance.deathrun.engine.MainGameEngine;
import be.hctel.renaissance.deathrun.listeners.BlockListeners;
import be.hctel.renaissance.deathrun.listeners.PlayerListener;
import be.hctel.renaissance.global.mapmanager.MapManager;

public class DeathRun extends JavaPlugin {
	
	public String header = "§8▍ §§4Death§cRun§8 ▏ ";
	
	public Plugin plugin;
	
	public MultiverseCore core;
	public MultiverseCoreApi mvAPI;
	public WorldManager worldManager;
	
	public MapManager mapManager; 
	public MainGameEngine mainGameEngine;

	@Override
	public void onEnable() {
		getLogger().info("Enabling DeathRun");
		
		plugin = this;
		
		core = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
		mvAPI = core.getApi();
		worldManager = mvAPI.getWorldManager();
		
		mapManager = new MapManager(this);
		
		loadCommands();
		registerListeners();
		getLogger().info("Enabled DeathRun");
	}
	
	@Override
	public void onDisable() {
		mapManager.onDisable();
	}
	
	private void loadCommands() {
		StaffCommands staffCommands = new StaffCommands(this);
		TrapCommands trapCommands = new TrapCommands(this);
		getCommand("testtrap").setExecutor(new TestTrapCommand(this));
		getCommand("gm").setExecutor(staffCommands);
		getCommand("dms").setExecutor(staffCommands);
		getCommand("traptool").setExecutor(trapCommands);
		getCommand("savetrap").setExecutor(trapCommands);
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new PlayerListener(this), plugin);
		getServer().getPluginManager().registerEvents(new BlockListeners(), plugin);
	}
}
