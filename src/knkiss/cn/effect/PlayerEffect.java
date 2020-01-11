package knkiss.cn.effect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerEffect {
    Plugin plugin;

    public PlayerEffect(Plugin plugin){
        this.plugin = plugin;
    }

    public void YuanZhuTi_All(Player p){
        new BukkitRunnable(){
            @Override
            public void run() {
                float walkSpeed = p.getWalkSpeed();
                float flySpeed = p.getFlySpeed();

                p.setWalkSpeed(0);
                p.setFlySpeed(0);
                Location loc = p.getLocation();
                loc.add(0,0.1,0);
                for(double h=0;h<2;h+=0.1){
                    loc.add(0,0.1,0);
                    for(int t=0;t<360;t+=6){
                        double r = Math.toRadians(t);
                        double x = Math.cos(r);
                        double z = Math.sin(r);
                        loc.add(x,0,z);
                        loc.getWorld().spawnParticle(org.bukkit.Particle.DRIP_LAVA,loc,1,null);
                        loc.subtract(x, 0, z);
                    }
                    try {
                        Thread.sleep(40L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F,0F);
                p.setWalkSpeed(walkSpeed);
                p.setFlySpeed(flySpeed);
                this.cancel();
            }
        }.runTaskAsynchronously(plugin);
    }

    public void Yuan_Foot(Player p){
        new BukkitRunnable(){
            @Override
            public void run() {
                p.setWalkSpeed(0);
                p.setFlySpeed(0);
                Location loc = p.getLocation();
                loc.add(0,0.1,0);
                for(int t=0;t<360;t+=6){
                    double r = Math.toRadians(t);
                    double x = Math.cos(r);
                    double z = Math.sin(r);
                    for(double l=0;l<2;l+=0.1){
                        loc.add(x*l,0,z*l);
                        loc.getWorld().spawnParticle(org.bukkit.Particle.DRIP_LAVA,loc,1,null);
                        loc.subtract(x*l, 0, z*l);
                    }
                    try {
                        Thread.sleep(30L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1F,0F);
                p.setWalkSpeed((float) 0.2);
                p.setFlySpeed((float) 0.1);
                this.cancel();
            }
        }.runTaskAsynchronously(plugin);
    }
}
