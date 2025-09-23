package be.hctel.renaissance.deathrun.traps;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.JSONObject;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.deathrun.engine.MainGameEngine;
import be.hctel.renaissance.deathrun.engine.Role;

public class TrapControls implements Listener {

	private DeathRun plugin;
	
	private static ItemStack triggerItem = Utils.createQuickItemStack(Material.MAGMA_CREAM, "§a§lTrigger trap!");
	private static ItemStack cooldownItem = Utils.createQuickItemStack(Material.SLIME_BALL, "§cTrap on cooldown...");
	
	private Location controlsStartLocation, controlsEndLocation, controlsCenterLocation;
	private Vector controlsSize;
	private ArmorStand centerEntity;
	private List<Block> glassBlocks = new ArrayList<>();
	private long cooldownDelay;
	private TrapStatus status = TrapStatus.READY;
	
	private TrapType type;
	private Location trapStartLocation;
	private Location trapStopLocation;
	private long trapResetDelay;
	private int steps;
	private long delay;
	private int width;
	private int height = 1;
	private BukkitRunnable postTrapTask;
	
	/**
	 * Instantiates the TrapControls
	 * 
	 * @param plugin this plugin's main class
	 * @param trap the controlled trap
	 * @param controlsStartLocation the controls region start location
	 * @param controlsEndLocation the controls region end location
	 * @param cooldownDelay the trap cooldown delay in ticks
	 */
	public TrapControls(DeathRun plugin, Location controlsStartLocation, Location controlsEndLocation, Location trapStartLocation, Location trapStopLocation, int trapWidth, int trapHeight, int trapSteps, long stepDelay, TrapType type, long trapResetDelay, long trapCooldownDelay) {
		this.plugin = plugin;
		
		
		this.controlsStartLocation = controlsStartLocation;
		this.controlsEndLocation = controlsEndLocation;
		this.cooldownDelay = trapCooldownDelay;
		
		this.plugin = plugin;
		this.type = type;
		this.trapStartLocation = trapStartLocation;
		this.trapStopLocation = trapStopLocation;
		this.steps = trapSteps;
		this.delay = stepDelay;
		this.trapResetDelay = trapResetDelay;
		this.width = trapWidth;
		this.height = trapHeight;
		
		controlsCenterLocation = new Location(this.controlsStartLocation.getWorld(), (this.controlsStartLocation.getX() + this.controlsEndLocation.getX())/2, (this.controlsStartLocation.getY() + this.controlsEndLocation.getY())/2, (this.controlsStartLocation.getZ() + this.controlsEndLocation.getZ())/2);
		controlsSize = this.controlsEndLocation.clone().subtract(this.controlsStartLocation).toVector();

		centerEntity = (ArmorStand) controlsCenterLocation.getWorld().spawnEntity(controlsCenterLocation, EntityType.ARMOR_STAND);
		centerEntity.setVisible(false);
		centerEntity.setBasePlate(false);
		centerEntity.setGravity(false);
		centerEntity.setInvulnerable(true);		
		
		for(int x = this.controlsStartLocation.getBlockX(); x <= this.controlsEndLocation.getBlockX(); x++) {
			for(int y = this.controlsStartLocation.getBlockY(); y <= this.controlsEndLocation.getBlockY(); y++) {
				for(int z = this.controlsStartLocation.getBlockZ(); z <= this.controlsEndLocation.getBlockZ(); z++) {
					Block b = new Location(this.controlsStartLocation.getWorld(), x, y, z).getBlock();
					if(b.getType().toString().contains("STAINED_GLASS")) {
						b.setType(Material.LIME_STAINED_GLASS);
						glassBlocks.add(b);
					}
				}
			}
		}
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		
		setupRunnables();
	}
	
	private void setupRunnables() {
		postTrapTask = new BukkitRunnable() {

			@Override
			public void run() {
				for(Block B : glassBlocks) B.setType(Material.ORANGE_STAINED_GLASS);
				status = TrapStatus.COOLDOWN1;
				new BukkitRunnable() {

					@Override
					public void run() {
						for(Block B : glassBlocks) B.setType(Material.LIME_STAINED_GLASS);
						status = TrapStatus.READY;
						for(Entity E : centerEntity.getNearbyEntities(controlsSize.getX()/2, controlsSize.getY()/2, controlsSize.getZ()/2)) {
							if(E instanceof Player) {
								Player player = (Player) E;
								if(player.getInventory().contains(cooldownItem)) {
									player.getInventory().setItem(3, triggerItem);
								}
							}
						}
					}
					
				}.runTaskLater(plugin, cooldownDelay/2);
			}
			
		};
	}
	
	public JSONObject getJsonObject() {
		JSONObject out = new JSONObject();
		out.put("startLocation", Utils.locationToJson(controlsStartLocation));
		out.put("endLocation", Utils.locationToJson(controlsEndLocation));
		JSONObject o = new JSONObject();
		o.put("startLocation", Utils.locationToJson(this.trapStartLocation));
		o.put("stopLocation", Utils.locationToJson(this.trapStopLocation));
		o.put("width", this.width);
		o.put("height", this.height);
		o.put("steps", steps);
		o.put("delay", delay);
		o.put("type", type.toString());
		o.put("trapReset", trapResetDelay);
		o.put("trapCooldown", cooldownDelay);
		o.put("controls", out);
		return o;
	}
	
	private void performTrap() {
		if(status == TrapStatus.READY) {
			setupRunnables();
			new Trap(plugin, trapStartLocation, trapStopLocation, width, height, steps, delay, type, trapResetDelay, cooldownDelay).startTrap();
			status = TrapStatus.COOLDOWN0;
			for(Block B : glassBlocks) B.setType(Material.RED_STAINED_GLASS);
			postTrapTask.runTaskLater(plugin, cooldownDelay/20);
			for(Entity E : centerEntity.getNearbyEntities(controlsSize.getX()/2, controlsSize.getY()/2, controlsSize.getZ()/2)) {
				if(E instanceof Player) {
					Player player = (Player) E;
					if(player.getInventory().contains(triggerItem)) {
						player.getInventory().setItem(3, cooldownItem);
					}
				}
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	private void onMove(PlayerMoveEvent e) {
		if(MainGameEngine.getRole(e.getPlayer()) != Role.RUNNER) {
			List<Entity> nearbyEntities = centerEntity.getNearbyEntities(controlsSize.getX()/2, controlsSize.getY()/2, controlsSize.getZ()/2);
			if(nearbyEntities.size() != 0) {
				if(nearbyEntities.contains(e.getPlayer())) {
					if(status == TrapStatus.READY) {
						e.getPlayer().getInventory().setItem(3, triggerItem);
					}
					else {
						e.getPlayer().getInventory().setItem(3, cooldownItem);
					}
				} else if(e.getPlayer().getInventory().contains(triggerItem) | e.getPlayer().getInventory().contains(cooldownItem)) {
					e.getPlayer().getInventory().setItem(3, null);
				}
			}
		}
	}
	
	@EventHandler
	private void onInteract(PlayerInteractEvent e) {
		Action eventAction = e.getAction();
		if(eventAction == Action.LEFT_CLICK_AIR | eventAction == Action.RIGHT_CLICK_AIR | eventAction == Action.LEFT_CLICK_BLOCK | eventAction == Action.RIGHT_CLICK_BLOCK) {
			if(e.getItem() != null) {
				if(e.getItem().getType() == Material.MAGMA_CREAM && e.getItem().getItemMeta().getDisplayName().contains("Trigger trap")) {
					List<Entity> nearbyEntities = centerEntity.getNearbyEntities(controlsSize.getX()/2, controlsSize.getY()/2, controlsSize.getZ()/2);
					if(nearbyEntities.size() != 0) {
						if(nearbyEntities.contains(e.getPlayer())) performTrap();
					}
				}
			}
		}
	}
	
	public void serverStop() {
		centerEntity.remove();
	}
	
	
	public static TrapControls getFromJsonObject(DeathRun plugin, JSONObject o) {
		try {
			Location startLocaction = Utils.jsonToLocation(o.getJSONObject("startLocation"));
			Location stopLocation = Utils.jsonToLocation(o.getJSONObject("stopLocation"));
			int width = o.getInt("width");
			int height = o.getInt("height");
			int steps = o.getInt("steps");
			long delay = o.getLong("delay");
			TrapType type = TrapType.valueOf(o.getString("type"));
			long trapReset = o.getLong("trapReset");
			long trapCooldown = o.getLong("trapCooldown");
			JSONObject controlsObject = o.getJSONObject("controls");
			Location controlsStart = Utils.jsonToLocation(controlsObject.getJSONObject("startLocation"));
			Location controlsEnd = Utils.jsonToLocation(controlsObject.getJSONObject("endLocation"));
			return new TrapControls(plugin, controlsStart, controlsEnd, startLocaction, stopLocation, width, height, steps, delay, type, trapReset, trapCooldown);
		} catch (Exception e){
			e.printStackTrace();
			throw new IllegalArgumentException("The source JSON does not include one of the needed values");
		}
	}
}