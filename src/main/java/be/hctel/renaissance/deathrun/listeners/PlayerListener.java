package be.hctel.renaissance.deathrun.listeners;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.misc.StrafeCooldown;
import be.hctel.renaissance.deathrun.misc.StrafeDirection;


public class PlayerListener implements Listener {
	
	DeathRun plugin;
	private HashMap<Player, Long> lastPad = new HashMap<>();
	
	public PlayerListener(DeathRun plugin) {
		this.plugin = plugin;
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		switch(e.getTo().clone().add(0,-1,0).getBlock().getType()) {	
		case EMERALD_BLOCK: {
			if(System.currentTimeMillis() - (lastPad.containsKey(e.getPlayer()) ? lastPad.get(e.getPlayer()) : 0) < 500) break;
			lastPad.put(e.getPlayer(), System.currentTimeMillis());
			Location location = e.getTo().clone().subtract(0, 1, 0);
			for(int x = -1; x < 2; x++) {
				for(int y = -1; y < 2; y++) {
					for(int z = -1; z < 2; z++) {
						Block block = location.clone().add(x, y, z).getBlock();
						if(block.getState() instanceof Sign) {
							Sign sign = (Sign) block.getState();
							int strenght = Integer.parseInt(sign.getSide(Side.FRONT).getLine(0));
							int duration = Integer.parseInt(sign.getSide(Side.FRONT).getLine(1));
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP_BOOST, duration*20, strenght, false, false));
							e.getPlayer().sendMessage(plugin.header + String.format("§a§l+ §6§lJump %d §7[%d Seconds]", strenght, duration));
						}
					}
				}
			}
			break;
		}
		case REDSTONE_BLOCK: {
			if(System.currentTimeMillis() - (lastPad.containsKey(e.getPlayer()) ? lastPad.get(e.getPlayer()) : 0) < 500) break;
			lastPad.put(e.getPlayer(), System.currentTimeMillis());
			Location location = e.getTo().clone().subtract(0, 1, 0);
			for(int x = -1; x < 2; x++) {
				for(int y = -1; y < 2; y++) {
					for(int z = -1; z < 2; z++) {
						Block block = location.clone().add(x, y, z).getBlock();
						if(block.getState() instanceof Sign) {
							Sign sign = (Sign) block.getState();
							int strenght = Integer.parseInt(sign.getSide(Side.FRONT).getLine(0));
							int duration = Integer.parseInt(sign.getSide(Side.FRONT).getLine(1));
							e.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, duration*20, strenght, false, false));
							e.getPlayer().sendMessage(plugin.header + String.format("§a§l+ §6§lSpeed %d §7[%d Seconds]", strenght, duration));
						}
					}
				}
			}
			break;
		}
		default: break;
		}
		switch(e.getTo().getBlock().getType()) {
		case NETHER_PORTAL: {
			plugin.mainGameEngine.checkpoint(e.getPlayer());
			break;
		}
		default: break;
		}
		if(e.getPlayer().isInWater()) plugin.mainGameEngine.killPlayer(e.getPlayer());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		e.setCancelled(true);
		plugin.getServer().broadcastMessage(String.format("<%s> %s", e.getPlayer().getDisplayName(), e.getMessage()));
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		if(e.getItem() != null) {
			Player player = e.getPlayer();
			if(e.getItem().getItemMeta().getDisplayName().contains("Strafe") && e.getItem().getType() == Material.PLAYER_HEAD) {
				int cooldown = 30;
				switch(e.getItem().getItemMeta().getDisplayName().split("Strafe ")[1].toLowerCase()) {
				case "left":
				{
					applyLeftStrafe(player);
					StrafeCooldown strafeCooldown = new StrafeCooldown(player, cooldown, StrafeDirection.LEFT, plugin);
					new BukkitRunnable() {
						@Override
						public void run() {
							if(strafeCooldown.step()) cancel();
						}
					}.runTaskTimer(plugin, 0L, 20L);
					break;
				}
				case "right":
				{
					applyRightStrafe(player);
					StrafeCooldown strafeCooldown = new StrafeCooldown(player, cooldown, StrafeDirection.RIGHT, plugin);
					new BukkitRunnable() {
						@Override
						public void run() {
							if(strafeCooldown.step()) cancel();
						}
					}.runTaskTimer(plugin, 0L, 20L);
					break;
				}
				case "backwards": {
					applyBackStrafe(player);
					StrafeCooldown strafeCooldown = new StrafeCooldown(player, cooldown, StrafeDirection.BACKWARDS, plugin);
					new BukkitRunnable() {
						@Override
						public void run() {
							if(strafeCooldown.step()) cancel();
						}
					}.runTaskTimer(plugin, 0L, 20L);
					break;
				}
				}
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(e.getEntity() instanceof Player) {
			System.out.println(e.getCause());
			switch(e.getCause()) {
				case ENTITY_ATTACK: {
					e.setCancelled(true);
					break;
				}
				case ENTITY_SWEEP_ATTACK: {
					e.setCancelled(true);
					break;
				}
				default: break;
			}
		}
		else if(e.getEntity() instanceof Giant) {
			List<Entity> nearby = e.getEntity().getNearbyEntities(11, 2, 11);
			for(Entity E : nearby) {
				if(E instanceof Player) {
					plugin.mainGameEngine.killPlayer(((Player) E));
				}
			}
		}
	}
	
	/*
	 * The underlying part of the code was written by Gargant, for their GStrafe plugin (https://github.com/Gargant0373/GStrafes/tree/master). The project was published under an MIT License as follows:
	 * 
	 * MIT License
	 *
	 * Copyright (c) 2021 Gargant
	 * 
	 * Permission is hereby granted, free of charge, to any person obtaining a copy
	 * of this software and associated documentation files (the "Software"), to deal
	 * in the Software without restriction, including without limitation the rights
	 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
	 * copies of the Software, and to permit persons to whom the Software is
	 * furnished to do so, subject to the following conditions:
	 * 
	 * The above copyright notice and this permission notice shall be included in all
	 * copies or substantial portions of the Software.
	 * 
	 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
	 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
	 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
	 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
	 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
	 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
	 * SOFTWARE.
	 * 
	 */
	private void applyBackStrafe(Player player) {
		Location locVec = player.getLocation().clone();
		locVec = this.snapYaw(locVec);
		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection().multiply(-1.0)
				.multiply(2);
		velocityVector = velocityVector.setY(0.25);
		player.setVelocity(velocityVector);
	}

	private void applyLeftStrafe(Player player) {
		Location locVec = player.getLocation().clone();

		locVec = this.snapYaw(locVec);

		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection();
		double x = velocityVector.getX(), z = velocityVector.getZ();
		double aux = -x;
		x = z;
		z = aux;
		velocityVector.setX(x);
		velocityVector.setZ(z);
		velocityVector = velocityVector.multiply(2);
		velocityVector = velocityVector.setY(0.25);
		player.setVelocity(velocityVector);
	}

	private void applyRightStrafe(Player player) {
		Location locVec = player.getLocation().clone();

		locVec = this.snapYaw(locVec);

		locVec.setPitch(0);
		Vector velocityVector = locVec.getDirection();
		double x = velocityVector.getX(), z = velocityVector.getZ();
		double aux = x;
		x = -z;
		z = aux;
		velocityVector.setX(x);
		velocityVector.setZ(z);
		velocityVector = velocityVector.multiply(2);
		velocityVector = velocityVector.setY(0.25);
		player.setVelocity(velocityVector);
	}
	
	private Location snapYaw(Location location) {
		double rot = (location.getYaw() - 90) % 360;
		if (rot < 0)
			rot += 360.0;

		float settableYaw = -1;
		if (45 <= rot && rot < 135)
			settableYaw = 90;
		else if (135 <= rot && rot < 225)
			settableYaw = 180;
		else if (225 <= rot && rot < 315)
			settableYaw = 270;
		else
			settableYaw = 0;

		location.setYaw(settableYaw + 90);
		return location;
	}
}
