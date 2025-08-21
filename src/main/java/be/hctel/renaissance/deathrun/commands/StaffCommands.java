package be.hctel.renaissance.deathrun.commands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.deathrun.DeathRun;

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
		}
		return false;
	}
	
}
