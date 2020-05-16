package red.mohist.pfworlds.utilities;

import red.mohist.pfworlds.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PFPapiHook extends PlaceholderHook {

	private static final String hook_name = "pfw";

	@Override
	public String onPlaceholderRequest(Player p, String i) {
		if (i.equalsIgnoreCase("name")) {
			World w = p.getWorld();
			String worldname = w.getName();
			if (Main.instance.getConfig().getConfigurationSection("worlds.") != null) {
				if (Main.instance.getConfig().get("worlds." + worldname + ".name") != null) {
					return Main.instance.getConfig().getString("worlds." + w.getName() + ".name");
				} else {
					return worldname;
				}
			}
		}
		return null;
	}

	public static void hook() {
		PlaceholderAPI.registerPlaceholderHook(hook_name, new PFPapiHook());
	}

	public static void unhook() {
		PlaceholderAPI.unregisterPlaceholderHook(hook_name);
	}
	
    public static String replacepapi(OfflinePlayer player, String x) {
        return PlaceholderAPI.setPlaceholders(player, x.replace("&", "§"));
    }
}
