package be.hctel.renaissance.deathrun;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolManager;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import be.hctel.api.bungee.BungeeCordMessenger;
import be.hctel.renaissance.deathrun.nongame.utils.MapLoader;

public class DeathRun extends JavaPlugin    {
	public static String header = "§8▍ §cDeath§4Run ▏ ";
	
	public static Plugin plugin;
	public static MultiverseCore core;
	public static MVWorldManager worldManager;
	public static ProtocolManager protocolLibManager;
	public static BungeeCordMessenger bm;
	public static MapLoader mapLoader;
}
