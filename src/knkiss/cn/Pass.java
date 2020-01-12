package knkiss.cn;

import knkiss.cn.command.TestCommand;
import knkiss.cn.command.PassCommand;
import knkiss.cn.util.Effects;
import knkiss.cn.util.Messages;
import knkiss.cn.listener.InfoListener;
import knkiss.cn.listener.InventoryListener;
import knkiss.cn.player.PlayerInfoList;
import knkiss.cn.task.TaskList;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class Pass extends JavaPlugin implements Listener {
    public static Pass pass;
    public static File taskFile;
    public static File infoFile;
    public static FileConfiguration config;
    public static FileConfiguration taskConfig;
    public static FileConfiguration infoConfig;
    public static PlayerInfoList infoList;
    public static TaskList taskList;
    public static Effects effect;
    public static Messages messages;

    @Override
    public void onEnable() {
        pass = this;
        this.getLogger().info("大家快来完成任务吧！");
        this.init();
    }

    @Override
    public void onDisable(){
        Bukkit.getScheduler().cancelTasks(this);
        try {
            infoConfig.save(infoFile);
            taskConfig.save(taskFile);
            saveConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        Bukkit.getPluginManager().registerEvents(new InfoListener(),this);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(),this);
        Bukkit.getPluginCommand("pass").setExecutor(new PassCommand());
        Bukkit.getPluginCommand("test").setExecutor(new TestCommand());

        saveDefaultConfig();
        this.saveResource("task.yml", false);
        this.saveResource("info.yml", false);
        this.saveResource("lang.yml", false);

        taskFile = new File(this.getDataFolder(), "task.yml");
        infoFile = new File(this.getDataFolder(), "info.yml");
        taskConfig = YamlConfiguration.loadConfiguration(taskFile);
        infoConfig = YamlConfiguration.loadConfiguration(infoFile);
        config = this.getConfig();
        infoList = new PlayerInfoList();
        taskList = new TaskList();
        messages = new Messages();

        new BukkitRunnable(){//更新线程
            @Override
            public void run(){
                try {
                    infoConfig.save(infoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskTimerAsynchronously(this,0,config.getInt("settings.saveTiming")*20);

        effect= new Effects(this);
    }
}
