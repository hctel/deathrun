package be.hctel.renaissance.deathrun.commands.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import be.hctel.renaissance.deathrun.traps.TrapType;

public class TrapCommandCompleter implements TabCompleter {
	private List<String> empty = new ArrayList<String>();
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(args.length == 1) {
			List<String> tabs = new ArrayList<String>();
			for(TrapType T : TrapType.values()) {
				if(T.toString().startsWith(args[0].toUpperCase())) {
					tabs.add(T.toString());
				}
			}
			return tabs;
		}
		return empty;
	}

}
