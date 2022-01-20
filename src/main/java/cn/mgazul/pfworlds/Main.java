package cn.mgazul.pfworlds;

import cn.mgazul.pfworlds.Commands.cmdWorld;
import cn.mgazul.pfworlds.Listener.ClickEvent;
import cn.mgazul.pfworlds.utilities.Config;
import cn.mgazul.pfworlds.utilities.PFPapiHook;
import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class Main extends JavaPlugin  implements Listener {

    public static Main instance;
    public static String prefix = "§6世界管理 §8》 §7";
    public static ArrayList<WorldType> WorldTypes = new ArrayList<WorldType>();

    public String type;
    
    public void addWorldType() {
        WorldTypes.add(WorldType.FLAT);
        WorldTypes.add(WorldType.NORMAL);
        WorldTypes.add(WorldType.AMPLIFIED);
        WorldTypes.add(WorldType.LARGE_BIOMES);
    }
    
    public String getCommandName() {
        return this.type;
    }
    
    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public void onEnable() {
        loadConfig();
        this.addWorldType();
        Main.instance = this;
        Config.createFile();
        Config.loadWorlds();
        Config.addWorld("world");
        Config.addWorld("world_the_end");
        Config.addWorld("world_nether");
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.registerEvents();
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PFPapiHook().register();
        	Bukkit.getConsoleSender().sendMessage("§7[§6"+getDescription().getName()+"§7] §a变量系统已关联PlaceholderAPI.");
        } 
    }
    
    @Override
    public void onDisable() {
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new PFPapiHook().unregister();
        }
    }
    
    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ClickEvent(), this);
        pm.registerEvents(this, this);
        this.getCommand("world").setExecutor(new cmdWorld());
    }
    
    public void setCommandName(String type) {
        this.type = type;
    }

    public static void deleteDir(File path) {
        if (path.exists()) {
            File[] allContents = path.listFiles();
            if (allContents != null) {
                File[] array;
                for (int length = (array = allContents).length, i = 0; i < length; ++i) {
                    File file = array[i];
                    deleteDir(file);
                }
            }
            path.delete();
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {

    }
}
