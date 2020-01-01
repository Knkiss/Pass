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
import org.bukkit.scheduler.BukkitRunnable;

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


    @Override
    public void onDisable(){
        Bukkit.getScheduler().cancelTasks(this);
        saveConfig();
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0)return false;

        if(args[0].equalsIgnoreCase("me")){
            //task me
            if(!(sender instanceof Player)) return false;
            Player p = (Player)sender;
            infoList.showTask(p);


        }else if(args[0].equalsIgnoreCase("finish")){
            //task finish
            if(!(sender instanceof Player)) return false;
            Player p = (Player)sender;
            if(infoList.canFinish(p)){
                infoList.Finish(p);
            }
        }else if(args[0].equalsIgnoreCase("check")){
            //task check [task/player]
            if (args.length == 1){
                infoList.check(sender);
                taskList.check(sender);
            }else{
                if(args[1].equalsIgnoreCase("task")){
                    taskList.check(sender);
                }else if(args[1].equalsIgnoreCase("player")){
                    infoList.check(sender);
                }else {
                    sender.sendMessage("/task check [task/player]");
                }
            }
        }else if(args[0].equalsIgnoreCase("set")){
            //task set <name> <number>
            if (args.length >= 3){
                //等待编辑
            }else{
                sender.sendMessage("/task set <name> <number>");
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

        new BukkitRunnable(){//更新线程
            @Override
            public void run(){
                saveConfig();
            }
        }.runTaskTimerAsynchronously(this,0,config.getInt("settings.saveTiming")*20);
    }
}
