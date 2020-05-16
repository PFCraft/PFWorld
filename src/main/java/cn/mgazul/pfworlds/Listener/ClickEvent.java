package cn.mgazul.pfworlds.Listener;

import cn.mgazul.pfworlds.Commands.cmdWorld;
import cn.mgazul.pfworlds.Main;
import cn.mgazul.pfworlds.utilities.Config;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ClickEvent implements Listener{
    static ClickEvent intsanz;
    public static String changeInfo = "";
    
    public void createWorld(InventoryClickEvent event, Player p) {
        p.closeInventory();
        WorldType worldType = WorldType.getByName(event.getCurrentItem().getItemMeta().getDisplayName());
        if (worldType != null) {
            if (Main.WorldTypes.contains(worldType)) {
            	World w = Bukkit.createWorld(new WorldCreator(Main.instance.getCommandName()).type(worldType));
                p.teleport(Bukkit.getWorld(Main.instance.getCommandName()).getSpawnLocation());
                try {
                    Config.addWorld(w.getName());
                }
                catch (Exception e) {
                    e.printStackTrace();
                    p.sendMessage("世界无法得救.");
                }
            }
        }
        else {
            p.sendMessage(Main.prefix  + "§c无法找到此WorldType.");
        }
    }
    
    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        ClickEvent.intsanz = this;
        if (event.getWhoClicked() instanceof Player) {
            Player p = (Player)event.getWhoClicked();
            if (event.getView().getTitle().startsWith("§7世界名§8: ")) {
                try {
                    if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.MAP) {
                        this.createWorld(event, p);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    p.sendMessage(Main.prefix  + "发生了未知错误. ");
                }
            }
            else if (event.getView().getTitle().equals("§8》 §6世界")) {
                event.setCancelled(true);
                if (event.getCurrentItem() == null) {    //如果点击的是null，则返回
                    return;
              }
                if (event.getCurrentItem().getItemMeta().getDisplayName().startsWith("§7》")) {
                    String toSplit = event.getCurrentItem().getItemMeta().getDisplayName();
                    String[] splitted = toSplit.split("6");
                    if (Bukkit.getWorld(splitted[1]) != null) {
                        Config.getSpawn(splitted[1], p);
                    }
                    else {
                        cmdWorld.worldNotExists(p, splitted[1]);
                    }
                }
                else if (event.getCurrentItem().getItemMeta().getDisplayName().equals("§4§l关闭菜单")) {
                    p.closeInventory();
                }
            }
        }
    }
}
