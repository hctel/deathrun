package be.hctel.renaissance.deathrun.commands.completers;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class StaffCommandCompleter implements TabCompleter{
	private List<String> empty = new ArrayList<String>();
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		if(command.getName().equalsIgnoreCase("dms")) {
			List<String> out = new ArrayList<String>();
			if(args.length == 1) {
				if(args[0].toLowerCase().startsWith("r")) out.add("runner");
				else if(args[0].toLowerCase().startsWith("d")) out.add("death");
				else {
					out.add("runner");
					out.add("death");
				}
				return out;
			}
			else if(args.length == 2) {
				for(Material M : Material.values()) {
					if(M.toString().startsWith(args[0].toUpperCase())) {
						out.add(M.toString());
					}
				}
				return out;
			}
		}
		return empty;
	}

}
