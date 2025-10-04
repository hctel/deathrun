package be.hctel.renaissance.deathrun.misc;

import org.bukkit.entity.Player;

import be.hctel.api.Utils;

public class StrafeCooldown {
	private Player player;
	private int counter;
	private StrafeDirection direction;
	
	public StrafeCooldown(Player player, int cooldown, StrafeDirection direction) {
		this.player = player;
		this.counter = cooldown;
		this.direction = direction;
	}
	
	public boolean step() {
		switch(direction) {
		case BACKWARDS:
			if(counter > 0) {
				player.getInventory().setItem(4, Utils.skullBuilder("http://textures.minecraft.net/texture/d1b62db5c0a3fa1ef441bf7044f511be58bedf9b6731853e50ce90cd44fb69", "§7§lOn cooldown", counter));
				counter--;
			} else {
				player.getInventory().setItem(4, Utils.skullBuilder("http://textures.minecraft.net/texture/3b83bbccf4f0c86b12f6f79989d159454bf9281955d7e2411ce98c1b8aa38d8", "§a§lStrafe Backwards"));
				return true;
			}
			break;
		case LEAP:
			break;
		case LEFT:
			if(counter > 0) {
				player.getInventory().setItem(3, Utils.skullBuilder("http://textures.minecraft.net/texture/542fde8b82e8c1b8c22b22679983fe35cb76a79778429bdadabc397fd15061", "§7§lOn cooldown", counter));
				counter--;
			} else {
				player.getInventory().setItem(3, Utils.skullBuilder("http://textures.minecraft.net/texture/f5347423ee55daa7923668fca8581985ff5389a45435321efad537af23d", "§a§lStrafe Left"));
				return true;
			}
			break;
		case RIGHT:
			if(counter > 0) {
				player.getInventory().setItem(5, Utils.skullBuilder("http://textures.minecraft.net/texture/406262af1d5f414c597055c22e39cce148e5edbec45559a2d6b88c8d67b92ea6", "§7§lOn cooldown", counter));
				counter--;
			} else {
				player.getInventory().setItem(5, Utils.skullBuilder("http://textures.minecraft.net/texture/4ef356ad2aa7b1678aecb88290e5fa5a3427e5e456ff42fb515690c67517b8", "§a§lStrafe Right"));
				return true;
			}
			break;
		default:
			break;
		
		}
		return false;
	}
}

