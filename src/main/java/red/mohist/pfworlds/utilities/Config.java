package red.mohist.pfworlds.utilities;

import red.mohist.pfworlds.Main;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

public class Config {

    public static void add(String worldName, String key, Object info) {
        Main.instance.getConfig().set("worlds." + worldName + "." + key, info);
        Main.instance.saveConfig();
    }

    public static void addWorld(String w) {
        if (Bukkit.getWorld(w) != null) {
            World world = Bukkit.getWorld(w);
            add(w, "seed", world.getSeed());
            add(w, "worldtype", world.getWorldType().name());
            add(w, "environment", world.getEnvironment().name());
            add(w, "name", world.getName());
            add(w, "info", "-/-");
            add(w, "difficulty", "EASY");
        }
    }

    public static void loadWorlds() {
        if (Main.instance.getConfig().getConfigurationSection("worlds.") != null) {
            for (String w : Main.instance.getConfig().getConfigurationSection("worlds.").getKeys(false)) {
                if (Bukkit.getWorld(w) == null) {
                    Long seed = -1L;
                    String environment = "-1";
                    String worldtype = "NORMAL";
                    String difficulty = "EASY";
                    if (Main.instance.getConfig().get("worlds." + w + ".seed") != null) {
                        seed = Main.instance.getConfig().getLong("worlds." + w + ".seed");
                    } else {
                        seed = -1L;
                    }
                    if (Main.instance.getConfig().get("worlds." + w + ".environment") != null) {
                        environment = Main.instance.getConfig().getString("worlds." + w + ".environment");
                    } else {
                        environment = "-1";
                    }
                    if (Main.instance.getConfig().get("worlds." + w + ".worldtype") != null) {
                        worldtype = Main.instance.getConfig().getString("worlds." + w + ".worldtype");
                    } else {
                        worldtype = "NORMAL";
                    }
                    if (Main.instance.getConfig().get("worlds." + w + ".difficulty") != null) {
                        difficulty = Main.instance.getConfig().getString("worlds." + w + ".difficulty");
                    } else {
                        difficulty = "EASY";
                    }
                    new WorldCreator(w).seed(seed).type(WorldType.valueOf(worldtype)).environment(World.Environment.valueOf(environment)).createWorld().setDifficulty(Difficulty.valueOf(difficulty));
                }
            }
        }
    }

    public static void removeWorld(String worldName) {
        if (Bukkit.getWorld(worldName) != null) {
            World world = Bukkit.getWorld(worldName);
            for (String w : Main.instance.getConfig().getConfigurationSection("worlds.").getKeys(false)) {
                if (w.equals(world.getName()) && Main.instance.getConfig().getString("worlds." + world.getName()) != null) {
                    Main.instance.getConfig().set("worlds." + world.getName(), null);
                    Main.instance.saveConfig();
                }
            }
        }
    }

    public static void addSpawn(String w, Player player) {
        Location loc = player.getLocation();
        add(w, "spawn.x", loc.getX());
        add(w, "spawn.y", loc.getY());
        add(w, "spawn.z", loc.getZ());
        add(w, "spawn.yaw", loc.getYaw());
        add(w, "spawn.pitch", loc.getPitch());
    }

    public static void getSpawn(String w, Player player) {
        World world = Bukkit.getWorld(w);
        if (Main.instance.getConfig().getString("worlds." + world.getName()) != null) {
            double x = Main.instance.getConfig().getDouble("worlds." + world.getName() + ".spawn.x");
            double y = Main.instance.getConfig().getDouble("worlds." + world.getName() + ".spawn.y");
            double z = Main.instance.getConfig().getDouble("worlds." + world.getName() + ".spawn.z");
            double yaw = Main.instance.getConfig().getDouble("worlds." + world.getName() + ".spawn.yaw");
            double pitch = Main.instance.getConfig().getDouble("worlds." + world.getName() + ".spawn.pitch");
            player.teleport(new Location(world, x, y, z, (float) yaw, (float) pitch));
        } else {
            player.teleport(world.getSpawnLocation());
        }
    }
}