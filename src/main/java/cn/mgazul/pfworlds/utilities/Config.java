package cn.mgazul.pfworlds.utilities;

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Config
{
    static File f;
    
    static {
        Config.f = new File("plugins/PFWorlds/worlds.yml");
    }
    
    public static void addInfo(String w, String info) {
        World world = Bukkit.getWorld(w);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
        if (Config.f.exists()) {
            try {
                cfg.load(Config.f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    cfg.set("worlds." + world.getName() + ".info", info);
                }
                cfg.save(Config.f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void addname(String w, String info) {
        World world = Bukkit.getWorld(w);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
        if (Config.f.exists()) {
            try {
                cfg.load(Config.f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    cfg.set("worlds." + world.getName() + ".name", info);
                }
                cfg.save(Config.f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setnandu(Player player, String nandu) {
        World world = player.getWorld();
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
        if (Config.f.exists()) {
            try {
                cfg.load(Config.f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    cfg.set("worlds." + world.getName() + ".difficulty", nandu);
                }
                cfg.save(Config.f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void addWorld(String w) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
            if (Config.f.exists()) {
                try {
                    cfg.load(Config.f);
                    if (cfg.getString("worlds." + world.getName()) == null) {
                        cfg.set("worlds." + world.getName() + ".seed", world.getSeed());
                        cfg.set("worlds." + world.getName() + ".worldtype", world.getWorldType().name());
                        cfg.set("worlds." + world.getName() + ".environment", world.getEnvironment().name());
                        cfg.set("worlds." + world.getName() + ".name",world.getName());
                        cfg.set("worlds." + world.getName() + ".info","-/-");
                        cfg.set("worlds." + world.getName() + ".difficulty", "EASY");
                    }
                    cfg.save(Config.f);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void createFile() {
        try {
            if (!Config.f.exists()) {
                File file = new File("plugins/PFWorlds", "worlds.yml");
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                try {
                    cfg.save(file);
                }
                catch (IOException var4) {
                    var4.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadWorlds() {
        try {
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
            cfg.load(Config.f);
            if (cfg.getConfigurationSection("worlds.") != null) {
                for (String w : cfg.getConfigurationSection("worlds.").getKeys(false)) {
                    if (Bukkit.getWorld(w) == null && Config.f.exists()) {
                        Long seed = -1L;
                        String environment = "-1";
                        String worldtype = "NORMAL";
                        String difficulty = "EASY";
                        if (cfg.get("worlds." + w + ".seed") != null) {
                            seed = cfg.getLong("worlds." + w + ".seed");
                        }
                        else {
                            seed = -1L;
                        }
                        if (cfg.get("worlds." + w + ".environment") != null) {
                            environment = cfg.getString("worlds." + w + ".environment");
                        }
                        else {
                            environment = "-1";
                        }
                        if (cfg.get("worlds." + w + ".worldtype") != null) {
                            worldtype = cfg.getString("worlds." + w + ".worldtype");
                        }
                        else {
                            worldtype = "NORMAL";
                        }
                        if (cfg.get("worlds." + w + ".difficulty") != null) {
                            difficulty = cfg.getString("worlds." + w + ".difficulty");
                        }
                        else {
                            difficulty = "EASY";
                        }
                        new WorldCreator(w).seed(seed).type(WorldType.valueOf(worldtype)).environment(World.Environment.valueOf(environment)).createWorld().setDifficulty(Difficulty.valueOf(difficulty));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void removeWorld(String w) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
            if (Config.f.exists()) {
                try {
                    cfg.load(Config.f);
                    if (cfg.getString("worlds." + world.getName()) != null) {
                        cfg.set("worlds." + world.getName(), null);
                        cfg.save(Config.f);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void addSpawn(String w, Player player) {
        World world = Bukkit.getWorld(w);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
        if (Config.f.exists()) {
            try {
                cfg.load(Config.f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    cfg.set("worlds." + world.getName() + ".spawn.x", player.getLocation().getX());
                    cfg.set("worlds." + world.getName() + ".spawn.y", player.getLocation().getY());
                    cfg.set("worlds." + world.getName() + ".spawn.z", player.getLocation().getZ());
                    cfg.set("worlds." + world.getName() + ".spawn.yaw", player.getLocation().getYaw());
                    cfg.set("worlds." + world.getName() + ".spawn.pitch", player.getLocation().getPitch());
                }
                cfg.save(Config.f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void getSpawn(String w, Player player) {
        World world = Bukkit.getWorld(w);
        FileConfiguration cfg = YamlConfiguration.loadConfiguration(f);
        if (f.exists()) {
            try {
                cfg.load(f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    double x = cfg.getDouble("worlds." + world.getName() + ".spawn.x");
                    double y = cfg.getDouble("worlds." + world.getName() + ".spawn.y");
                    double z = cfg.getDouble("worlds." + world.getName() + ".spawn.z");
                    double yaw = cfg.getDouble("worlds." + world.getName() + ".spawn.yaw");
                    double pitch = cfg.getDouble("worlds." + world.getName() + ".spawn.pitch");
                    player.teleport(new Location(world, x, y, z, (float)yaw, (float)pitch));
                } else {
                    player.teleport(world.getSpawnLocation());
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
