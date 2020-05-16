package cn.mgazul.pfworlds.Listener;

import cn.mgazul.pfworlds.Main;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

/**
 * @author Mgazul
 * @date 2020/4/8 23:31
 */
public class BungeeCordEnd implements Listener {

    @EventHandler
    public void tp(PlayerPortalEvent e){
        // 生存服
        if (Main.instance.getConfig().getInt("type") == 0) {
            // 末地门
            if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                this.senderPlayer(Main.instance.getConfig().getString("modi"), e.getPlayer());
                e.setCancelled(true);
            }
        }
        // 冒险世界
        if (Main.instance.getConfig().getInt("type") == 1) {
            if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) {
                this.senderPlayer("pfcraftsc", e.getPlayer());
                e.setCancelled(true);
            }
        }
    }

    public void senderPlayer(String server, Player p) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(server);
        p.sendPluginMessage(Main.instance, "BungeeCord", out.toByteArray());
    }
}
