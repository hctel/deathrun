package be.hctel.renaissance.deathrun.traps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.engine.MainGameEngine;

public enum TrapType {
	PARKOUR_DISAPPEAR(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Location workLocationDeep = workLocation.clone();
				for(int h = Math.min(height, 0); h < Math.max(0, height); h++) {
					Block b = workLocationDeep.getBlock();
					if(b.getType() == Material.STAINED_CLAY && b.getData() == (byte) 14) b.setType(Material.AIR);
					workLocationDeep.add(0,Integer.signum(height),0);
				}
				workLocation.add(crossVector);
			}
		}
		
	}, false, true, TrapOrientation.TRID),
	LANTERN_DISAPPEAR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector) {	
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Block b = workLocation.getBlock();
				if(b.getType() == Material.SEA_LANTERN) b.setType(workLocation.clone().add(0, -1, 0).getBlock().getType());
				workLocation.add(crossVector);
			}
		}
	}, false, true, TrapOrientation.HORIZONTAL),	
	
	LADDER_DISPAWN(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				workLocation.getBlock().setType(Material.AIR);
				workLocation.add(crossVector);
			}
		}
		
	}, false, true, TrapOrientation.VERTICAL),
	
	FIRE_TRAIL(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector) {
			Location workLocation = point.clone().add(0.5, 0.5, 0);
			for(int i = 0; i < width; i++) {
				workLocation.getWorld().spawnParticle(Particle.FLAME, workLocation, 1, 0, 0, 0, 0);
				for(Entity E : workLocation.getWorld().getNearbyEntities(workLocation, 1, 0.5, 1)) {
					if(E instanceof Player) {
						Player player = (Player) E;
						MainGameEngine.killPlayer(player);
					}
				}
				workLocation.add(crossVector);
			}
		}
		
	}, true, false, TrapOrientation.HORIZONTAL),
	
	GLASS_FLOOR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector) {	
			Location workLocation = point.clone();
			point.getWorld().playSound(point, Sound.BLOCK_NOTE_PLING, 5f, (float) (0.5+(((float)stepnr)/20f)));
			for(int i = 0; i < width; i++) {
				Utils.transformToFallingBlock(workLocation.getBlock());
				workLocation.add(crossVector);
			}
		}
	}, true, true, TrapOrientation.HORIZONTAL);
	
	
	
	private TrapMethod method;
	private boolean silent;
	private boolean resets;
	private TrapOrientation orientation;
	
	private TrapType(TrapMethod method, boolean silent, boolean resets, TrapOrientation orientation) {
		this.method = method;
		this.silent = silent;
		this.resets = resets;
		this.orientation = orientation;
	}
	
	public TrapMethod getMethod() {
		return method;
	}
	
	public boolean isSilent() {
		return this.silent;
	}
	
	public boolean doesReset() {
		return this.resets;
	}
	
	public TrapOrientation getOrientation() {
		return this.orientation;
	}
	
	
}
