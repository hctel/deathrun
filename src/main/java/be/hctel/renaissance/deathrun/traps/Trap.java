package be.hctel.renaissance.deathrun.traps;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Trap {
	private Trap thisTrap;
	
	private Plugin plugin;
	
	private TrapType type;
	
	private Location startLocation;
	private Location stopLocation;
	private long trapReset;
	@SuppressWarnings("unused")
	private long trapCooldown;
	
	private Vector trapArea;
	
	private Vector travelDirection;
	private Vector crossVector;
	private Vector travelStep;
	
	Location workLocation;
	private int steps;
	private long delay;
	private TrapMethod method;
	
	private int ySize;
	
	private int width;
	private int length;
	private int height = 1;
	
	private double stepDistance;
	
	private int stepnr = 0;
	
	private BukkitRunnable trapTask;
	private BukkitRunnable resetTask;
	
	private ArrayList<BlockState> changedBlocks = new ArrayList<BlockState>();
	
	/**
	 * Creates an horizontal non-resetting trap
	 * @param plugin this plugin ({@link org.bukkit.plugin.Plugin})
	 * @param startLocation the trap's origin {@link org.bukkit.Location}
	 * @param stopLocation the trap's final {@link org.bukkit.Location}
	 * @param width the width of the trap in blocks
	 * @param steps the number of steps
	 * @param delay the delay in ticks between each step
	 * @param type the {@link TrapRype}
	 * @param trapReset the trap reset delay (delay before the trap resets)
	 * @param trapCooldown the trap cooldown delay (delay before the trap can be used again)
	 *//*
	public Trap(Plugin plugin, Location startLocation, Location stopLocation, int width, int steps, long delay, TrapType type, long trapReset, long trapCooldown) {
		this.plugin = plugin;
		this.type = type;
		this.startLocation = startLocation;
		this.stopLocation = stopLocation;
		this.steps = steps;
		this.delay = delay;
		this.method = this.type.getMethod();
		this.trapReset = trapReset;
		this.trapCooldown = trapCooldown;
		
		
		stopLocation.clone().subtract(startLocation).getBlockX();
		this.ySize = stopLocation.clone().subtract(startLocation).getBlockY();
		this.width = width;
		this.trapArea = stopLocation.clone().subtract(startLocation).toVector();
		this.length = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? this.trapArea.getBlockZ() : this.trapArea.getBlockX());
		
		this.travelDirection = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? new Vector(0, 0, this.trapArea.getBlockZ()/Math.abs(this.trapArea.getBlockZ())) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
		this.crossVector = travelDirection.clone().getCrossProduct(new Vector(0,1,0));
		this.stepDistance = Math.abs(((double) this.length)/((double) this.steps-1));
		this.travelStep = travelDirection.clone();
		this.travelStep.multiply(this.stepDistance);
		this.workLocation = startLocation.clone();
		
		setupTasks();
		this.thisTrap = this;
	}*/
	
	/**
	 * Creates a vertical resetting trap
	 * @param plugin this plugin ({@link org.bukkit.plugin.Plugin})
	 * @param startLocation the trap's origin {@link org.bukkit.Location}
	 * @param stopLocation the trap's final {@link org.bukkit.Location}
	 * @param steps the number of steps
	 * @param delay the delay in ticks between each step
	 * @param type the {@link TrapRype}
	 * @param trapReset the trap reset delay (delay before the trap resets)
	 * @param trapCooldown the trap cooldown delay (delay before the trap can be used again)
	 * 
	 * @throws {@link IllegalArgumentException} if the {@link TrapType}'s getOrientation() method returns a vertical trap type. <b>OR</b> if the trap's dimmensions arent' vertical
	 */
	public Trap(Plugin plugin, Location startLocation, Location stopLocation, int steps, long delay, TrapType type, long trapReset, long trapCooldown) {
		if(type.getOrientation() != TrapOrientation.VERTICAL) throw new IllegalArgumentException("TrapType is not a vertical trap");
		//if(Math.abs(stopLocation.getBlockX() - startLocation.getBlockX())+Math.abs(stopLocation.getBlockZ() - startLocation.getBlockZ()) != Math.sqrt(Math.abs(stopLocation.getBlockX() - startLocation.getBlockX())^2+Math.abs(stopLocation.getBlockZ() - startLocation.getBlockZ()^2))) throw new IllegalArgumentException("Trap is not a vertical trap");
		this.plugin = plugin;
		this.type = type;
		this.startLocation = startLocation;
		this.stopLocation = stopLocation;
		this.steps = steps;
		this.delay = delay;
		this.method = this.type.getMethod();
		this.trapReset = trapReset;
		this.trapCooldown = trapCooldown;
		
		stopLocation.clone().subtract(startLocation).getBlockX();
		this.ySize = stopLocation.clone().subtract(startLocation).getBlockY();
		this.width = Math.abs(ySize)+1;
		this.trapArea = stopLocation.clone().subtract(startLocation).toVector();
		this.length = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? this.trapArea.getBlockZ() : this.trapArea.getBlockX());
		this.travelDirection = (this.trapArea.getBlockX() == this.width-1 ? new Vector(0, 0, this.trapArea.getBlockZ()/Math.abs(this.trapArea.getBlockZ())) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
		this.crossVector = new Vector(0, this.trapArea.getBlockY()/Math.abs(this.trapArea.getBlockY()), 0);
		
		this.stepDistance = Math.abs(((double) this.length)/((double) this.steps-1));
		this.travelStep = travelDirection.clone();
		this.travelStep.multiply(this.stepDistance);
		this.workLocation = startLocation.clone();
		
		setupTasks();
		this.thisTrap = this;
	}
	
	public Trap(Plugin plugin, Location startLocation, Location stopLocation, int width, int steps, long delay, TrapType type, long trapReset, long trapCooldown) {
		this.plugin = plugin;
		this.type = type;
		this.startLocation = startLocation;
		this.stopLocation = stopLocation;
		this.steps = steps;
		this.delay = delay;
		this.method = this.type.getMethod();
		this.trapReset = trapReset;
		this.trapCooldown = trapCooldown;
		
		
		stopLocation.clone().subtract(startLocation).getBlockX();
		this.ySize = stopLocation.clone().subtract(startLocation).getBlockY();
		this.width = (type.getOrientation() == TrapOrientation.VERTICAL ? Math.abs(ySize)+1 : width);
		this.trapArea = stopLocation.clone().subtract(startLocation).toVector();
		this.length = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? this.trapArea.getBlockZ() : this.trapArea.getBlockX());
		
		if(type.getOrientation() == TrapOrientation.VERTICAL) {
			this.travelDirection = (this.trapArea.getBlockX() == 0 ? new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
			this.crossVector = new Vector(0, this.trapArea.getBlockY()/Math.abs(this.trapArea.getBlockY()), 0);
		} else {
			this.travelDirection = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? new Vector(0, 0, this.trapArea.getBlockZ()/Math.abs(this.trapArea.getBlockZ())) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
			this.crossVector = travelDirection.clone().getCrossProduct(new Vector(0,1,0));
		}
		this.stepDistance = Math.abs(((double) this.length)/((double) this.steps-1));
		this.travelStep = travelDirection.clone();
		this.travelStep.multiply(this.stepDistance);
		this.workLocation = startLocation.clone();
		this.width = Math.abs(this.width);
		setupTasks();
		this.thisTrap = this;
	}
	
	public Trap(Plugin plugin, Location startLocation, Location stopLocation, int width, int height, int steps, long delay, TrapType type, long trapReset, long trapCooldown) {
		this.plugin = plugin;
		this.type = type;
		this.startLocation = startLocation;
		this.stopLocation = stopLocation;
		this.steps = steps;
		this.delay = delay;
		this.method = this.type.getMethod();
		this.trapReset = trapReset;
		this.trapCooldown = trapCooldown;
		this.height = Math.abs(height);
		
		this.height = (startLocation.getBlockY() < stopLocation.getBlockY() ? this.height : -this.height);		
		
		stopLocation.clone().subtract(startLocation).getBlockX();
		this.ySize = stopLocation.clone().subtract(startLocation).getBlockY();
		this.width = (type.getOrientation() == TrapOrientation.VERTICAL ? Math.abs(ySize)+1 : width);
		this.trapArea = stopLocation.clone().subtract(startLocation).toVector();
		this.length = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? this.trapArea.getBlockZ() : this.trapArea.getBlockX());
		
		if(type.getOrientation() == TrapOrientation.VERTICAL) {
			this.travelDirection = (this.trapArea.getBlockX() == 0 ? new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
			this.crossVector = new Vector(0, Integer.signum(trapArea.getBlockY()), 0);
		} else {
			this.travelDirection = (Math.abs(this.trapArea.getBlockX()) == this.width-1 ? new Vector(0, 0, this.trapArea.getBlockZ()/Math.abs(this.trapArea.getBlockZ())) : new Vector(this.trapArea.getBlockX()/Math.abs(this.trapArea.getBlockX()), 0, 0));
			this.crossVector = travelDirection.clone().getCrossProduct(new Vector(0,1,0));
		}
		this.stepDistance = Math.abs(((double) this.length)/((double) this.steps-1));
		this.travelStep = travelDirection.clone();
		this.travelStep.multiply(this.stepDistance);
		this.workLocation = startLocation.clone();
		this.width = Math.abs(this.width);
		setupTasks();
		this.thisTrap = this;
	}
	
	public void startTrap() {
		if(delay == 0) trapTask.runTask(plugin);
		else trapTask.runTaskTimer(plugin, 0L, delay);
		if(type.doesReset()) resetTask.runTaskLater(plugin, delay*(steps+2)+trapReset);
	}
	
	public ArrayList<BlockState> getBlockStateList() {
		return this.changedBlocks;
	}
	
	@Override
	public String toString() {
		return String.format("Trap origin: %d, %d, %d\nTrap end: %d, %d, %d\nTrap area: (%d, %d)\nTrap width: %d\nTrap length: %d\nDirection of travel: (%d, %d, %d)\nTravel step: (%f, %f, %f)\nCross Vector: (%d, %d, %d)\nTrap type: %s", startLocation.getBlockX(), startLocation.getBlockY(), startLocation.getBlockZ(), stopLocation.getBlockX(), stopLocation.getBlockY(), stopLocation.getBlockZ(), trapArea.getBlockX(), trapArea.getBlockZ(), width, length, travelDirection.getBlockX(), travelDirection.getBlockY(), travelDirection.getBlockZ(), travelStep.getX(), travelStep.getY(), travelStep.getZ(), crossVector.getBlockX(), crossVector.getBlockY(), crossVector.getBlockZ(), type.toString());
	}
	
	private void setupTasks() {
		if(type.doesReset()) {
			resetTask = new BukkitRunnable() {
				@Override
				public void run() {
					for(BlockState B : changedBlocks) {
						B.update(true);
					}
				}			
			};
		}
		if(delay == 0) {
			trapTask = new BukkitRunnable() {

				@Override
				public void run() {
					if(!type.isSilent()) startLocation.getWorld().playSound(startLocation, Sound.ENTITY_CHICKEN_EGG, 2.5f, 1f);
					for(int i = 0; i < steps; i++) {
						method.trapStep(workLocation, width, height, stepnr, travelDirection, crossVector, thisTrap);
						workLocation.add(travelStep);
						stepnr++;
					}
				}
				
			};
		} else {
			trapTask = new BukkitRunnable() {

				@Override
				public void run() {
					if(!type.isSilent()) startLocation.getWorld().playSound(startLocation, Sound.ENTITY_CHICKEN_EGG, 2.5f, 1f);
					method.trapStep(workLocation, width, height, stepnr, travelDirection, crossVector, thisTrap);
					workLocation.add(travelStep);
					stepnr++;
					if(stepnr == steps) cancel();
				}
				
			};
		}
		
	}
	
}
