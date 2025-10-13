package be.hctel.renaissance.deathrun.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.misc.Checkpoint;
import be.hctel.renaissance.deathrun.misc.SpawnWallTool;
import be.hctel.renaissance.global.mapmanager.GameMap;

public class StaffCommands implements CommandExecutor {
	
	DeathRun plugin;
	HashMap<Player, SpawnWallTool> swTools = new HashMap<>();
	
	public StaffCommands(DeathRun plugin) {
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(command.getName().equalsIgnoreCase("gm")) {
				try {
					int gamemode = Integer.parseInt(args[0]);
					Player target = (args.length == 2 ? Bukkit.getPlayer(args[1]) : player);
					if(target == null) {
						player.sendRawMessage("§cThat player couldn't be found.");
						return false;
					}
					player.setGameMode(GameMode.getByValue(gamemode));
				} catch (NumberFormatException e) {
					player.sendMessage("§cPlease enter a valid number!");
				}
			}
			if(command.getName().equalsIgnoreCase("dms")) {
				if(args.length > 0) {
					if(args[0].equalsIgnoreCase("death")) {
						JSONObject mapConfig = plugin.mapManager.getMapConfig(player.getWorld());
						mapConfig.put(args[0] + "Spawn", Utils.locationToJson(player.getLocation()));
						//if(args[0].equalsIgnoreCase("runner")) ((LoadedMultiverseWorld) plugin.worldManager.getWorld(player.getWorld()).get()).setSpawnLocation(player.getLocation());
						return true;
					} else if(args[0].equalsIgnoreCase("runner")) {
						if(args.length == 2) {
							JSONObject mapConfig = plugin.mapManager.getMapConfig(player.getWorld());
							try {
								Material mat = Material.valueOf(args[1].toUpperCase());
								mapConfig.put("spawnMaterial", mat.toString());
							} catch (IllegalArgumentException e) {
								player.sendMessage("§cPlease enter a valid material!");
								return true;
							}
							mapConfig.put(args[0] + "Spawn", Utils.locationToJson(player.getLocation()));
							((LoadedMultiverseWorld) plugin.worldManager.getWorld(player.getWorld()).get()).setSpawnLocation(player.getLocation());
							return true;
						}
					}
				}
				player.sendMessage("§cPlease enter a valid spawn type (runner|death)");
			}
			if(command.getName().equalsIgnoreCase("cpd")) {
				GameMap map = plugin.mapManager.getMap(player.getWorld());
				Location checkpointLoc = player.getLocation();
				Location respawnLoc = checkpointLoc.clone().add(player.getLocation().getDirection().multiply(3));
				String name = String.format("Stage %d", map.getCheckpoints().size()+1);
				int tokenCounts = 10;
				switch(args.length) {
				case 0:
					break;
				case 1:
					try {
						tokenCounts = Integer.parseInt(args[0]);
					} catch(NumberFormatException e) {
						player.sendRawMessage("§cPlease enter a valid number!");
						return true;
					}
					break;
				default:
					try {
						tokenCounts = Integer.parseInt(args[0]);
					} catch(NumberFormatException e) {
						player.sendRawMessage("§cPlease enter a valid number!");
						return true;
					}
					name = args[1];
					break;	
						
				}
				map.addCheckpoint(new Checkpoint(checkpointLoc, respawnLoc, name, tokenCounts));
				return true;
			}
			if(command.getName().equalsIgnoreCase("testheads")) {
				player.getInventory().setItem(3, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getLeftTextureURL(), "§a§lStrafe Left"));
				player.getInventory().setItem(5, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getRightTextureURL(), "§a§lStrafe Right"));
				player.getInventory().setItem(4, Utils.skullBuilder(plugin.stats.getStrafeColor(player).getBackTextureURL(), "§a§lStrafe Backwards"));
				return true;
			}
			if(command.getName().equalsIgnoreCase("preshow")) {
				if(args.length != 1) {
					player.sendRawMessage("§cMissing preshow slide number!");
					return true;
				}
				int number = Integer.parseInt(args[0]);
				if(number < 0 || number > 3) {
					player.sendRawMessage("§cPreshow slide number should be between 0-3!");
					return true;
				}
				JSONObject location = Utils.locationToJson(player.getLocation());
				JSONObject mapJson = plugin.mapManager.getMap(player.getWorld()).getConfig();
				if(mapJson.has("preshow")) {
					JSONArray preshow = mapJson.getJSONArray("preshow");
					preshow.put(number, location);
					mapJson.put("preshow", preshow);
					return true;
				}
				JSONArray preshow = new JSONArray();
				preshow.put(number, location);
				mapJson.put("preshow", preshow);
				return true;		
			}
			if(command.getName().equalsIgnoreCase("testspawn")) {
				int spawnRadius = 7;
				GameMap map = plugin.mapManager.getMap(player.getWorld());
				if(!map.getConfig().has("spawnMaterial")) return true;
				ArrayList<Location> spawnLocs = new ArrayList<>();
				for(int x = -spawnRadius; x <= spawnRadius; x++) {
					for(int z = -spawnRadius; z <= spawnRadius; z++) {
						for(int y = -1; y < 2; y++) {
							Location l = Utils.jsonToLocation(map.getConfig().getJSONObject("runnerSpawn")).add(x,y,z);
							if(map.getConfig().getString("spawnMaterial").endsWith("_STAINED_GLASS") && l.getBlock().getType().toString().endsWith("_STAINED_GLASS")) {
								spawnLocs.add(l);
							}
							else if(map.getConfig().getString("spawnMaterial").endsWith("_TERRACOTTA") && l.getBlock().getType().toString().endsWith("_TERRACOTTA")) {
								spawnLocs.add(l);
							}
							else if(l.getBlock().getType() == Material.valueOf(map.getConfig().getString("spawnMaterial"))) {
								spawnLocs.add(l);
							}
						}
					}
				}
				player.sendMessage(plugin.header + (spawnLocs.size() == 20 ? "§aSize is OK" : "§cSize not matching 20 players: §4" + spawnLocs.size()));
				return true;
			}
			if(command.getName().equalsIgnoreCase("swt")) {
				if(swTools.containsKey(player)) swTools.get(player).getLocations();
				swTools.put(player, new SpawnWallTool(plugin, player));
				return true;
			}
			if(command.getName().equalsIgnoreCase("ssw")) {
				if(!swTools.containsKey(player)) {
					player.sendMessage(plugin.header + "§cPlease start a spawn wall tool first.");
				} else {
					try {
						List<Location> locs	= swTools.get(player).getLocations();
						JSONObject config = plugin.mapManager.getMap(player.getWorld()).getConfig();
						JSONArray arr = new JSONArray();
						for(Location L : locs) {
							arr.put(Utils.locationToJson(L));
						}
						config.put("spawnWall", arr);
						swTools.remove(player);
					} catch (NullPointerException e) {
						player.sendMessage(plugin.header + "§cPlease make a complete selection first.");
					}
				}
				return true;
			}
			if(command.getName().equalsIgnoreCase("tsw")) {
				ArrayList<BlockState> changedStartBlocks = new ArrayList<>();
				GameMap map = plugin.mapManager.getMap(player.getWorld());
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
				new BukkitRunnable() {
					
					@Override
					public void run() {
						for(BlockState S : changedStartBlocks) {
							S.update(true);
						}
					}
				}.runTaskLater(plugin, 60L);
			}
		}
		return false;
	}
	
}
