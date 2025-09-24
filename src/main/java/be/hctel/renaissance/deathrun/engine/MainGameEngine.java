package be.hctel.renaissance.deathrun.engine;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import be.hctel.renaissance.deathrun.DeathRun;

public class MainGameEngine {
	
	DeathRun plugin;
	
	public MainGameEngine(DeathRun plugin) {
		this.plugin = plugin;
	}

	public void killPlayer(Player player) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		Location toTeleport = player.getRespawnLocation();
		if(toTeleport == null) toTeleport = plugin.mapManager.getMap(player.getWorld()).getSpawn();
		player.teleport(toTeleport);
		player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 10f, 1f);
		player.sendTitle("§4§l✖", "You died!", 0, 50, 20);
	}
	
	public Role getRole(Player player) {
		return Role.TEST;
	}
	
}
