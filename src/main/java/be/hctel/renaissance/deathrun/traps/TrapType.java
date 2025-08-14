package be.hctel.renaissance.deathrun.traps;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.engine.MainGameEngine;

public enum TrapType {
	FIRE_TRAIL(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int stepnr, Vector direction, Vector crossVector) {
			Location workLocation = point.clone().add(0.5, 0.5, 0);
			for(int i = 0; i < width; i++) {
				workLocation.getWorld().spawnParticle(Particle.FLAME, workLocation, 1, 0, 0, 0, 0);
				for(Entity E : workLocation.getWorld().getNearbyEntities(workLocation, 1, 1, 1)) {
					if(E instanceof Player) {
						Player player = (Player) E;
						MainGameEngine.killPlayer(player);
					}
				}
				workLocation.add(crossVector);
			}
		}
		
	}, false),
	GLASS_FLOOR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int stepnr, Vector direction, Vector crossVector) {	
			Location workLocation = point.clone();
			point.getWorld().playSound(point, Sound.BLOCK_NOTE_PLING, 5f, (float) (0.5+(((float)stepnr)/20f)));
			for(int i = 0; i < width; i++) {
				Utils.transformToFallingBlock(workLocation.getBlock());
				workLocation.add(crossVector);
			}
		}
	}, true);
	
	private TrapMethod method;
	private boolean resets;
	private TrapType(TrapMethod method, boolean resets) {
		this.method = method;
		this.resets = resets;
	}
	
	public TrapMethod getMethod() {
		return method;
	};
	
	public boolean doesReset() {
		return this.resets;
	}
	
	
}
