package be.hctel.renaissance.deathrun.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class BlockListeners implements Listener {
	@EventHandler
	public void onBlockForm(EntityChangeBlockEvent e) {
		if(e.getTo() != Material.AIR) {
			e.setCancelled(true);
			e.getEntity().remove();
		}

	}
		
	
	@EventHandler
	public void onItemSpawn(EntitySpawnEvent e) {
		if(e.getEntityType() == EntityType.ITEM) e.setCancelled(true); 
	}
}
