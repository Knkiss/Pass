package knkiss.cn;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

public class Pass extends JavaPlugin implements Listener {
    static FileConfiguration config;
    static PlayerInfoList infoList;
    static TaskList taskList;
    static Logger log;

    @Override
    public void onEnable() {
        this.getLogger().info("大家快来完成任务吧！");
        this.init();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))return false;
        if(args.length==0)return false;
        if(args[0].equalsIgnoreCase("me")){
            sender.sendMessage("你的任务等级为："+infoList.getPlayerLevel(sender.getName()));


        }else if(args[0].equalsIgnoreCase("finish")){
            Player p = (Player)sender;
            if(infoList.canFinish(p)){
                infoList.Finish(p);
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        infoList.addPlayer(e.getPlayer().getName());

    }

    public void init(){
        this.log = this.getLogger();
        Bukkit.getPluginManager().registerEvents(this,this);
        saveDefaultConfig();
        this.config = this.getConfig();
        this.infoList = new PlayerInfoList();
        this.taskList = new TaskList();
    }
}
