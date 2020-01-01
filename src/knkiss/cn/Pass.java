package knkiss.cn;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length==0)return false;

        if(args[0].equalsIgnoreCase("me")){
            //pass me
            if(!(sender instanceof Player)) return false;
            Player p = (Player)sender;
            infoList.showTask(p);


        }else if(args[0].equalsIgnoreCase("finish")){
            //pass finish
            if(!(sender instanceof Player)) return false;
            Player p = (Player)sender;
            if(infoList.canFinish(p)){
                infoList.Finish(p);
            }
        }else if(args[0].equalsIgnoreCase("check")){
            //pass check [task/player]
            if (args.length == 1){
                infoList.check(sender);
                taskList.check(sender);
            }else{
                if(args[1].equalsIgnoreCase("task")){
                    taskList.check(sender);
                }else if(args[1].equalsIgnoreCase("player")){
                    infoList.check(sender);
                }else {
                    sender.sendMessage("/pass check [task/player]");
                }
            }
        }else if(args[0].equalsIgnoreCase("set")){
            //pass set <name> <number>
            if (args.length == 3){
                //等待编辑
                if(util.canParseInt(args[2])){
                    int level = Integer.parseInt(args[2]);
                    if(!infoList.setPlayerLevel(args[1],level)){
                        sender.sendMessage("不存在 "+ args[1] +" 玩家");
                    }else{
                        if (level >= PlayerInfoList.maxLevel) level = 1;
                        sender.sendMessage("已将 "+ args[1] +" 玩家等级设置为 "+level);
                    }
                }else{
                    sender.sendMessage("/pass set <name> <number> number必须为纯数字");
                }
            }else{
                sender.sendMessage("/pass set <name> <number>");
            }
        }else if(args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")){
            //pass help/?
            sender.sendMessage("/pass help/? - 查看帮助");
            sender.sendMessage("/pass me - 查看自己的任务表");
            sender.sendMessage("/pass finish - 完成当前任务");
            sender.sendMessage("/pass check [task/player] - 查看当前内部信息");
            sender.sendMessage("/pass set <name> <level:1~maxlevel> - 设置玩家等级");
        }
        return true;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        infoList.addPlayer(e.getPlayer().getName());
    }

    public void init(){
        log = this.getLogger();
        Bukkit.getPluginManager().registerEvents(this,this);
        saveDefaultConfig();
        config = this.getConfig();
        infoList = new PlayerInfoList();
        taskList = new TaskList();

        new BukkitRunnable(){//更新线程
            @Override
            public void run(){
                saveConfig();
                log.info("数据保存");
            }
        }.runTaskTimerAsynchronously(this,0,config.getInt("settings.saveTiming")*20);
    }
}
