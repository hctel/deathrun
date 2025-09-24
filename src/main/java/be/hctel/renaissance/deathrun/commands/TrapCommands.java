package be.hctel.renaissance.deathrun.commands;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.traps.TrapControls;
import be.hctel.renaissance.deathrun.traps.TrapTool;
import be.hctel.renaissance.deathrun.traps.TrapType;

public class TrapCommands implements CommandExecutor {
	
	private DeathRun plugin;
	private HashMap<Player, TrapTool> trapTools = new HashMap<Player, TrapTool>();

	public TrapCommands(DeathRun deathRun) {
		this.plugin = deathRun;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(cmd.getName().equalsIgnoreCase("traptool")) {
				if(args.length != 6) {
					player.sendMessage(plugin.header + "§cInvalid command structure! Expected: <type> <width> <steps> <delay> <resetDelay> <cooldownDelay>");
					return true;
				} 
				try {
					trapTools.put(player, new TrapTool(plugin, player, Integer.parseInt(args[1]), Integer.parseInt(args[2]), Long.parseLong(args[3]), Long.parseLong(args[4]), Long.parseLong(args[5]), TrapType.valueOf(args[0].toUpperCase())));
					player.getInventory().setItem(0, Utils.createQuickItemStack(Material.STICK, "§aTrap area"));
					player.getInventory().setItem(1, Utils.createQuickItemStack(Material.DIAMOND, "§bTrap controls area"));
				} catch(NumberFormatException e) {
					player.sendMessage("§cPlease enter a valid number!");
				} catch(IllegalArgumentException e) {
					player.sendMessage("§cPlease enter a valid trap type!");
				}
				return true;
				
			} else if(cmd.getName().equalsIgnoreCase("savetrap")) {
				System.out.println(trapTools.size());
				if(!trapTools.containsKey(player)) player.sendMessage(plugin.header + "§cYou need to open the trap tool first!");
				else {
					TrapControls trap = trapTools.get(player).getTrap();
					plugin.mapManager.getTrapManager(plugin.mapManager.getMap(player.getWorld())).addTrap(trap);
					player.getInventory().removeItem(Utils.createQuickItemStack(Material.STICK, "§aTrap area"), Utils.createQuickItemStack(Material.DIAMOND, "§bTrap controls area"));
				}
				return true;
			} else if(cmd.getName().equalsIgnoreCase("trapmanager")) {
				plugin.mapManager.getTrapManager(plugin.mapManager.getMap(player.getWorld())).showTrapManager(player);
				return true;
			}
		}
		return false;
	}

}
