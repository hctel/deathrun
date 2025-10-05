package be.hctel.renaissance.deathrun.engine;

/**
 * Represents the different roles one player might have.
 * @author <a href="https://hctel.net/">hctel</a>: <a href="https://links.hctel.net/">LinkTree</a>
 *
 */
public enum Role {
	/**
	 * Pregame role.
	 */
	PREGAME(""),
	/**
	 * Default player role.
	 */
	RUNNER("§7Runner"),
	/**
	 * Death role. These players can activate traps and run alongside the racetrack.
	 */
	DEATH("§cDeath"),
	/**
	 * Players who either joined while in-game to spectate or who already finished the race.
	 */
	SPEC("§4SPEC"),
	/**
	 * Test role. Not used in production.
	 */
	TEST("§6Test");
	
	private String display;
	
	private Role(String display) {
		this.display = display;
	}
	
	public String getDisplay() {
		return display;
	}
}
