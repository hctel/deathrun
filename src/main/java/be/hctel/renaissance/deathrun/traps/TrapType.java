package be.hctel.renaissance.deathrun.traps;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import be.hctel.api.Utils;

public enum TrapType {
	GLASS_FLOOR(new TrapMethod() {
		@Override
		public void trapStep(Location point, int width, int stepnr, Vector direction, Vector crossVector) {	
			Location workLocation = point.clone();
			point.getWorld().playSound(point, Sound.BLOCK_NOTE_PLING, 5f, (float) (0.5+(((float)stepnr)/20f)));
			System.out.println(String.format("Point at (%d, %d, %d)", point.getBlockX(), point.getBlockY(), point.getBlockZ()));
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
