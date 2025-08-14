package be.hctel.renaissance.deathrun;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import be.hctel.renaissance.deathrun.commands.TestTrapCommand;

public class DeathRun extends JavaPlugin {
	
	public static String header = "§8▍ §§4Death§cRun§8 ▏ ";
	
	public static Plugin plugin;

	@Override
	public void onEnable() {
		getLogger().info("Enabling DeathRun");
		plugin = this;
		loadCommands();
		getLogger().info("Enabled DeathRun");
	}
	
	@Override
	public void onDisable() {
		
	}
	
	private void loadCommands() {
		getCommand("testtrap").setExecutor(new TestTrapCommand());
	}
}
