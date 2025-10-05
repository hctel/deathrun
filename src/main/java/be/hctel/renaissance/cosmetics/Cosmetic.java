package be.hctel.renaissance.cosmetics;

public enum Cosmetic {
	//DR Cosmetics
	WOOD_STRAFE(GameType.DR, "Use a wooden look for your strafe items!"),
	//HIDE Cosmetics
	DEFAULTARMOR(GameType.HIDE, "Plain old Iron Armor");
	
	GameType game;
	String name;
	private Cosmetic(GameType game, String name) {
		this.game = game;
		this.name = name;
	}
}
