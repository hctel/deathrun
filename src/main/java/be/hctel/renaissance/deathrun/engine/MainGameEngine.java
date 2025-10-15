package be.hctel.renaissance.deathrun.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.joml.Random;
import org.json.JSONObject;

import be.hctel.api.Utils;
import be.hctel.api.scoreboard.DynamicScoreboard;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.misc.Checkpoint;
import be.hctel.renaissance.global.mapmanager.GameMap;

/**
 * <p>Main Game Engine class for DeathRun game.
 * <p>Manages timing, roles, countdowns, etc...
 * 
 * @author <a href="https://hctel.net/">hctel</a>: <a href="https://links.hctel.net/">LinkTree</a>
 *
 */
public class MainGameEngine {
	private static PotionEffect endPotionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 40, 1, true, false);
	
	private int spawnRadius = 7;
	
	private DeathRun plugin;
	
	private GameMap map;
	
	private BukkitRunnable eachSecondTask;
	
	private int timer = 320;
	private boolean gameOngoig = false;
	private long gameStartEpoch;
	
	private ArrayList<BlockState> changedStartBlocks = new ArrayList<BlockState>();
	
	private ArrayList<Player> runners = new ArrayList<>();
	private ArrayList<Player> deaths = new ArrayList<>();
	
	private HashMap<Player, Integer> checkpointIndex = new HashMap<>();
	private HashMap<Player, Location> respawnLocation = new HashMap<>();
	private HashMap<Player, Integer> deathCount = new HashMap<>();
	private HashMap<Player, Integer> points = new HashMap<>();
	private HashMap<Player, Integer> tokens = new HashMap<>();
	private HashMap<Player, DynamicScoreboard> scoreboards = new HashMap<>();
	//private HashMap<Player, Team> playerTeams = new HashMap<>();
	private HashMap<Player, Long> finishTime = new HashMap<>();
	
	/**
	 * Game Engine constructor
	 * @param plugin This plugin's main class
	 */
	public MainGameEngine(DeathRun plugin) {
		this.plugin = plugin;
		this.map = plugin.mapManager.getMap(plugin.getServer().getWorld("world"));
	}
	
	public void startGame(GameMap map, List<Player> wishDeaths, List<Player> wishRunner) {
		this.map = map;
		for(Player P : plugin.getServer().getOnlinePlayers()) {
			runners.add(P);
		}
		
		if(wishDeaths.size() > 0) {
			Player chosenDeath = wishDeaths.get(new Random().nextInt(wishDeaths.size()));
			plugin.cosmetics.addTokens(chosenDeath, -100);
			deaths.add(chosenDeath);
			runners.remove(chosenDeath);
		} else {
			Player chosenDeath = runners.get(new Random().nextInt(runners.size()));
			while(wishRunner.contains(chosenDeath)) {
				plugin.cosmetics.addTokens(chosenDeath, -50);
				chosenDeath = runners.get(new Random().nextInt(runners.size()));
			}
			deaths.add(chosenDeath);
			runners.remove(chosenDeath);
		}
		
		for(Player P : plugin.getServer().getOnlinePlayers()) {
			deathCount.put(P, 0);
			points.put(P, 0);
			tokens.put(P, 0);
			DynamicScoreboard scoreboard = new DynamicScoreboard(P.getName()+"_DR_INGAME", "§c§lDeathRun", plugin.scoreboardManager);
			scoreboard.setLine(14, "");
			scoreboard.setLine(13, "§e§lGame Info");
			scoreboard.setLine(12, "§7Time: §r00:00");
			scoreboard.setLine(11, String.format("§7Runners: §r%d", runners.size()));
			scoreboard.setLine(10, "§7GameID:");
			scoreboard.setLine(9, "00000");
			scoreboard.setLine(8, "§b§lMy Round Stats");
			scoreboard.setLine(6, String.format("§7Role: %s", getRole(P).getDisplay()));
			scoreboard.setLine(5, "§7Points: §r0");
			scoreboard.setLine(4, "§7Deaths: §r0");
			scoreboard.setLine(3, "");
			scoreboard.setLine(2, "§7§m--------");
			scoreboard.setLine(1, "");
			scoreboards.put(P, scoreboard);
//			Team playerTeam = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("Team"+P.getName());
//			playerTeam.setCanSeeFriendlyInvisibles(true);
//			playerTeams.put(P, playerTeam);
			P.sendTitle("§b§l" + map.getName(), "§3By " + map.getAuthor(), 10, 70, 20);
		}
		ArrayList<Location> preshow = new ArrayList<>();
		if(map.getConfig().has("preshow")) {
			for(Object O : map.getConfig().getJSONArray("preshow")) {
				if(O instanceof JSONObject) {
					preshow.add(Utils.jsonToLocation((JSONObject) O));
				}
			}
			
		}
		eachSecondTask = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player P : plugin.getServer().getOnlinePlayers()) {
					if(getRole(P) == Role.RUNNER || getRole(P) == Role.DEATH) {
						if(timer > 317) {
							if(preshow.size() > 0) P.teleport(preshow.get(0));
							else teleportToSpawn();
							if(timer == 319) P.sendTitle("§b§b" + map.getName(), "§3By " + map.getAuthor(), 10,70,20);
						} else if(timer <= 317 && timer > 313) {
							if(preshow.size() > 1) P.teleport(preshow.get(1));
						} else if(timer <= 313 && timer > 310) {
							if(preshow.size() > 2) P.teleport(preshow.get(2));
						}
						if(timer == 308) {
							P.sendTitle("§ePrepare to run!", null, 10,70,20);						
						}
						if(timer == 305) {
							P.sendTitle("§c§l\u278e", "§7untill start", 0,20,0);
						}
						if(timer == 304) {
							P.sendTitle("§c§l\u278d", "§7untill start", 0,20,0);
						}
						if(timer == 303) {
							P.sendTitle("§6§l\u278c", "§7untill start", 0,20,0);
							P.playSound(P, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.5f);
						}
						if(timer == 302) {
							P.sendTitle("§6§l\u278b", "§7untill start", 0,20,0);
							P.playSound(P, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 0.75f);
						}
						if(timer == 301) {
							P.sendTitle("§e§l\u278a", "§7untill start", 0,20,0);
							P.playSound(P, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
						}
						if(timer == 300) {
							gameStartEpoch = System.currentTimeMillis();
							scoreboards.get(P).addReceiver(P);
							P.sendTitle("§c§l< RUN! >", "§3The game has BEGUN!", 0, 70, 20);
						}
						if(timer == 60) {
							P.sendMessage(plugin.header + "§aGame ends in 60 seconds!");
						}
						if(timer == 30) {
							P.sendMessage(plugin.header + "§cGame ends in 30 seconds!");
						}
						if(timer == 10) {
							P.sendTitle("", "§c10 seconds left!", 10, 35, 20);
						}
						if(timer < 4 && timer > 0) {
							P.playSound(P.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 1.0f);
							P.sendMessage(plugin.header + "§eEnding in §f" + timer);
						}
						if(timer > 0 && timer < 300) {
							DynamicScoreboard sc = scoreboards.get(P);
							sc.setLine(12, "§7Time: §r" + Utils.formatSeconds(timer));
							sc.setLine(5, "§7Points: §r" + points.get(P));
							sc.setLine(4, "§7Deaths: §r" + points.get(P));							
						}
					}
				}
				if(timer == 310 && preshow.size() > 0) {
					teleportToSpawn();
				}
				if(timer == 300) {
					destroyStartWall();
					giveHeads();
				}
				if(timer == 0) {
					endGame();
				}
				timer--;
			}
		};
		eachSecondTask.runTaskTimer(plugin, 0L, 20L);
	}
	
	private void teleportToSpawn() {
		ArrayList<Location> spawnLocs = new ArrayList<>();
		for(int x = -spawnRadius; x <= spawnRadius; x++) {
			for(int z = -spawnRadius; z <= spawnRadius; z++) {
				for(int y = -1; y < 2; y++) {
					Location l = Utils.jsonToLocation(map.getConfig().getJSONObject("runnerSpawn")).add(x,y,z);
					if(map.getConfig().getString("spawnMaterial").endsWith("_STAINED_GLASS") && l.getBlock().getType().toString().endsWith("_STAINED_GLASS")) {
						spawnLocs.add(l.add(0, 1, 0));
					}
					else if(map.getConfig().getString("spawnMaterial").endsWith("_TERRACOTTA") && l.getBlock().getType().toString().endsWith("_TERRACOTTA")) {
						spawnLocs.add(l.add(0, 1, 0));
					}
					else if(l.getBlock().getType() == Material.valueOf(map.getConfig().getString("spawnMaterial"))) {
						spawnLocs.add(l.add(0, 1, 0));
					}
				}
			}
		}
		System.out.println(spawnLocs.size());
		for(int i = 0; i < runners.size(); i++) {
			runners.get(i).teleport(spawnLocs.get(i));
		}
		Location deathLoc = Utils.jsonToLocation(map.getConfig().getJSONObject("deathSpawn"));
		for(Player P : deaths) {
			P.teleport(deathLoc);
		}
	}
	
	private void giveHeads() {
		for(Player player : runners) {
			player.getInventory().setItem(3, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getLeftTextureURL(), "§a§lStrafe Left"));
			player.getInventory().setItem(5, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getRightTextureURL(), "§a§lStrafe Right"));
			player.getInventory().setItem(4, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getBackTextureURL(), "§a§lStrafe Backwards"));
		}
	}
	
	private void destroyStartWall() {
		Location loc1 = Utils.jsonToLocation(map.getConfig().getJSONArray("spawnWall").getJSONObject(0));
		Location loc2 = Utils.jsonToLocation(map.getConfig().getJSONArray("spawnWall").getJSONObject(1));
		Location workLoc = loc1.clone();
		Vector totalBlocks = loc2.clone().subtract(loc1).toVector();
		
		Vector xInc = new Vector(Math.signum(totalBlocks.getBlockX()), 0, 0);
		Vector yInc = new Vector(0, Math.signum(totalBlocks.getBlockY()), 0);
		Vector zInc = new Vector(0, 0, Math.signum(totalBlocks.getBlockZ()));
	
		for(int x = 0; x < Math.abs(totalBlocks.getBlockX())+1; x++) {
			Location workLocD = workLoc.clone();
			for(int y = 0; y < Math.abs(totalBlocks.getBlockY())+1; y++) {
				Location workLocDD = workLocD.clone();
				for(int z = 0; z < Math.abs(totalBlocks.getBlockZ())+1; z++) {
					Block b = workLocDD.getBlock();
					if(b.getType().toString().endsWith("_STAINED_GLASS") || b.getType() == Material.IRON_BARS) {
						changedStartBlocks.add(b.getState());
						Utils.transformToFallingBlock(b);
					}
					workLocDD.add(zInc);
				}
				workLocD.add(yInc);
			}
			workLoc.add(xInc);
		}
	}
	
	private void endGame() {
		eachSecondTask.cancel();
		timer = 0;
		plugin.mapManager.getTrapManager(map).endGame();
		for(BlockState S : changedStartBlocks) {
			S.update(true);
		}
		for(Player P : plugin.getServer().getOnlinePlayers()) {
			P.sendTitle("§3< Game Over! >", null, 10, 70, 20);
			P.playSound(P, Sound.ENTITY_BLAZE_SHOOT, 1.0f, 1.0f);
			scoreboards.get(P).removeReceiver(P);
		}
	}
	
	/**
	 * <p>Registers a player death (doesn't actually kill the player). 
	 * <p>Teleports to spawn and displays the death message.
	 * @param player The player to kill
	 */
	public void killPlayer(Player player) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		Location toTeleport = respawnLocation.get(player);
		if(toTeleport == null) toTeleport = map.getSpawn();
		player.teleport(toTeleport);
		player.setVelocity(new Vector(0,0,0));
		player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 10f, 1f);
		player.sendTitle("§4§l✖", "You died!", 0, 50, 20);
	}
	
	public void finishGame(Player player) {
		if(finishTime.containsKey(player)) return;
		long runTime = System.currentTimeMillis() - gameStartEpoch;
		finishTime.put(player, runTime);
		if(finishTime.size() == 1) {
			for(Player P : plugin.getServer().getOnlinePlayers()) {
				if(!P.equals(player)) P.sendTitle("", "§eA player finished, game ends in 90 seconds!", 10, 70, 20);
			}
			timer = 90;
		}
		runners.remove(player);
		player.sendTitle("", "§cYou are now spectating!", 10, 70, 20);
		player.addPotionEffect(endPotionEffect);
		plugin.getServer().broadcastMessage(String.format("%s%s%s §3finished §b%s§3. §7(%s)", plugin.header, plugin.ranks.getRankColor(player), player.getName(), Utils.ordinal(finishTime.size()), Utils.formatMilliseconds(runTime)));
		player.sendMessage(plugin.header + "§bYou finished your run in " + Utils.formatMilliseconds(runTime) + "!");
		player.teleport(map.getSpawn());
		if(runners.size() == 0) endGame();
	}
	
	public void checkpoint(Player player) {
		if(!gameOngoig) map = plugin.mapManager.getMap(player.getWorld());
		if(checkpointIndex.containsKey(player)) {
			int index = checkpointIndex.get(player);
			if(index == map.getCheckpoints().size()) {
				finishGame(player);
				return;
			}
			Checkpoint cp = map.getCheckpoints().get(index);
			if(respawnLocation.get(player).distanceSquared(player.getLocation()) > 75) {
				checkpointIndex.put(player, index+1);
				respawnLocation.put(player, cp.getRespawnLocation());
				player.sendTitle(cp.getName(), null, 10, 40, 20);
			}
		} else {
			Checkpoint cp = map.getCheckpoints().get(0);
			checkpointIndex.put(player, 1);
			respawnLocation.put(player, cp.getRespawnLocation());
			player.sendTitle(cp.getName(), null, 10, 40, 20);
		}
	}
	
	/**
	 * Gets a player's role
	 * @param player
	 * @return the {@link Role} of the player
	 */
	public Role getRole(Player player) {
		if(runners.contains(player)) {
			return Role.RUNNER;
		} else if(deaths.contains(player)) {
			return Role.DEATH;
		} else if(gameOngoig) {
			return Role.SPEC;
		}
		return Role.TEST;
	}
	
}
