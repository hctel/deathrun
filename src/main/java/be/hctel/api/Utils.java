package be.hctel.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.concurrent.RecursiveTask;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.json.JSONException;
import org.json.JSONObject;

import com.comphenix.protocol.wrappers.EnumWrappers.Direction;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

/*
 * This file is a part of the Renaissance Project API
 */

public class Utils {
	/**
	 * Converts a string to an int
	 * @param input the string to convert
	 * @return the int
	 * @throws NumberFormatException if the provided String cannot be converted to an int
	 */
	public static int convertToInt(String input) throws NumberFormatException {
		return Integer.parseInt(input);
	}
	/**
	 * Gets an ItemStack matching a formatted name.
	 * Used mainly in the JSON storages
	 * @param formattedName the formatted name
	 * @return the ItemStack
	 */
	@SuppressWarnings("deprecation")
	public static ItemStack getItemStackFromFormattedName(String formattedName) {
		String[] parts = formattedName.split(":");
		String a = parts[0];
		String b = parts[1];
		int c = Utils.convertToInt(b);
		Material d = Material.matchMaterial(a);
		return new ItemStack(d, 1, (short) 0, (byte) c);
	}
	/**
	 * Gets the formatted name of an ItemStack
	 * @param item the ItemStack
	 * @return the formatted name
	 */
	@SuppressWarnings("deprecation")
	public static String getFormattedName(ItemStack item) {
		if(item.getType() == Material.REDSTONE_BLOCK) return "REDSTONE_BLOCK:0";
		else if (item.getData().getData() == 0) return item.getType().toString();
		else return item.getType().toString() + ":" + item.getData().getData();
	}
	/**
	 * Gets the UUID of an online player
	 * @param player the player to get the UUID of
	 * @return the UUID of the player
	 */
	public static String getUUID(Player player) {
		String u = player.getUniqueId().toString();
		String uu = u.replace("-", "");
		return uu;
	}
	/**
	 * Gets the UUID of an offline player
	 * @param player the player to get the UUID of
	 * @return the UUID of the player
	 */
	public static String getUUID(OfflinePlayer player) {
		String u = player.getUniqueId().toString();
		String uu = u.replace("-", "");
		return uu;
	}
	/**
	 * Get the user-friendly name of an ItemStack
	 * @param it the ItemStack to get the name of
	 * @return the name of te ItemStack
	 */
	public static String getUserItemName(ItemStack it) {
		return getUserItemName(it.getType());
	}
	/**
	 * Get the user-friendly name of a material
	 * @param a the Material to get the name of
	 * @return the name of te ItemStack
	 */
	public static String getUserItemName(Material a) {
		String aN = a.toString().toLowerCase().replace('_', ' ');
		String aN1 = StringUtils.capitalize(aN);
		return aN1;
	}
	/**
	 * Spawns a firework at the given location
	 * @param location the location where the firewxork should be spawned
	 */
	public static void spawnFireworks(Location location){
        Firework fw = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.setPower(1);
        fwm.addEffects(FireworkEffect.builder().withColor(Color.RED).withFade(Color.ORANGE).flicker(true).build());
        fw.setFireworkMeta(fwm);
        }
	/**
	 * Maps a value into a new range
	 * @param x The value to map
	 * @param in_min The old minimum value
	 * @param in_max The old max value
	 * @param out_min The new minimum value
	 * @param out_max The new max value
	 * @return The mapped value
	 */
	public static long map(long x, long in_min, long in_max, long out_min, long out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * Maps a value into a new range
	 * @param x The value to map
	 * @param in_min The old minimum value
	 * @param in_max The old max value
	 * @param out_min The new minimum value
	 * @param out_max The new max value
	 * @return The mapped value
	 */
	public static long map(int x, int in_min, int in_max, int out_min, int out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	/**
	 * Maps a value into a new range
	 * @param x The value to map
	 * @param in_min The old minimum value
	 * @param in_max The old max value
	 * @param out_min The new minimum value
	 * @param out_max The new max value
	 * @return The mapped value
	 */
	public static double map(float x, float in_min, float in_max, float out_min, float out_max) {
		  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
	private final static int CENTER_PX = 154;
	/**
	 * Sends a centered text to a player
	 * @param player The player to send the message to
	 * @param message The message to send
	 */
	public static void sendCenteredMessage(Player player, String message){
	        if(message == null || message.equals("")) player.sendMessage("");
	                message = ChatColor.translateAlternateColorCodes('&', message);
	 
	                int messagePxSize = 0;
	                boolean previousCode = false;
	                boolean isBold = false;
	 
	                for(char c : message.toCharArray()){
	                        if(c == '�'){
	                                previousCode = true;
	                                continue;
	                        }else if(previousCode == true){
	                                previousCode = false;
	                                if(c == 'l' || c == 'L'){
	                                        isBold = true;
	                                        continue;
	                                }else isBold = false;
	                        }else{
	                                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
	                                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
	                                messagePxSize++;
	                        }
	                }
	 
	                int halvedMessageSize = messagePxSize / 2;
	                int toCompensate = CENTER_PX - halvedMessageSize;
	                int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
	                int compensated = 0;
	                StringBuilder sb = new StringBuilder();
	                while(compensated < toCompensate){
	                        sb.append(" ");
	                        compensated += spaceLength;
	                }
	                player.sendMessage(sb.toString() + message);
	}
	/**
	 * Capitalizes the first letter of a string
	 * @param toCapitalize The string to capitalize
	 * @return The capitalized string.
	 */
	public static String capitalize(String toCapitalize) {
		return toCapitalize.substring(0, 1).toUpperCase() + toCapitalize.substring(1).toLowerCase();
	}
	/**
	 * Get the index of the maximum value in an {@link ArrayList}
	 * @param array The ArrayList ({@link Integer}) to get the max value key from
	 * @return The key of the max value
	 */
	public static int getMaxValuePosition(ArrayList<Integer> array) {
		int votes = 0;
		int key = 0;
		for(int i = 0; i < 6; i++) {
			if(array.get(i) > votes) {
				key = i;
				votes = array.get(i);
			}
		}
		return key;
	}
	/**
	 * Get the max assigned integer to an object in an {@link HashMap}
	 * @param map The HashMap to get the max assigned value from
	 * @return The object matching the max value
	 */
	public static Object getMaxValueKey(HashMap<Object, Integer> map) {
		int max = Integer.MIN_VALUE;
		for(Object o : map.keySet()) {
			if(map.get(o) > max) max = map.get(o);
		}
		return max;
	}
	/**
	 * Sends an action bar message to a player (used to clean other classes)
	 * @param player The player to send the message to
	 * @param msg The message to send
	 */
	@SuppressWarnings("deprecation")
	public static void sendActionBarMessage(Player player, String msg) {
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
	}
	
	
	public static ItemStack createQuickItemStack(Material material, String name) {
		ItemStack toReturn = new ItemStack(material, 1);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		toReturn.setItemMeta(meta);
		return toReturn;
		
	}
	
	public static ItemStack createQuickItemStack(Material material, String name, String...lore) {
		return createQuickItemStack(material, false, name, lore);
		
	}
	
	public static ItemStack createQuickItemStack(Material material, boolean enchanted, String name, String...lore) {
		ItemStack toReturn = new ItemStack(material, 1);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		if(enchanted) {
			meta.addEnchant(Enchantment.BANE_OF_ARTHROPODS, 1, true);
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}
		List<String> loreList = new ArrayList<>();
		for(String S : lore) loreList.add(S);
		meta.setLore(loreList);
		toReturn.setItemMeta(meta);
		return toReturn;
		
	}
	/**
	 * A quick way to create an ItemStack and make the code cleaner
	 * @deprecated This method uses an anmbiguous data type object
	 * @param material the material of the ItemStack
	 * @param damage the durability value of the ItemStack
	 * @param data the data value of the ItemStack
	 * @param name the name of the ItemStack
	 * @return the generated ItemStack
	 */
	public static ItemStack createQuickItemStack(Material material, short damage, byte data, String name) {
		ItemStack toReturn = new ItemStack(material, 1, damage, data);
		ItemMeta meta = toReturn.getItemMeta();
		meta.setDisplayName(name);
		toReturn.setItemMeta(meta);
		return toReturn;
	}
	/**
	 * Creates a delayed recursive loop
	 * @param plugin The main plugin instance
	 * @param initialParamValue The initial value (for(int i = <b>60</b>; i >...))
	 * @param maxParamValue The maximal value (for(int i = 60; i > <b>42</b>, i = -2))
	 * @param increment The increment to add every time the loop finishes(for(int i = 0; i > 42; i = -<b>2</b>))
	 * @param delay The delay to wait before repeating the task, expressed in server ticks
	 * @param task the {@link RecursiveTask} to run every time the loop is runned.
	 */
	public static void recursiveDelayed(Plugin plugin, int initialParamValue, int maxParamValue, int increment, long delay, RecursiveExecutable task) {
		
		new BukkitRunnable() {
			private int param = initialParamValue;
			private boolean isIncreasing = maxParamValue >= initialParamValue;
			@Override
			public void run() {
				if(isIncreasing) {
					if(param < maxParamValue) {
						task.run(param);
					} else cancel();
				} else {
					if(param > maxParamValue) {
						task.run(param);
					} else cancel();
				}
				param = +increment;
			}
		}.runTaskTimer(plugin, 0L, delay);
	}
	/**
	 * Gets the full UUID (untrimmed UUID) from a trimmed UUID
	 * @param formattedUUID the trimmed UUID
	 * @return the full UUID
	 */
	public static String getFullUUID(String formattedUUID) {
		String uuid = formattedUUID.replaceAll(                                            
			    "(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})",                            
			    "$1-$2-$3-$4-$5");
		return uuid;
	}
	/**
	 * Generates a random string
	 * @param lenght the lenght of the string
	 * @return a random string
	 */
	public static String randomString(int lenght) {
	    int leftLimit = 48; // numeral '0'
	    int rightLimit = 122; // letter 'z'
	    int targetStringLength = lenght;
	    Random random = new Random();

	    String generatedString = random.ints(leftLimit, rightLimit + 1)
	      .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
	      .limit(targetStringLength)
	      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
	      .toString();

	    return generatedString;
	}
	/**
	 * Checks if two locations have a deltaY less than 4
	 * @param a the first location
	 * @param b the second location
	 * @return whether the two locations have a deltaY less than 4
	 */
	public static boolean locationComparator(Location a, Location b) {
		return (a.getBlockX() == b.getBlockX() && a.getBlockZ() == b.getBlockZ() && (a.getBlockY() - b.getBlockY()) < 4);
	}
	
	public static Location locationFlattenner(Location loc) {
		return new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getYaw(), loc.getPitch());
	}
	public static Location substractLocation(Location loc, double x, double y, double z) {
		return new Location(loc.getWorld(), loc.getX() - x, loc.getY() - y, loc.getZ() - z, loc.getYaw(), loc.getPitch());
	}
	public static Location substractLocation(Location loc, double x, double y, double z, boolean ignoreYawPitch) {
		if(ignoreYawPitch) return new Location(loc.getWorld(), loc.getX() - x, loc.getY() - y, loc.getZ() - z, 0.1f, 0.1f);
		else return new Location(loc.getWorld(), loc.getX() - x, loc.getY() - y, loc.getZ() - z, loc.getYaw(), loc.getPitch());
	}
	public static String formatSeconds(int timeInSeconds)
	{
	    int secondsLeft = timeInSeconds % 3600 % 60;
	    int minutes = (int) Math.floor(timeInSeconds % 3600 / 60);
	    //int hours = (int) Math.floor(timeInSeconds / 3600);
	    String MM = ((minutes     < 10) ? "0" : "") + minutes;
	    String SS = ((secondsLeft < 10) ? "0" : "") + secondsLeft;

	    return MM + ":" + SS;
	} 
	
	public static Location substractLocation(Location a, Location b) {
		if(!a.getWorld().equals(b.getWorld())) throw new IllegalArgumentException("The two locations are not in the same world!");
		double x = a.getX() - b.getX();
		double y = a.getY() - b.getY();
		double z = a.getZ() - b.getZ();
		return new Location(a.getWorld(), x, y, z);
	}
	
	public static boolean doubleContains(List<Entity> list, ArrayList<Player> seekers) {
		for(Player P : seekers) {
			if(list.contains(P)) return true;
			else continue;
		}
		return false;
	}
	private static String readAll(Reader rd) throws IOException {
		    StringBuilder sb = new StringBuilder();
		    int cp;
		    while ((cp = rd.read()) != -1) {
		      sb.append((char) cp);
		    }
		    return sb.toString();
		  }
	
		  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
		    InputStream is = new URL(url).openStream();
		    try {
		      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		      String jsonText = readAll(rd);
		      JSONObject json = new JSONObject(jsonText);
		      return json;
		    } finally {
		      is.close();
		    }
	}
    public static String getHTTPResponse(String url, int timeout) {
		    HttpURLConnection c = null;
		    try {
		        URL u = new URL(url);
		        c = (HttpURLConnection) u.openConnection();
		        c.setRequestMethod("GET");
		        c.setRequestProperty("Content-length", "0");
		        c.setRequestProperty("content-type", "text/plain; charset=utf-8");
		        c.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36");
		        c.setUseCaches(false);
		        c.setAllowUserInteraction(false);
		        c.setConnectTimeout(timeout);
		        c.setReadTimeout(timeout);
		        c.connect();
		        int status = c.getResponseCode();

		        switch (status) {
		            case 200:
		            case 201:
		                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
		                StringBuilder sb = new StringBuilder();
		                String line;
		                while ((line = br.readLine()) != null) {
		                    sb.append(line+"\n");
		                }
		                br.close();
		                return sb.toString();
		        }

		    } catch (MalformedURLException ex) {
		       ex.printStackTrace();
		    } catch (IOException ex) {
		    	ex.printStackTrace();
		    } finally {
		       if (c != null) {
		          try {
		              c.disconnect();
		          } catch (Exception ex) {
		        	  ex.printStackTrace();
		          }
		       }
		    }
		    return null;
	  }
	  
	  public static <K,V extends Comparable<? super V>> 
          List<Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

		  List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
		
		  Collections.sort(sortedEntries, 
		          new Comparator<Entry<K,V>>() {
		              @Override
		              public int compare(Entry<K,V> e1, Entry<K,V> e2) {
		                  return e2.getValue().compareTo(e1.getValue());
		              }
		          }
		  );
		
		  return sortedEntries;
	  }
	  
	  public static Direction getPerpendicular(Direction direction) {
		  switch(direction) {
		case DOWN:
			return Direction.EAST;
		case EAST:
			return Direction.SOUTH;
		case NORTH:
			return Direction.EAST;
		case SOUTH:
			return Direction.WEST;
		case UP:
			return Direction.NORTH;
		case WEST:
			return Direction.NORTH;		
		default:
			return Direction.NORTH;
		  }
	  }
	  
	  public static Vector getDeltaFromDirection(double multiplier, Direction direction) {
		  switch(direction) {
		case DOWN:
			return new Vector(0, -multiplier, 0);
		case EAST:
			return new Vector(multiplier, 0, 0);
		case NORTH:
			return new Vector(0, 0, -multiplier);
		case SOUTH:
			return new Vector(0, 0, multiplier);
		case UP:
			return new Vector(0, multiplier, 0);
		case WEST:
			return new Vector(-multiplier, 0, 0);
		default:
			return new Vector(0, 0, 0);
		  }
	  }
	  
	  public static Vector getDeltaFromDirection(Direction direction) {
		  return getDeltaFromDirection(1, direction);
	  }
	  
	  public static int findDistance(Location locationA, Location locationB, Direction direction) {
		  Location result = locationB.subtract(locationA);
		  if(direction == Direction.DOWN | direction == Direction.UP) {
			  return result.getBlockY();
		  } else if(direction == Direction.EAST | direction == Direction.WEST) {
			  return result.getBlockX();
		  } else {
			  return result.getBlockZ();
		  }
	  }
	  
	  public static void transformToFallingBlock(Block b) {
		  	BlockData data = b.getBlockData();
		    Location loc = b.getLocation().add(0.5, 0.01, 0.5);

		    b.setType(Material.AIR);
		    FallingBlock fb = b.getWorld().spawnFallingBlock(loc, data);

		    fb.setDropItem(false);
		}
	  
	  public static String locationToString(Location loc) {
		  if(loc == null) return "null";
		  return String.format("(%d, %d, %d)", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	  }
	  
	  public static JSONObject locationToJson(Location loc) {
		  JSONObject o = new JSONObject();
		  o.put("world", loc.getWorld().getName());
		  o.put("x", loc.getX());
		  o.put("y", loc.getY());
		  o.put("z", loc.getZ());
		  o.put("yaw", loc.getYaw());
		  o.put("pitch", loc.getPitch());
		  return o;
	  }
	  
	  public static Location jsonToLocation(JSONObject o) {
		  try {
			  if(o.has("yaw")) return new Location(Bukkit.getWorld(o.getString("world")), o.getDouble("x"), o.getDouble("y"), o.getDouble("z"), o.getFloat("yaw"), o.getFloat("pitch"));
			  else return new Location(Bukkit.getWorld(o.getString("world")), o.getDouble("x"), o.getDouble("y"), o.getDouble("z"));
		  } catch(Exception e) {
			  throw new IllegalArgumentException("One of the needed JSON keys is missing or damaged");
		  }
	  }
}