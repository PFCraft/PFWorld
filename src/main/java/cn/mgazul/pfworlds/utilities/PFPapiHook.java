package cn.mgazul.pfworlds.utilities;

import cn.mgazul.pfworlds.Commands.cmdWorld;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.PlaceholderHook;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class PFPapiHook extends PlaceholderHook{

	private static final String hook_name = "pfw";

	@Override
	public String onPlaceholderRequest(Player p, String i) {
    	if(p == null){
    	 	return null;
    	}
        if (i.equalsIgnoreCase("name")) {
        	World w = p.getWorld();
        	String worldname = w.getName();
            try {
                final FileConfiguration cfg = YamlConfiguration.loadConfiguration(cmdWorld.f);
                cfg.load(cmdWorld.f);
                if (cfg.getConfigurationSection("worlds.") != null && cmdWorld.f.exists()) {                  
                    if (cfg.get("worlds." + w.getName() + ".name") != null) {
                    	return cfg.getString("worlds." + w.getName() + ".name");
                    }else {
                    	return worldname;
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
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
