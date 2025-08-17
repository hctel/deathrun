package be.hctel.renaissance.deathrun.listeners;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import be.hctel.renaissance.deathrun.engine.MainGameEngine;


public class PlayerListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		switch(e.getTo().clone().add(0,-1,0).getBlock().getType()) {	
		case EMERALD_BLOCK: {
			Location location = e.getTo().clone().subtract(0, 1, 0);
			for(int x = -1; x < 2; x++) {
				for(int y = -1; y < 2; y++) {
					for(int z = -1; z < 2; z++) {
						Block block = location.clone().add(x, y, z).getBlock();
						if(block.getState() instanceof Sign) {
							Sign sign = (Sign) block.getState();
							int strenght = Integer.parseInt(sign.getLine(0));
							int duration = Integer.parseInt(sign.getLine(1));
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, duration*20, strenght, false, false));
						}
					}
				}
			}
			break;
		}
		case REDSTONE_BLOCK: {
			Location location = e.getTo().clone().subtract(0, 1, 0);
			for(int x = -1; x < 2; x++) {
				for(int y = -1; y < 2; y++) {
					for(int z = -1; z < 2; z++) {
						Block block = location.clone().add(x, y, z).getBlock();
						if(block.getState() instanceof Sign) {
							Sign sign = (Sign) block.getState();
							int strenght = Integer.parseInt(sign.getLine(0));
							int duration = Integer.parseInt(sign.getLine(1));
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration*20, strenght, false, false));
						}
					}
				}
			}
			break;
		}
		default: break;
		}
		if(e.getPlayer().isInWater()) MainGameEngine.killPlayer(e.getPlayer());
	}
}
