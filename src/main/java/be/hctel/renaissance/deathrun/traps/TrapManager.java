package be.hctel.renaissance.deathrun.traps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.JSONObject;

import be.hctel.api.Utils;
import be.hctel.renaissance.deathrun.DeathRun;
import be.hctel.renaissance.global.mapmanager.GameMap;

public class TrapManager implements Listener {
	
	private DeathRun plugin;
	
	private GameMap map;
	
	private ArrayList<TrapControls> traps = new ArrayList<>();
	
	public TrapManager(DeathRun plugin, GameMap map) {
		this.plugin = plugin;
		this.map = map;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	public void addTrap(TrapControls controls) {
		traps.add(controls);
		JSONObject controlsJson = controls.getJsonObject();
		map.getConfig().getJSONArray("traps").put(controlsJson);
	}
	
	public void addTrap(JSONObject trapJson) {
		traps.add(TrapControls.getFromJsonObject(plugin, trapJson));
	}
	
	public void showTrapManager(Player player) {
		if(traps.size() == 0) {
			player.sendMessage("§cNo traps on this world!");
		}
		Inventory inv = plugin.getServer().createInventory(null, Math.max(((traps.size()/9)+1)*9, 54), String.format("Trap manager: %s", map.getWorld().getName()));
		int counter = 0;
		for(TrapControls T : traps) {
			inv.setItem(counter, Utils.createQuickItemStack(T.getType().getGuiRepresentation(), false, T.getType().getFamiliarName(), "§4" + Utils.locationToString(T.getTrapStartLocation()), "\n", "§cClick to delete."));
			counter++;
		}
		player.openInventory(inv);
	}
	
	@EventHandler
	public void onInventory(InventoryClickEvent e) {
		if(e.getView().getTitle().startsWith("Trap manager")) {
			String worldName = e.getView().getTitle().split(": ")[1];
			if(map.getWorld().getName().equalsIgnoreCase(worldName)) {
				if(e.getCurrentItem() == null) {
					e.setCancelled(true);
					return;
				}
				traps.remove(e.getRawSlot());
				plugin.mapManager.getMapTraps(Bukkit.getWorld(worldName)).remove(e.getRawSlot());
				e.setCancelled(true);
				e.getView().close();
				new BukkitRunnable() {
					
					@Override
					public void run() {
						showTrapManager((Player) e.getWhoClicked());
					}
				}.runTaskLater(plugin, 1L);
			}
		}
	}
	
	public void endGame() {
		for(TrapControls T : traps) {
			T.gameEnd();
		}
	}
	
	public void stop() {
		for(TrapControls T : traps) {
			T.serverStop();
		}
	}
	
}
