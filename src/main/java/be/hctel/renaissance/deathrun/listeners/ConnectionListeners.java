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
		plugin.ranks.load(e.getPlayer());
		plugin.cosmetics.loadPlayer(e.getPlayer());
		plugin.preGameTimer.loadPlayer(e.getPlayer());
		plugin.votesHandler.sendMapChoices(e.getPlayer());
		e.getPlayer().teleport(plugin.getServer().getWorld("DR_Hub").getSpawnLocation());
	}
	
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		plugin.stats.unloadPlayer(e.getPlayer());
		plugin.ranks.unLoad(e.getPlayer());
		plugin.cosmetics.unloadPlayer(e.getPlayer());
	}
}
