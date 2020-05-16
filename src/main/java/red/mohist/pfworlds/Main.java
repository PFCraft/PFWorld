package red.mohist.pfworlds;

import red.mohist.pfworlds.Commands.cmdWorld;
import red.mohist.pfworlds.Listener.ClickEvent;
import red.mohist.pfworlds.utilities.Config;
import red.mohist.pfworlds.utilities.PFPapiHook;
import java.io.File;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import red.mohist.api.ServerAPI;

public class Main extends JavaPlugin{

    public static Main instance;
    public static String prefix = "§6世界管理 §8》 §7";
    public static ArrayList<WorldType> WorldTypes = new ArrayList<WorldType>();

    public String type;
    
    public void addWorldType() {
        // 兼容获取mod的世界类型
        for (WorldType type : WorldType.values()) {
            if (type.equals(WorldType.getByName("debug_all_block_states"))) {
                continue;
            }
            WorldTypes.add(type);
        }
    }
    
    public String getCommandName() {
        return this.type;
    }
    
    public void loadConfig() {
        this.saveDefaultConfig();
        this.saveConfig();
    }

    @Override
    public void onEnable() {
        try {
            if (ServerAPI.hasMod("mohsit")) {
                Bukkit.getConsoleSender().sendMessage("§感谢使用Mohist核心");
            }
        } catch (Exception ignored) {}
        loadConfig();
        this.addWorldType();
        Main.instance = this;
        Config.loadWorlds();
        Config.addWorld("world");
        Config.addWorld("DIM1");
        Config.addWorld("DIM-1");
        this.registerEvents();
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        	PFPapiHook.hook();
        	Bukkit.getConsoleSender().sendMessage("§7[§6"+getDescription().getName()+"§7] §a变量系统已关联PlaceholderAPI.");
        } 
    }
    
    @Override
    public void onDisable() {
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PFPapiHook.unhook();
        }
    }
    
    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ClickEvent(), this);
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
}
