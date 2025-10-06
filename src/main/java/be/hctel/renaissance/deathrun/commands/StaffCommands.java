package be.hctel.renaissance.deathrun.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mvplugins.multiverse.core.world.LoadedMultiverseWorld;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.misc.Checkpoint;
import be.hctel.renaissance.global.mapmanager.GameMap;

public class StaffCommands implements CommandExecutor {
	
	DeathRun plugin;
	
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
				if(args.length > 1) {
					if(args[0].equals("death")) {
						JSONObject mapConfig = plugin.mapManager.getMapConfig(player.getWorld());
						mapConfig.put(args[0] + "Spawn", Utils.locationToJson(player.getLocation()));
						//if(args[0].equalsIgnoreCase("runner")) ((LoadedMultiverseWorld) plugin.worldManager.getWorld(player.getWorld()).get()).setSpawnLocation(player.getLocation());
						return true;
					} else if(args[0].equalsIgnoreCase("runner")) {
						if(args.length == 2) {
							JSONObject mapConfig = plugin.mapManager.getMapConfig(player.getWorld());
							try {
								Material mat = Material.valueOf(args[1].toUpperCase());
								mapConfig.put("spawnMaterial", mat);
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
		}
		return false;
	}
	
}
