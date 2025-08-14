package be.hctel.renaissance.deathrun.traps;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class TrapMethod {
	public abstract void trapStep(Location point, int width, int stepnr, Vector direction, Vector crossVector);
}
