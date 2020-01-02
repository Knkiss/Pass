package knkiss.cn.Listener;

import knkiss.cn.Pass;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class InfoListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Pass.infoList.addPlayer(e.getPlayer().getName());
    }
}
