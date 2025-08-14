package be.hctel.renaissance.deathrun;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import be.hctel.renaissance.deathrun.commands.TestTrapCommand;
import be.hctel.renaissance.deathrun.listeners.PlayerListener;

public class DeathRun extends JavaPlugin {
	
	public static String header = "§8▍ §§4Death§cRun§8 ▏ ";
	
	public static Plugin plugin;

	@Override
	public void onEnable() {
		getLogger().info("Enabling DeathRun");
		plugin = this;
		loadCommands();
		registerListeners();
		getLogger().info("Enabled DeathRun");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void loadCommands() {
		getCommand("testtrap").setExecutor(new TestTrapCommand());
	}
	
	private void registerListeners() {
		getServer().getPluginManager().registerEvents(new PlayerListener(), plugin);
	}
}
