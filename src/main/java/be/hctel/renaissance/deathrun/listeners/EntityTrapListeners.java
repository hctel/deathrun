package be.hctel.renaissance.deathrun.listeners;

import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.engine.Role;

public class EntityTrapListeners implements Listener {
	DeathRun plugin;
	public EntityTrapListeners(DeathRun plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onHit(ProjectileHitEvent e) {
		if(e.getEntity() instanceof Arrow && e.getEntity().getShooter() instanceof ArmorStand) {
			e.getEntity().remove();
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Giant) {
			if(e.getCause() == DamageCause.FALL) {
				if(e.getEntity().getCustomName().startsWith("owo giant trap owo")) {
					double x = Math.abs(Double.parseDouble(e.getEntity().getCustomName().split("/")[1]));
					double z = Math.abs(Double.parseDouble(e.getEntity().getCustomName().split("/")[2]));
					for(Entity E : e.getEntity().getNearbyEntities(x, 5, z)) {
						if(E instanceof Player) {
							if(plugin.mainGameEngine.getRole((Player) E) == Role.RUNNER) {
								plugin.mainGameEngine.killPlayer((Player) E);
							}
						}
					}
					new BukkitRunnable() {
						@Override
						public void run() {
							e.setCancelled(true);
							e.getEntity().remove();
						}
					}.runTaskLater(plugin, 1L);
				}
			}
		}
	}
}
