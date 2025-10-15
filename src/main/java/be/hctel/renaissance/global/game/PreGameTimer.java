package be.hctel.renaissance.global.game;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import be.hctel.api.Utils;
import be.hctel.api.scoreboard.DynamicScoreboard;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.text.ChatMessages;

public class PreGameTimer {
	DeathRun plugin;
	int minPlayers = 2;
	int timer = 20;
	BukkitScheduler scheduler;
	
	public boolean mapSelected = false;
	public boolean gameStarted = false;
	public boolean choosingBlock = false;
	
	private HashMap<Player, DynamicScoreboard> sidebars = new HashMap<Player, DynamicScoreboard>();
	public final ArrayList<Player> seekerQueue = new ArrayList<Player>();
	
	public PreGameTimer(DeathRun plugin) {
		this.plugin = plugin;
		scheduler = plugin.getServer().getScheduler();
			
		new BukkitRunnable() {
			public void run() {
				if(!gameStarted) {
					if(plugin.getServer().getOnlinePlayers().size() < minPlayers) {
						if(minPlayers - plugin.getServer().getOnlinePlayers().size() == 1) {
							for(Player p : plugin.getServer().getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§e" + (minPlayers - plugin.getServer().getOnlinePlayers().size()) + " player needed to start...");
							}
						} else {
							for(Player p : plugin.getServer().getOnlinePlayers()) {
								Utils.sendActionBarMessage(p, "§e" + (minPlayers - plugin.getServer().getOnlinePlayers().size()) + " players needed to start...");
							}
						}
					} else if(plugin.getServer().getOnlinePlayers().size() < minPlayers && timer != 20) {
						plugin.getServer().broadcastMessage(plugin.header + ChatMessages.STARTCANCELLED.toText());
						timer = 20;
						for(Player p : plugin.getServer().getOnlinePlayers()) {
							p.playSound(p.getLocation(), Sound.BLOCK_LEVER_CLICK, 1.0f, 0.5f);
						}
					} else if(plugin.getServer().getOnlinePlayers().size() >= minPlayers) {
						if(timer > 5) {
							if(timer == 10) plugin.votesHandler.sendMapChoices();
							for(Player P : plugin.getServer().getOnlinePlayers()) {
								Utils.sendActionBarMessage(P, "§a§lStarting in " + timer);
							}
						}
						else {
							if(timer == 5) {
								plugin.votesHandler.endVotes();
								plugin.getServer().broadcastMessage(plugin.header + "§3Voting has ended. §bThe map §f" + plugin.votesHandler.currentGameMaps.get(plugin.votesHandler.voted).getName() + " §bhas won.");
								
							}
							for(Player P : plugin.getServer().getOnlinePlayers()) {
								Utils.sendActionBarMessage(P, "§a§lStarting in §c§l" + timer);
								P.playSound(P, Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
							}
							if(timer == 0) {
								plugin.mainGameEngine.startGame(plugin.votesHandler.currentGameMaps.get(plugin.votesHandler.voted), seekerQueue, seekerQueue);
								for(Player P : plugin.getServer().getOnlinePlayers()) {
									sidebars.get(P).removeReceiver(P);
								}
								this.cancel();
							}
						}
						if(timer > -1) timer--;
					}
				}
				
			}
		}.runTaskTimer(plugin, 0L, 20L);
	}
	public void loadPlayer(Player player) {
		sidebars.put(player, new DynamicScoreboard(player.getName(), "§eYour HIDE stats", plugin.getServer().getScoreboardManager()));
		sidebars.get(player).setLine(plugin.stats.getPoints(player), "§bPoints", false);
		sidebars.get(player).setLine(plugin.cosmetics.getTokens(player), "§aTokens", false);
		sidebars.get(player).setLine(plugin.stats.getGamesPlayed(player), "§bGames Played", false);
		sidebars.get(player).setLine(plugin.stats.getDeaths(player), "§bTotal Deaths", false);
		sidebars.get(player).setLine(plugin.stats.getKills(player), "§bTotal Kills", false);
		sidebars.get(player).setLine(plugin.stats.getVictories(player), "§bVictories", false);
		sidebars.get(player).addReceiver(player);
	}
	
	public void updatePlayerScoreboard(Player player) {
		sidebars.get(player).setLine(plugin.stats.getPoints(player), "§bPoints", false);
		sidebars.get(player).setLine(plugin.cosmetics.getTokens(player), "§aTokens", false);
		sidebars.get(player).setLine(plugin.stats.getGamesPlayed(player), "§bGames Played", false);
		sidebars.get(player).setLine(plugin.stats.getDeaths(player), "§bTotal Deaths", false);
		sidebars.get(player).setLine(plugin.stats.getKills(player), "§bTotal Kills", false);
		sidebars.get(player).setLine(plugin.stats.getVictories(player), "§bVictories", false);
		sidebars.get(player).addReceiver(player);
	}
}