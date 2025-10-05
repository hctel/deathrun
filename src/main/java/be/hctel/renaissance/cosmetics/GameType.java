package be.hctel.renaissance.cosmetics;

/*
 * This file is a part of the Renaissance Project API
 */

public enum GameType {
	DR("Death Run"),
	HIDE("Hide and Seek");
	
	String name;
	private GameType(String name) {
		this.name = name;
	}
}
