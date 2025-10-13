package be.hctel.renaissance.deathrun.misc;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;

public class SpawnWallTool implements Listener {
	
	private DeathRun plugin;
	private Player player;
	
	private boolean cancelled;
	
	private Location loc1, loc2;
	
	private static ItemStack tool = Utils.createQuickItemStack(Material.DIAMOND_HOE, "§6§lStart wall tool");
	
	public SpawnWallTool(DeathRun plugin, Player player) {
		this.plugin = plugin;
		this.player = player;
		plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
		player.getInventory().setItemInMainHand(tool);
	}
	
	@EventHandler
	private void onInteract(PlayerInteractEvent e) {
		if(!cancelled) {
			if(e.getPlayer().equals(player)) {
				if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND_HOE) {
					switch(e.getAction()) {
					case LEFT_CLICK_BLOCK: {
						loc1 = e.getClickedBlock().getLocation();
						player.sendMessage(String.format("§dFirst location set at %s", Utils.locationToString(loc1)));
						e.setCancelled(true);
						break;
					}
					case RIGHT_CLICK_BLOCK: {
						loc2 = e.getClickedBlock().getLocation();
						player.sendMessage(String.format("§dSecond location set at %s", Utils.locationToString(loc2)));
						e.setCancelled(true);
						break;
					}
					default: return;
					}
				}
			}
		}
	}
	
	public List<Location> getLocations() {
		if(loc1 == null && loc2==null) throw new NullPointerException("One or both locations are null.");
		cancelled = true;
		return Arrays.asList(new Location[] {loc1, loc2});
	}
	
}
