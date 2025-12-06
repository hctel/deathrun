package be.hctel.renaissance.deathrun.data;

import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import be.hctel.renaissance.deathrun.misc.StrafeColor;
import be.hctel.renaissance.global.stats.Stats;
import be.hctel.renaissance.global.storage.SQLConnector;

public class DRStats extends Stats {
	
	public DRStats(Plugin plugin, SQLConnector connector) {
		super(plugin, connector);
		this.defaultPrefsJson = "{\"viewOthers\":\"visible\", \"strafeColor\":\"GREEN\"}";
		this.defaultJoinMessage = "%s §7wants to run!";
		this.defaultJoinMessages = "[\"%s has joined.\"]";
	}
	
	public StrafeColor getStrafeColor(OfflinePlayer player) {
		if(getPreferences(player) == null) return StrafeColor.GREEN;
		else {
			return StrafeColor.valueOf(getPreferences(player).getString("strafeColor").toUpperCase());
		}
	}

}
