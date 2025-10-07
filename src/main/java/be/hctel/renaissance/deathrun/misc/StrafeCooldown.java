package be.hctel.renaissance.deathrun.misc;

import org.bukkit.entity.Player;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;

public class StrafeCooldown {
	private DeathRun plugin;
	private Player player;
	private int counter;
	private StrafeDirection direction;
	
	public StrafeCooldown(Player player, int cooldown, StrafeDirection direction, DeathRun plugin) {
		this.player = player;
		this.counter = cooldown;
		this.direction = direction;
		this.plugin = plugin;
	}
	
	public boolean step() {
		if(counter == 0) player.sendMessage(plugin.header + "§aStrafe available");
		switch(direction) {
		case BACKWARDS:
			if(counter > 0) {
				player.getInventory().setItem(4, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getDisabledBackTextureURL(), "§7§lOn cooldown", counter));
				counter--;
			} else {
				player.getInventory().setItem(4, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getBackTextureURL(), "§a§lStrafe Backwards"));
				return true;
			}
			break;
		case LEAP:
			break;
		case LEFT:
			if(counter > 0) {
				player.getInventory().setItem(3, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getDisabledLeftTextureURL(), "§7§lOn cooldown", counter));
				counter--;
			} else {
				player.getInventory().setItem(3, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getLeftTextureURL(), "§a§lStrafe Left"));
				return true;
			}
			break;
		case RIGHT:
			if(counter > 0) {
				player.getInventory().setItem(5, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getDisabledRightTextureURL(), "§7§lOn cooldown", counter));
				counter--;
			} else {
				player.getInventory().setItem(5, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getRightTextureURL(), "§a§lStrafe Right"));
				return true;
			}
			break;
		default:
			break;
		
		}
		return false;
	}
}

