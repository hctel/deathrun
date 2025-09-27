package be.hctel.renaissance.deathrun.traps;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.hctel.api.Utils;

public enum TrapType {
	WALL_SPAWN(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Block b = workLocation.getBlock();
				trap.getBlockStateList().add(b.getState());
				b.setType(Material.GOLD_BLOCK);
				workLocation.add(crossVector);
			}
		}
		
	}, false, true, TrapOrientation.VERTICAL, Material.GOLD_BLOCK),
	LAUNCH_PLAYERS(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			if(stepnr == 0) {
				ArmorStand centerStand = (ArmorStand) trap.getCenter().getWorld().spawnEntity(trap.getCenter(), EntityType.ARMOR_STAND);
				centerStand.setVisible(false);
				centerStand.setBasePlate(false);
				centerStand.setInvulnerable(true);
				centerStand.setGravity(false);
				List<Entity> nearbyEntities = centerStand.getNearbyEntities(trap.getArea().getX()/2, 5, trap.getArea().getZ()/2);
				for(Entity E : nearbyEntities) {
					if(E instanceof Player) {
						E.setVelocity(E.getVelocity().setY(15));
					}
				}
				centerStand.remove();
			}
		}
		
	}, true, false, TrapOrientation.HORIZONTAL, Material.PISTON, 1),
	COBBLE_STAIRS(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Location workLocationDeep = workLocation.clone();
				for(int h = Math.min(height, 0); h < Math.max(0, height); h++) {
					Block b = workLocationDeep.getBlock();
					trap.getBlockStateList().add(b.getState());
					if(b.getType() == Material.COBBLESTONE_STAIRS) b.setType(Material.AIR);
					workLocationDeep.add(0,Integer.signum(height),0);
				}
				workLocation.add(crossVector);
			}
		}
		
	}, false, true, TrapOrientation.TRID, Material.COBBLESTONE_STAIRS),
	TNT(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector,
				Trap trap) {
			Location workLocation = trap.getCenter();
			workLocation.getWorld().spawnParticle(Particle.EXPLOSION, workLocation.clone().add(0,1.5,0), 5, trap.getArea().getX()/5, 1, trap.getArea().getZ()/5);
			workLocation.getWorld().playSound(workLocation, Sound.ENTITY_GENERIC_EXPLODE, 5f, 1f);
			for(Entity E : workLocation.getWorld().getNearbyEntities(workLocation, trap.getArea().getX()/2, 5, trap.getArea().getZ()/2)) {
				if(E instanceof Player) {
					Player player = (Player) E;
					trap.getPlugin().mainGameEngine.killPlayer(player);
				}
			}
		}
		
	}, true, false, TrapOrientation.HORIZONTAL, Material.TNT, 1),
	FIRE_FLOOR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector,
				Trap trap) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Block b = workLocation.getBlock();
				if(b.getType() == Material.AIR) {
					trap.getBlockStateList().add(b.getState());
					b.setType(Material.FIRE);
				}
				workLocation.add(crossVector);
			}
		}
	}, true, true, TrapOrientation.HORIZONTAL, Material.FLINT_AND_STEEL),
	FLOOD_FLOOR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector,
				Trap trap) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Block b = workLocation.getBlock();
				if(b.getType() == Material.AIR) {
					trap.getBlockStateList().add(b.getState());
					b.setType(Material.WATER);
				}
				workLocation.add(crossVector);
			}
		}
	}, true, true, TrapOrientation.HORIZONTAL, Material.WATER_BUCKET),
	FIRE_ARROWS(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			Location workLocation = point.clone();
			ArmorStand armorStand = (ArmorStand) workLocation.getWorld().spawnEntity(workLocation, EntityType.ARMOR_STAND);
			armorStand.setVisible(false);
			armorStand.setInvulnerable(true);
			armorStand.setVisible(false);
			for(int i = 0; i < width; i++) {
				Location workLocationDeep = workLocation.clone();
				for(int h = Math.min(height, 0); h < Math.max(0, height); h++) {
					Block b = workLocationDeep.getBlock();
					if(b.getType() == Material.DISPENSER) {
						Vector fireDirection = ((Dispenser) b.getBlockData()).getFacing().getDirection();
						armorStand.teleport(workLocationDeep.clone().add(fireDirection).add(0,-1,0));
						Arrow a = armorStand.launchProjectile(Arrow.class, fireDirection);
						a.setFireTicks(10*20);
						a.setVelocity(fireDirection.clone().multiply(5));
						a.setSilent(true);
						a.getWorld().playSound(a.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1f, 1f);
						new BukkitRunnable() {

							@Override
							public void run() {
								a.remove();
							}
							
						}.runTaskLater(trap.getPlugin(), 5L);
						
					}
					workLocationDeep.add(0, Math.signum(height), 0);
				}
				workLocation.add(crossVector);
			}
			armorStand.remove();
		}
	}, true, false, TrapOrientation.TRID, Material.ARROW),
	PARKOUR_DISAPPEAR(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Location workLocationDeep = workLocation.clone();
				for(int h = Math.min(height, 0); h < Math.max(0, height); h++) {
					Block b = workLocationDeep.getBlock();
					trap.getBlockStateList().add(b.getState());
					if(b.getType() == Material.RED_TERRACOTTA) b.setType(Material.AIR);
					workLocationDeep.add(0,Integer.signum(height),0);
				}
				workLocation.add(crossVector);
			}
		}
		
	}, false, true, TrapOrientation.TRID, Material.RED_TERRACOTTA),
	LANTERN_DISAPPEAR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {	
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Block b = workLocation.getBlock();
				trap.getBlockStateList().add(b.getState());
				if(b.getType() == Material.SEA_LANTERN) b.setType(workLocation.clone().add(0, -1, 0).getBlock().getType());
				workLocation.add(crossVector);
			}
		}
	}, false, true, TrapOrientation.HORIZONTAL, Material.SEA_LANTERN),	
	
	LADDER_DISPAWN(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			Location workLocation = point.clone();
			for(int i = 0; i < width; i++) {
				Block b = workLocation.getBlock();
				trap.getBlockStateList().add(b.getState());
				b.setType(Material.AIR);
				workLocation.add(crossVector);
			}
		}
		
	}, false, true, TrapOrientation.VERTICAL, Material.LADDER),
	
	FIRE_TRAIL(new TrapMethod() {

		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {
			Location workLocation = point.clone().add(0.5, 0.5, 0.5);
			for(int i = 0; i < width; i++) {
				workLocation.getWorld().spawnParticle(Particle.FLAME, workLocation, 2, 0.25, 0.25, 0.25, 0);
				for(Entity E : workLocation.getWorld().getNearbyEntities(workLocation, 1, 0.5, 1)) {
					if(E instanceof Player) {
						Player player = (Player) E;
						trap.getPlugin().mainGameEngine.killPlayer(player);
					}
				}
				workLocation.add(crossVector);
			}
		}
		
	}, true, false, TrapOrientation.HORIZONTAL, Material.DISPENSER),
	
	GLASS_FLOOR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int height, int stepnr, Vector direction, Vector crossVector, Trap trap) {	
			Location workLocation = point.clone();
			point.getWorld().playSound(point, Sound.BLOCK_NOTE_BLOCK_PLING, 5f, (float) (0.5+(((float)stepnr)/20f)));
			for(int i = 0; i < width; i++) {
				trap.getBlockStateList().add(workLocation.getBlock().getState());
				Utils.transformToFallingBlock(workLocation.getBlock());
				workLocation.add(crossVector);
			}
		}
	}, true, true, TrapOrientation.HORIZONTAL, Material.WHITE_STAINED_GLASS);
	
	
	
	private TrapMethod method;
	private boolean silent;
	private boolean resets;
	private TrapOrientation orientation;
	private Material guiRepresentation;
	private int forceSteps = -1;
	
	private TrapType(TrapMethod method, boolean silent, boolean resets, TrapOrientation orientation, Material guiRepresentation) {
		this.method = method;
		this.silent = silent;
		this.resets = resets;
		this.orientation = orientation;
		this.guiRepresentation = guiRepresentation;
	}
	
	private TrapType(TrapMethod method, boolean silent, boolean resets, TrapOrientation orientation, Material guiRepresentation, int forceSteps) {
		this.method = method;
		this.silent = silent;
		this.resets = resets;
		this.orientation = orientation;
		this.forceSteps = forceSteps;
		this.guiRepresentation = guiRepresentation;
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
	
	public Material getGuiRepresentation() {
		return this.guiRepresentation;
	}
	
	public int getForceSteps() {
		return this.forceSteps;
	}
	
	public String getFamiliarName() {
		return Utils.capitalize(this.toString().replace("_", " ").toLowerCase());
	}
	
}
