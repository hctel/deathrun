package be.hctel.renaissance.deathrun.traps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Trap {
	private Plugin plugin;
	
	private TrapType type;
	
	private Location startLocation;
	private Location stopLocation;
	private Location modelStartLocation;
	
	private long trapReset;
	private long trapCooldown;
	
	private Vector trapArea;
	
	private Vector travelDirection;
	private Vector crossVector;
	private Vector travelStep;
	
	Location workLocation;
	private int steps;
	private long delay;
	private TrapMethod method;
	
	private int xSize;
	private int ySize;
	
	private int width;
	private int length;
	
	
	private double stepDistance;
	
	private int stepnr = 0;
	
	private BukkitRunnable trapTask;
	private BukkitRunnable resetTask;
	
	public Trap(Plugin plugin, Location startLocation, Location stopLocation, Location modelLocation, int width, int steps, long delay, TrapType type, long trapReset, long trapCooldown) {
		this.plugin = plugin;
		this.type = type;
		this.startLocation = startLocation;
		this.stopLocation = stopLocation;
		this.modelStartLocation = modelLocation;
		this.steps = steps;
		this.delay = delay;
		this.method = this.type.getMethod();
		this.trapReset = trapReset;
		this.trapCooldown = trapCooldown;
		
		
		this.xSize = stopLocation.clone().subtract(startLocation).getBlockX();
		this.ySize = stopLocation.clone().subtract(startLocation).getBlockY();
		this.width = width;
		this.trapArea = stopLocation.clone().subtract(startLocation).toVector();
		this.length = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? this.trapArea.getBlockZ() : this.trapArea.getBlockX());
		
		this.travelDirection = (this.trapArea.getBlockX() == this.width-1 ? new Vector(0, 0, this.trapArea.getBlockZ()/Math.abs(this.trapArea.getBlockZ())) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
		this.crossVector = travelDirection.clone().getCrossProduct(new Vector(0,1,0));
		this.stepDistance = Math.abs(((double) this.length)/((double) this.steps));
		this.travelStep = travelDirection.clone();
		this.travelStep.multiply(this.stepDistance);
		this.workLocation = startLocation.clone();
		
		setupTasks();
		
	}
	
	public void startTrap() {
		trapTask.runTaskTimer(plugin, 0L, delay);
		if(type.doesReset()) resetTask.runTaskLater(plugin, delay*(steps+2)+trapReset);
	}
	
	public String getTrapDetails() {
		return String.format("Trap origin: %d, %d, %d\nTrap end: %d, %d, %d\nTrap area: (%d, %d)\nTrap width: %d\nTrap length: %d\nDirection of travel: (%d, %d, %d)", startLocation.getBlockX(), startLocation.getBlockY(), startLocation.getBlockZ(), stopLocation.getBlockX(), stopLocation.getBlockY(), stopLocation.getBlockZ(), trapArea.getBlockX(), trapArea.getBlockZ(), width, length, travelDirection.getBlockX(), travelDirection.getBlockY(), travelDirection.getBlockZ());
	}
	
	private void setupTasks() {
		resetTask = new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				System.out.println("RESET TRAP!!!");
				Location workLoc = startLocation.clone();
				Location modelWorkLoc = modelStartLocation.clone();
				for(int i = 0; i < Math.abs(length)+1; i++) {
					Location workLocDeep = workLoc.clone();
					Location modelWorkLocDeep = modelWorkLoc.clone();
					for(int j = 0; j < Math.abs(width); j++) {
						Material modelMat = modelWorkLocDeep.getBlock().getType();
						byte modelData = modelWorkLocDeep.getBlock().getData();
						Block targetBlock = workLocDeep.getBlock();
						targetBlock.setType(modelMat);
						targetBlock.setData(modelData);
						//targetBlock.getState().update();
						workLocDeep.add(crossVector);
						modelWorkLocDeep.add(crossVector);
						System.out.println(String.format("Cloning to (%d, %d, %d) from (%d, %d, %d), data = %d", workLocDeep.getBlockX(), workLocDeep.getBlockY(), workLocDeep.getBlockZ(), modelWorkLocDeep.getBlockX(), modelWorkLocDeep.getBlockY(), modelWorkLocDeep.getBlockZ(), (int) modelData));
					}
					workLoc.add(travelDirection);
					modelWorkLoc.add(travelDirection);
				}
			}			
		};
		
		trapTask = new BukkitRunnable() {

			@Override
			public void run() {
				method.trapStep(workLocation, width, stepnr, travelDirection, crossVector);
				workLocation.add(travelStep);
				stepnr++;
				if(stepnr == steps) cancel();
			}
			
		};
	}
	
}
