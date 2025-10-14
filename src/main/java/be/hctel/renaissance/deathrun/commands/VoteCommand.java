package be.hctel.renaissance.deathrun.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.text.ChatMessages;

public class VoteCommand implements CommandExecutor {
	
	private DeathRun plugin;
	
	public VoteCommand(DeathRun plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(args.length == 0) {
				plugin.votesHandler.sendMapChoices(player);
			} else {
				try {
					int a = Utils.convertToInt(args[0]);
					plugin.votesHandler.registerPlayerVote(player, a, plugin.ranks.getRank(player).getVotes());
				} catch (NumberFormatException e) {
					player.sendMessage(ChatMessages.NAN.toText() + " (1-6).");
				}
			}
		}
		return true;
	}

}
