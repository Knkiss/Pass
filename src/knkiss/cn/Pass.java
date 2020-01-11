package knkiss.cn;

import knkiss.cn.command.TestCommand;
import knkiss.cn.command.PassCommand;
import knkiss.cn.effect.PlayerEffect;
import knkiss.cn.listener.InfoListener;
import knkiss.cn.listener.InventoryListener;
import knkiss.cn.player.PlayerInfoList;
import knkiss.cn.task.TaskList;
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
    public static PlayerEffect effect;
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
        Bukkit.getPluginCommand("pass").setExecutor(new PassCommand());
        Bukkit.getPluginCommand("test").setExecutor(new TestCommand());
        saveDefaultConfig();
        config = this.getConfig();
        infoList = new PlayerInfoList();
        taskList = new TaskList();

        new BukkitRunnable(){//更新线程
            @Override
            public void run(){
                saveConfig();
            }
        }.runTaskTimerAsynchronously(this,0,config.getInt("settings.saveTiming")*20);

        effect= new PlayerEffect(this);
    }
}
