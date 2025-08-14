package be.hctel.renaissance.deathrun.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.traps.Trap;
import be.hctel.renaissance.deathrun.traps.TrapType;

public class TestTrapCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 13) {
				Plugin plugin = DeathRun.plugin;
				int x1 = Integer.parseInt(args[0]);
				int y1 = Integer.parseInt(args[1]);
				int z1 = Integer.parseInt(args[2]);
				Location startLocation = new Location(player.getWorld(), x1, y1, z1);
				int x2 = Integer.parseInt(args[3]);
				int y2 = Integer.parseInt(args[4]);
				int z2 = Integer.parseInt(args[5]);
				Location stopLocation = new Location(player.getWorld(), x2, y2, z2);
				int x3 = Integer.parseInt(args[6]);
				int y3 = Integer.parseInt(args[7]);
				int z3 = Integer.parseInt(args[8]);
				Location modelStartLocation = new Location(player.getWorld(), x3, y3, z3);
				int width = Integer.parseInt(args[9]);
				int steps = Integer.parseInt(args[10]);
				long delay = Long.parseLong(args[11]);
				TrapType type = TrapType.valueOf(args[12]);
				Trap testTrap = new Trap(plugin, startLocation, stopLocation, modelStartLocation, width, steps, delay, type, 2*20L, 60*20L);
				testTrap.startTrap();
				player.sendRawMessage(testTrap.getTrapDetails());
				return true;
			} else if(args.length == 10) {
				Plugin plugin = DeathRun.plugin;
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
				player.sendRawMessage(testTrap.getTrapDetails());
				return true;
			}
		}
		return false;
	}

}
