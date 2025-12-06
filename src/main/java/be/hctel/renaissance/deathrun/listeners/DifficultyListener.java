package be.hctel.renaissance.deathrun.listeners;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import be.hctel.renaissance.deathrun.DeathRun;

public class DifficultyListener implements Listener {
	DeathRun plugin;
	
	public DifficultyListener(DeathRun plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onEntitySpawnEvent(EntitySpawnEvent e) {
		if(e.getEntity() instanceof Monster) {
			if(e.getEntityType() != EntityType.GIANT) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent e) {
		e.getEntity().setFoodLevel(20);
		e.getEntity().setSaturation(20);		
		e.setCancelled(true);
	}

}
