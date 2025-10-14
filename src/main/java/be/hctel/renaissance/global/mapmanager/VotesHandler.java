package be.hctel.renaissance.global.mapmanager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.text.ChatMessages;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class VotesHandler {
	DeathRun plugin;
	ArrayList<GameMap> maps = new ArrayList<GameMap>();
	HashMap<GameMap, Integer> votes = new HashMap<GameMap, Integer>();
	HashMap<Player, Integer> playerVote = new HashMap<Player, Integer>();
	HashMap<Player, BukkitRunnable> voteMenuRunnables = new HashMap<>();
	public ArrayList<GameMap> currentGameMaps = new ArrayList<GameMap>();
	boolean acceptingVotes = true;
	GameMap max;
	public int voted;
	private int totalVotes;
	public VotesHandler(DeathRun plugin) {
		this.plugin = plugin;
		for(GameMap map : plugin.mapManager.getMaps()) {
			maps.add(map);
		}
		ArrayList<GameMap> a = new ArrayList<GameMap>();
		Random r = new Random();
		for(int i = 0; i < 6; i++) {
			GameMap b = maps.get(r.nextInt(maps.size()));
			while(a.contains(b)) {
				b = maps.get(r.nextInt(maps.size()));
			}
			a.add(b);
			votes.put(b, 0);
		}
		currentGameMaps = a;
		max = currentGameMaps.get(0);
		a = null;
	}
	
	@SuppressWarnings("deprecation")
	public void sendMapChoices() {
		for(int i = 0; i < 5; i++) {
			TextComponent message = new TextComponent(plugin.header + "§7§l" + (i+1) + ". §6" + currentGameMaps.get(i).getName() + getVoteAmountString(currentGameMaps.get(i)));
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v " + (i+1)));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §6" + currentGameMaps.get(i).getName()).create()));
			for(Player p : plugin.getServer().getOnlinePlayers()) {
				p.spigot().sendMessage(message);
			}
			
		}
		TextComponent message = new TextComponent(plugin.header + "§7§l" + (6) + ". §cRandom Map" + getVoteAmountString(currentGameMaps.get(5)));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §cRandom Map").create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v 6"));
		for(Player p : plugin.getServer().getOnlinePlayers()) {
			p.spigot().sendMessage(message);
		}
	}
	@SuppressWarnings("deprecation")
	public void sendMapChoices(Player player) {
		player.sendMessage(plugin.header + "§e§lVote for a map! §7Use §f/v # §7or click.");
		for(int i = 0; i < 5; i++) {
			TextComponent message = new TextComponent(plugin.header + "§7§l" + (i+1) + ". §6" + currentGameMaps.get(i).getName() + getVoteAmountString(currentGameMaps.get(i)));
			message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §6" + currentGameMaps.get(i).getName()).create()));
			message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v " + (i+1)));
			player.spigot().sendMessage(message);
		}
		TextComponent message = new TextComponent(plugin.header + "§7§l" + (6) + ". §cRandom Map" + getVoteAmountString(currentGameMaps.get(5)));
		message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick to vote for §cRandom Map").create()));
		message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/v 6"));
		player.spigot().sendMessage(message);
	}
	public void registerPlayerVote(Player player, int in, int qty) {
		if(!acceptingVotes) {
			player.sendMessage(plugin.header + ChatMessages.VOTESENDED.toText());
			return;
		}
		in--;
		if(in >= -1 && in < 6) {
			if(playerVote.containsKey(player)) {
				if(in == -1) {
					votes.replace(currentGameMaps.get(playerVote.get(player)), votes.get(currentGameMaps.get(playerVote.get(player)))-qty);
					playerVote.remove(player);
					totalVotes-=qty;
					return;
				}
				if(playerVote.get(player) == in) {
					player.sendMessage(plugin.header + ChatMessages.ALREADYVOTED.toText());
				} else {
					votes.replace(currentGameMaps.get(playerVote.get(player)), votes.get(currentGameMaps.get(playerVote.get(player)))-qty);
					playerVote.replace(player, in);
					votes.replace(currentGameMaps.get(playerVote.get(player)), votes.get(currentGameMaps.get(playerVote.get(player))) + qty);
					if(in == 5) {
						player.sendMessage(plugin.header + ChatMessages.VOTEREGISTERED.toText() + "§cRandom Map" + getVoteAmountString(currentGameMaps.get(in)) + ".");
					} else {
						player.sendMessage(plugin.header + ChatMessages.VOTEREGISTERED.toText() + currentGameMaps.get(in).getName() + getVoteAmountString(currentGameMaps.get(in)) + ".");
					}
				}
			} else {
				if(in == -1) return;
				totalVotes+=qty;
				playerVote.put(player, in);
				votes.replace(currentGameMaps.get(in), votes.get(currentGameMaps.get(in)) + qty);
				if(in == 5) {
					player.sendMessage(plugin.header + ChatMessages.VOTEREGISTERED.toText() + "§cRandom Map" + getVoteAmountString(currentGameMaps.get(in)) + ".");
				} else {
					player.sendMessage(plugin.header + ChatMessages.VOTEREGISTERED.toText() + currentGameMaps.get(in).getName() + getVoteAmountString(currentGameMaps.get(in)) + ".");
				}
			}
		} else {
			player.sendMessage(plugin.header + ChatMessages.NAN.toText() + " (1-6).");
		}
	}
	
	public void openVotesInventory(Player player) {
		Inventory inv = plugin.getServer().createInventory(null, 54, "Vote for an Option");
		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				
				int index = 0;
				max = Collections.max(votes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
				for(GameMap M : currentGameMaps) {
					Material lightData = (M == max ? Material.LIME_STAINED_GLASS_PANE : Material.WHITE_STAINED_GLASS_PANE);
					String mapName = "§r" + M.getName();
					int mapVotes = votes.get(M);
					double ratio = 0;
					if(totalVotes != 0) ratio= ((double) mapVotes) / ((double) totalVotes);
					int percent = (int) (ratio * 100);
					int lightGlasses = (int) (ratio * 8);
					int grayGlasses = 8-lightGlasses;
					int grayOffset = lightGlasses + 1;
					if(index == 5) mapName = "§cRandom Map";
					inv.setItem(index*9, Utils.createQuickItemStack(Material.FILLED_MAP, false, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
					if(playerVote.containsKey(player)) {
						if(playerVote.get(player) == index) {
							for(int i = 0; i < lightGlasses; i++) inv.setItem((index*9)+i+1, Utils.createQuickItemStack(lightData, true, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
							for(int i = 0; i < grayGlasses; i++) inv.setItem((index*9)+i+grayOffset, Utils.createQuickItemStack(Material.BLACK_STAINED_GLASS_PANE, true, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
						} else {
							for(int i = 0; i < lightGlasses; i++) inv.setItem((index*9)+i+1, Utils.createQuickItemStack(lightData, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
							for(int i = 0; i < grayGlasses; i++) inv.setItem((index*9)+i+grayOffset, Utils.createQuickItemStack(Material.BLACK_STAINED_GLASS_PANE, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
						}
					}
					else {
						for(int i = 0; i < lightGlasses; i++) inv.setItem((index*9)+i+1, Utils.createQuickItemStack(lightData, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
						for(int i = 0; i < grayGlasses; i++) inv.setItem((index*9)+i+grayOffset, Utils.createQuickItemStack(Material.BLACK_STAINED_GLASS_PANE, mapName, "", "§7Vote for this option!", "", String.format("§6§l%d %s §7[%d%%]", mapVotes, (mapVotes == 1 ? "Vote" : "Votes"), percent), "", "§b▸ Click to Vote"));
					}
					
					index++;
				}
			}
		};
		voteMenuRunnables.put(player, runnable);
		runnable.runTaskTimer(plugin, 0L, 10L);
		player.openInventory(inv);
		
	}
	
	public void refreshVotesInventory(Player player) {
		if(voteMenuRunnables.containsKey(player)) {
			voteMenuRunnables.get(player).run();
			//voteMenuRunnables.get(player).runTaskTimer(plugin, 0L, 10L);
		}
	}
	
	public void closeVotesInventory(Player player) {
		voteMenuRunnables.get(player).cancel();
	}
	
	public int endVotes() {
		acceptingVotes = false;
		GameMap voted = Collections.max(votes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
		this.voted = currentGameMaps.indexOf(voted);
		return currentGameMaps.indexOf(voted);
	}
	private String getVoteAmountString(GameMap map) {
		int n = votes.get(map);
		String color = "§7";
		max = Collections.max(votes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
		if(map == max) color = "§a";
		if(n == 1) {
			return color + " [§f" + n + color + " Vote]";
		} else {
			return  color + " [§f" + n + color + " Votes]";
		}
	}
}
