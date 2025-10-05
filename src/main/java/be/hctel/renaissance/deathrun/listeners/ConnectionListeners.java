package be.hctel.renaissance.deathrun.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import be.hctel.renaissance.deathrun.DeathRun;

public class ConnectionListeners implements Listener {
	DeathRun plugin;
	
	public ConnectionListeners(DeathRun plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		plugin.stats.loadPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		plugin.stats.unloadPlayer(e.getPlayer());
	}
}
