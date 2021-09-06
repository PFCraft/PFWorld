package com.mohistmc.pfworlds.Commands;

import com.mohistmc.pfworlds.Main;
import com.mohistmc.pfworlds.utilities.Config;
import com.mohistmc.pfworlds.utilities.itemAPI;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.util.ArrayList;

public class cmdWorld implements CommandExecutor{

    public static File f = new File("plugins/PFWorlds/worlds.yml");
    
    
    public static void openWorldGui(Player p, String name) {
        int groesse = 54;
        int pos = 0;
        while (Bukkit.getWorlds().size() > groesse) {
            groesse += 54;
        }
        Inventory inv = Bukkit.createInventory(null, groesse, name);
        for (World w : Bukkit.getWorlds()) {
            ArrayList<String> infoLore = new ArrayList<String>();
            try {
                FileConfiguration cfg = YamlConfiguration.loadConfiguration(cmdWorld.f);
                cfg.load(cmdWorld.f);
                if (cfg.getConfigurationSection("worlds.") != null && cmdWorld.f.exists()) {
                    String worldtype = "N/A";
                    String infos;
                    String name1;
                    String difficulty;
                    if (cfg.get("worlds." + w.getName() + ".info") != null) {
                        infos = cfg.getString("worlds." + w.getName() + ".info");
                        worldtype = cfg.getString("worlds." + w.getName() + ".worldtype");
                        name1 = cfg.getString("worlds." + w.getName() + ".name");
                        difficulty = cfg.getString( "worlds." + w.getName() + ".difficulty");
                    } else {
                        infos = "§7-/-";
                        name1 = w.getName();
                        difficulty = w.getDifficulty().name();
                    }
                    infoLore.add("§b世界别名 §8》 §7" + name1.replace("&", "§"));
                    infoLore.add("§b信息 §8》 §7" + infos.replace("&", "§"));
                    infoLore.add("§b世界边界 §8》 §7" + String.valueOf(w.getWorldBorder().getSize()));
                    infoLore.add("§b世界类型 §8》 §7" + worldtype);
                    infoLore.add("§b世界难度 §8》 §7" + difficulty);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            inv.setItem(pos, itemAPI.doItem(Material.MAP, 1, "§7》 §6" + w.getName().toString(), infoLore));
            ++pos;
            infoLore.clear();
        }
        inv.setItem(53, itemAPI.doItem(Material.REDSTONE_BLOCK, 1, "§4§l关闭菜单", null));
        p.openInventory(inv);
    }
    
    public static void worldNotExists(Player p, String world) {
        p.sendMessage(Main.prefix + "这个世界<"+ world +">不存在。 可以通过§6/world list§7看到现有的世界!");
    }

    public static void worldAllExists(Player p, String world) {
        p.sendMessage(Main.prefix + "这个世界<"+ world +">已存在。 可以通过§6/world list§7看到现有的世界!");
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player)cs;
            if (args.length == 0 && cs.isOp()) {
                this.sendHelp(p);
            }
            //世界列表
            if (args.length == 1 && args[0].equalsIgnoreCase("list") && cs.isOp()) {
                openWorldGui(p, "§8》 §6世界");
            }
            //创建世界
            if (args.length == 2 && args[0].equalsIgnoreCase("create") && cs.isOp()) {
                if (Bukkit.getWorld(args[1]) == null) {
                    Main.instance.setCommandName(args[1]);
                    int i = -1;
                    Inventory inv = Bukkit.createInventory(null, 27, "§7世界名§8: §c" + args[1]);
                    for (WorldType worldType : Main.WorldTypes) {
                        i++;
                        inv.setItem(i, itemAPI.doItem(Material.MAP, 1, worldType.getName(), null));
                    }
                    p.openInventory(inv);
                } else {
                    worldAllExists(p, args[1]);
                }
            }
            //传送到世界
            if (args.length == 2 && args[0].equalsIgnoreCase("tp") && cs.isOp()) {
                World world = Bukkit.getWorld(args[1]);
                if (world == null) {
                    worldNotExists(p,args[1]);
                    return true;
                }
                Config.getSpawn(args[1], p);
                p.sendMessage(Main.prefix + "已传送到世界§6 " + args[1] + " §7的出生点.");
            }
            //删除世界
            if (args.length == 2 && args[0].equalsIgnoreCase("delete") && cs.isOp()) {
                        if (!args[1].equalsIgnoreCase("world")) {
                            World w = Bukkit.getWorld(args[1]);
                            if (w != null) {
                                for (Player all : Bukkit.getOnlinePlayers()) {
                                    if (all.getWorld() == w) {
                                        all.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                    }
                                }
                            }
                            try {
                                Bukkit.unloadWorld(w, true);
                                File deleteWorld = w.getWorldFolder();
                                Main.deleteDir(deleteWorld);
                                p.sendMessage(Main.prefix + "世界被成功删除.");
                                Config.removeWorld(args[1]);
                            }
                            catch (Exception e2) {
                                p.sendMessage(Main.prefix + "§c世界无法删除.");
                            }
                        } else {
                            p.sendMessage(Main.prefix + "§c你无法删除这个世界!");
                        }
                    }
             //加载世界
            if (args.length == 2 && args[0].equalsIgnoreCase("import") && cs.isOp()) {
                try {
                    World w = Bukkit.getWorld(args[1]);
                    p.teleport(w.getSpawnLocation());
                    p.sendMessage(Main.prefix + "这个世界已经存在。 已将你传送.");
                } catch (Exception e3) {
                    File loadWorld = new File(args[1]);
                    if (loadWorld.exists()) {
                        p.sendMessage(Main.prefix + "加载世界中.");
                        new WorldCreator(args[1]).environment(World.Environment.NORMAL).createWorld();
                        Config.addWorld(args[1]);
                        p.sendMessage(Main.prefix + "这个世界已被加载.");
                    } else {
                        p.sendMessage(Main.prefix + "找不到你要加载的世界文件.");
                    }
                }
            }
            //卸载世界
            if (args.length == 2 && args[0].equalsIgnoreCase("unload") && cs.isOp()) {
                if (Bukkit.getWorld(args[1]) != null) {
                    return true;
                }
                for (Player all2 : Bukkit.getOnlinePlayers()) {
                    if (all2.getWorld() == Bukkit.getWorld(args[1])) {
                        all2.teleport(Bukkit.getWorld("world").getSpawnLocation());
                    }
                }
                Bukkit.unloadWorld(Bukkit.getWorld(args[1]), true);
                Config.removeWorld(args[1]);
                p.sendMessage(Main.prefix + "§c卸载成功");
            }
            //设置世界介绍
            if (args.length == 2 && args[0].equalsIgnoreCase("addinfo") && cs.isOp()) {
                World w = p.getWorld();
                Config.addInfo(w.getName(), args[1]);
                p.sendMessage(Main.prefix  + "已添加信息§b " + args[1] + "§7 到配置文件.");
            }
            //查看世界名字
            if (args.length == 1 && args[0].equalsIgnoreCase("info") && cs.isOp()) {
                p.sendMessage(Main.prefix + "当前位于世界: " + p.getWorld().getName());
            }
            //设置世界中文名
            if (args.length == 2 && args[0].equalsIgnoreCase("setname") && cs.isOp()) {
                String worldname = p.getWorld().getName();
                Config.addname(worldname, args[1]);
                p.sendMessage(Main.prefix + "§c成功设置别名:"+ args[1].replace("&", "§"));
            }
            //设置世界出生点
            if (args.length == 1 && args[0].equalsIgnoreCase("setspawn") && cs.isOp()) {
                Config.addSpawn(p.getWorld().getName(), p);
                p.sendMessage(Main.prefix + "§c成功设置");
            }
            //设置世界难度
            if (args.length == 2 && args[0].equalsIgnoreCase("difficulty") && cs.isOp()) {
                if (Main.isInteger(args[1])) {
                    int nandu = Integer.valueOf(args[1]);
                    if (nandu >= 0 && nandu < 4) {
                        if (nandu == 0) {
                            p.getWorld().setDifficulty(Difficulty.PEACEFUL);
                            Config.setnandu(p, "PEACEFUL");
                            p.sendMessage(Main.prefix + "§a成功设置难度为和平!");
                        }
                        else if (nandu == 1) {
                            p.getWorld().setDifficulty(Difficulty.EASY);
                            Config.setnandu(p, "EASY");
                            p.sendMessage(Main.prefix + "§a成功设置难度为简单!");
                        }
                        else if (nandu == 2) {
                            p.getWorld().setDifficulty(Difficulty.NORMAL);
                            Config.setnandu(p, "NORMAL");
                            p.sendMessage(Main.prefix + "§a成功设置难度为普通!");
                        }
                        else if (nandu == 3) {
                            p.getWorld().setDifficulty(Difficulty.HARD);
                            Config.setnandu(p, "HARD");
                            p.sendMessage(Main.prefix + "§a成功设置难度为困难!");
                        }
                    }
                    else{
                        p.sendMessage(Main.prefix + "§c成功失败，请输入数字0-3!");
                    }
                }
                else{
                    p.sendMessage(Main.prefix + "§c成功失败，请输入数字0-3!");
                }
            }
        }
        //传送到世界 Player <控制台和命令方块可用>
        if (args.length == 3 && args[0].equalsIgnoreCase("tp") && cs.isOp()) {
            for(Player target : Bukkit.getOnlinePlayers()) {
                String name = target.getName();
                String argsname = args[1];
                if(name == null ) {
                    cs.sendMessage("改玩家不存在");
                    return true;
                }
                if(argsname.equals(name)) {
                    Player target1 = Bukkit.getServer().getPlayer(argsname);
                    World world = Bukkit.getWorld(args[2]);
                    if (world == null) {
                        target1.sendMessage(Main.prefix + "这个世界<"+ args[2] +">不存在!传送失败!请联系管理员!");
                        return true;
                    }
                    Config.getSpawn(args[2], target1);
                }
            }
        }
        return false;
    }
    
    private void sendHelp(Player p) {
        p.sendMessage(Main.prefix + "/world create <Name> 创建世界");
        p.sendMessage(Main.prefix + "/world delete <Name> 删除世界");
        p.sendMessage(Main.prefix + "/world tp <Name> 传送世界");
        p.sendMessage(Main.prefix + "/world tp <Player> <Name> 将玩家传送到指定世界");
        p.sendMessage(Main.prefix + "/world import <Name> 加载世界");
        p.sendMessage(Main.prefix + "/world unload <Name> 卸载世界");
        p.sendMessage(Main.prefix + "/world info 查看当前世界的名字");
        p.sendMessage(Main.prefix + "/world addinfo <info> 设置世界介绍");
        p.sendMessage(Main.prefix + "/world setname <Name> 设置当前世界别名");
        p.sendMessage(Main.prefix + "/world setspawn 设置世界出生点");
        p.sendMessage(Main.prefix + "/world list  世界列表GUI");
        p.sendMessage(Main.prefix + "/world difficulty <0-3>  设置当前世界难度");
        p.sendMessage(" ");
    }
}
