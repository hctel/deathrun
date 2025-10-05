package be.hctel.renaissance.deathrun.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

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
	
	private DeathRun plugin;
	
	private GameMap map;
	
	private BukkitRunnable eachSecondTask;
	
	private int timer = 320;
	private boolean gameOngoig = false;
	
	private ArrayList<Player> runners = new ArrayList<>();
	private ArrayList<Player> deaths = new ArrayList<>();
	
	private HashMap<Player, Integer> checkpointIndex = new HashMap<>();
	private HashMap<Player, Location> respawnLocation = new HashMap<>();
	private HashMap<Player, Integer> deathCount = new HashMap<>();
	private HashMap<Player, Integer> points = new HashMap<>();
	private HashMap<Player, DynamicScoreboard> scoreboards = new HashMap<>();
	private HashMap<Player, Team> playerTeams = new HashMap<>();
	
	/**
	 * Game Engine constructor
	 * @param plugin This plugin's main class
	 */
	public MainGameEngine(DeathRun plugin) {
		this.plugin = plugin;
		this.map = plugin.mapManager.getMap(plugin.getServer().getWorld("world"));
	}
	
	public void startGame(GameMap map, List<Player> wishDeaths) {
		
		if(wishDeaths.size() > 0) {
			
		}
		for(Player P : plugin.getServer().getOnlinePlayers()) {
			deathCount.put(P, 0);
			points.put(P, 0);
			DynamicScoreboard scoreboard = new DynamicScoreboard(P.getName()+"_DR_INGAME", "§c§lDeathRun", plugin.scoreboardManager);
			scoreboard.setLine(14, "");
			scoreboard.setLine(13, "§e§lGame Info");
			scoreboard.setLine(12, "§7Time: §r00:00");
			scoreboard.setLine(11, String.format("§7Runners: §r%d", runners.size()));
			scoreboard.setLine(10, "§7GameID:");
			scoreboard.setLine(9, "00000");
			scoreboard.setLine(8, "6b§lMy Round Stats");
			scoreboard.setLine(6, String.format("§7Role: %s", getRole(P).getDisplay()));
			scoreboard.setLine(5, "§7Points: §r0");
			scoreboard.setLine(4, "§7Deaths: §r0");
			scoreboard.setLine(3, "");
			scoreboard.setLine(2, "§7§m--------");
			scoreboard.setLine(1, "");
			scoreboards.put(P, scoreboard);
			Team playerTeam = plugin.getServer().getScoreboardManager().getMainScoreboard().registerNewTeam("Team"+P.getName());
			playerTeam.setCanSeeFriendlyInvisibles(true);
			playerTeams.put(P, playerTeam);
		}
		eachSecondTask = new BukkitRunnable() {
			@Override
			public void run() {
				for(Player P : plugin.getServer().getOnlinePlayers()) {
					if(getRole(P) == Role.RUNNER || getRole(P) == Role.DEATH) {
						if(timer == 300) {
							scoreboards.get(P).addReceiver(P);
							P.sendTitle("§c§l < RUN! >", "§3The game has BEGUN!", 10, 70, 20);
						}
					}
				}
				timer--;
			}
		};
		eachSecondTask.runTaskTimer(plugin, 0L, 20L);
	}
	
	/**
	 * <p>Registers a player death (doesn't actually kill the player). 
	 * <p>Teleports to spawn and displays the death message.
	 * @param player The player to kill
	 */
	public void killPlayer(Player player) {
		if(player.getGameMode() == GameMode.CREATIVE) return;
		Location toTeleport = respawnLocation.get(player);
		if(toTeleport == null) toTeleport = plugin.mapManager.getMap(player.getWorld()).getSpawn();
		player.teleport(toTeleport);
		player.setVelocity(new Vector(0,0,0));
		player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 10f, 1f);
		player.sendTitle("§4§l✖", "You died!", 0, 50, 20);
	}
	
	public void checkpoint(Player player) {
		if(checkpointIndex.containsKey(player)) {
			int index = checkpointIndex.get(player);
			Checkpoint cp = plugin.mapManager.getMap(player.getWorld()).getCheckpoints().get(index);
			if(respawnLocation.get(player).distanceSquared(player.getLocation()) > 100) {
				checkpointIndex.put(player, index+1);
				respawnLocation.put(player, cp.getRespawnLocation());
				player.sendTitle(cp.getName(), null, 10, 40, 20);
			}
		} else {
			Checkpoint cp = plugin.mapManager.getMap(player.getWorld()).getCheckpoints().get(0);
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
