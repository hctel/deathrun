package be.hctel.renaissance.deathrun.engine;

import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MainGameEngine {

	public static void killPlayer(Player player) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		player.teleport(player.getBedSpawnLocation());
		player.playSound(player.getLocation(), Sound.ENTITY_IRONGOLEM_HURT, 10f, 1f);
		player.sendTitle("§4§l✖", "You died!", 0, 50, 20);
	}
	
}
