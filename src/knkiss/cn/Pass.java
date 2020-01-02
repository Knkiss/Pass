package knkiss.cn;

import knkiss.cn.Command.passCommand;
import knkiss.cn.Listener.InfoListener;
import knkiss.cn.Listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class Pass extends JavaPlugin implements Listener {
    public static FileConfiguration config;
    public static PlayerInfoList infoList;
    public static TaskList taskList;
    public static Logger log;

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

    public void init(){
        log = this.getLogger();
        Bukkit.getPluginManager().registerEvents(new InfoListener(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        Bukkit.getPluginCommand("pass").setExecutor(new passCommand());
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
