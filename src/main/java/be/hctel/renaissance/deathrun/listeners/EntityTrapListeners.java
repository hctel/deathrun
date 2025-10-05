package be.hctel.renaissance.deathrun.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EntityTrapListeners implements Listener {
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof ArmorStand) {
			e.getEntity().remove();
		}
	}
}
