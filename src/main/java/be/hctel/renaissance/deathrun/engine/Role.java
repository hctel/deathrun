package be.hctel.renaissance.deathrun.engine;

/**
 * Represents the different roles one player might have.
 * @author <a href="https://hctel.net/">hctel</a>: <a href="https://links.hctel.net/">LinkTree</a>
 *
 */
public enum Role {
	/**
	 * Default player role.
	 */
	RUNNER,
	/**
	 * Death role. These players can activate traps and run alongside the racetrack.
	 */
	DEATH,
	/**
	 * Players who either joined while in-game to spectate or who already finished the race.
	 */
	SPEC,
	/**
	 * Test role. Not used in production.
	 */
	TEST;
}
