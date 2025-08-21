package be.hctel.renaissance.deathrun.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.traps.Trap;
import be.hctel.renaissance.deathrun.traps.TrapType;

public class TestTrapCommand implements CommandExecutor {
	
	DeathRun plugin;
	
	public TestTrapCommand(DeathRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 14) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(player.getWorld(), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(player.getWorld(), x2, y2, z2);
				int width = Integer.parseInt(args[9]);
				int steps = Integer.parseInt(args[10]);
				long delay = Long.parseLong(args[11]);
				TrapType type = TrapType.valueOf(args[12]);
				int height = Integer.parseInt(args[13]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, width, height, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				player.sendRawMessage(testTrap.toString());
				return true;
			}
			else if(args.length == 13) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(player.getWorld(), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(player.getWorld(), x2, y2, z2);
				int width = Integer.parseInt(args[9]);
				int steps = Integer.parseInt(args[10]);
				long delay = Long.parseLong(args[11]);
				TrapType type = TrapType.valueOf(args[12]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, width, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				player.sendRawMessage(testTrap.toString());
				return true;
			} else if(args.length == 10) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(player.getWorld(), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(player.getWorld(), x2, y2, z2);
				int width = Integer.parseInt(args[6]);
				int steps = Integer.parseInt(args[7]);
				long delay = Long.parseLong(args[8]);
				TrapType type = TrapType.valueOf(args[9]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, width, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				player.sendRawMessage(testTrap.toString());
				return true;
			} else if(args.length == 12) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(player.getWorld(), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(player.getWorld(), x2, y2, z2);
				int steps = Integer.parseInt(args[9]);
				long delay = Long.parseLong(args[10]);
				TrapType type = TrapType.valueOf(args[11]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, steps, delay, type, 2*20L, 60*20L);
				player.sendRawMessage(testTrap.toString());
				testTrap.startTrap();
				return true;
			}
		} else {
			if(args.length == 13) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(Bukkit.getWorld("world"), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(Bukkit.getWorld("world"), x2, y2, z2);
				int width = Integer.parseInt(args[9]);
				int steps = Integer.parseInt(args[10]);
				long delay = Long.parseLong(args[11]);
				TrapType type = TrapType.valueOf(args[12]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, width, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				return true;
			} else if(args.length == 10) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(Bukkit.getWorld("world"), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(Bukkit.getWorld("world"), x2, y2, z2);
				int width = Integer.parseInt(args[6]);
				int steps = Integer.parseInt(args[7]);
				long delay = Long.parseLong(args[8]);
				TrapType type = TrapType.valueOf(args[9]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, width, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				return true;
			} else if(args.length == 12) {
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(Bukkit.getWorld("world"), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(Bukkit.getWorld("world"), x2, y2, z2);
				int steps = Integer.parseInt(args[9]);
				long delay = Long.parseLong(args[10]);
				TrapType type = TrapType.valueOf(args[11]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				return true;
			}
		}
		return false;
	}

}
