package be.hctel.renaissance.deathrun.traps;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;

public class TrapTool implements Listener {
	private String trapStartMsg = "§dTrap start set at %s";
	private String trapEndMsg = "§dTrap end set at %s";
	private String controlsStartMsg = "§dControls start set at %s";
	private String controlsEndMsg = "§dControls end set at %s";
	
	private DeathRun plugin;
	
	private Player player;
	
	private Location trapStartLocation;
	private Location trapEndLocation;
	private Location controlsStartLocation;
	private Location controlsEndLocation;
	
	private TrapType trapType;	
	private int width;
	private int steps;
	private long delay;
	private long resetDelay;
	private long cooldownDelay;
	
	private boolean isEvent = true;
	
	public TrapTool(DeathRun plugin, Player player, int width, int steps, long delay, long resetDelay, long cooldownDelay, TrapType type) {
		this.plugin = plugin;
		this.player = player;
		this.width = width;
		this.steps = steps;
		this.delay = delay;
		this.resetDelay = resetDelay;
		this.cooldownDelay = cooldownDelay;
		this.trapType = type;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onToolClick(PlayerInteractEvent e) {
		if(isEvent) {
			if(e.getPlayer().equals(player)) {
				if(e.getPlayer().getInventory().getItemInMainHand() == null) return;
				if(e.getAction() == Action.RIGHT_CLICK_BLOCK) {
					if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND) {
						controlsEndLocation = e.getClickedBlock().getLocation().clone();
						e.setCancelled(true);
						e.getPlayer().sendMessage(String.format(controlsEndMsg, Utils.locationToString(controlsEndLocation)));
					} else if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
						trapEndLocation = e.getClickedBlock().getLocation().clone();
						e.setCancelled(true);
						e.getPlayer().sendMessage(String.format(trapEndMsg, Utils.locationToString(trapEndLocation)));
					}
				} else if(e.getAction() == Action.LEFT_CLICK_BLOCK) {
					if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK) {
						trapStartLocation = e.getClickedBlock().getLocation().clone();
						e.setCancelled(true);
						e.getPlayer().sendMessage(String.format(trapStartMsg, Utils.locationToString(trapStartLocation)));
						
					} else if(e.getPlayer().getInventory().getItemInMainHand().getType() == Material.DIAMOND) {
						controlsStartLocation = e.getClickedBlock().getLocation().clone();
						e.setCancelled(true);
						e.getPlayer().sendMessage(String.format(controlsStartMsg, Utils.locationToString(controlsStartLocation)));
					}
				}
				System.out.println(String.format("%s, %s, %s, %s", Utils.locationToString(trapStartLocation), Utils.locationToString(trapEndLocation), Utils.locationToString(controlsStartLocation), Utils.locationToString(controlsEndLocation)));
			}
		}
	}
	
	public TrapControls getTrap() {
		System.out.println(String.format("%s, %s, %s, %s", Utils.locationToString(trapStartLocation), Utils.locationToString(trapEndLocation), Utils.locationToString(controlsStartLocation), Utils.locationToString(controlsEndLocation)));
		if(trapStartLocation == null | trapEndLocation == null | controlsStartLocation == null | controlsEndLocation == null) {
			player.sendMessage("§c1You must first define start and end positions!");
		}
		TrapControls controls = new TrapControls(plugin, controlsStartLocation, controlsEndLocation, trapStartLocation, trapEndLocation, this.width, this.trapEndLocation.clone().subtract(this.trapStartLocation).getBlockY(), this.steps, this.delay, this.trapType, this.resetDelay, this.cooldownDelay);
		isEvent=false;
		return controls;
	}

}
