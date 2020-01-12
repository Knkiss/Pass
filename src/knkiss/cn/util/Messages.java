package knkiss.cn.util;

import knkiss.cn.Pass;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;

@SuppressWarnings("deprecation")
public class Messages {
    public static File langFile;
    public static FileConfiguration langConfig;

    public static String prefix;
    public static String permission_message;
    public static String commandUsage_message;
    public static String setLevelFinish_message;
    public static String setLevelFault_message;
    public static String buyLevelFault1_message;
    public static String buyLevelFault2_message;
    public static String skipLevelFault1_message;
    public static String skipLevelFault2_message;
    public static String LocationOne_message;
    public static String PageFault_message;
    public static String TaskUnlock_message;
    public static String TaskFinished_message;
    public static String RewardFault_message;
    public static String TaskDisable_message;
    public static String TaskCollectFault_message;
    public static String TaskCraftFault_message;
    public static String TaskKillFault_message;
    public static String TaskLocationFault_message;

    public static String TaskFinish_bigTitle;
    public static String TaskFinish_smallTitle;
    public static String TaskSkip_bigTitle;
    public static String TaskSkip_smallTitle;

    public Messages(){
        langFile = new File(Pass.pass.getDataFolder(),"lang.yml");
        langConfig = YamlConfiguration.loadConfiguration(langFile);


        prefix = langConfig.getString("prefix");
        permission_message = langConfig.getString("permission-message");
        commandUsage_message = langConfig.getString("commandUsage-message");
        setLevelFinish_message = langConfig.getString("setLevelFinish-message");
        setLevelFault_message = langConfig.getString("setLevelFault-message");
        buyLevelFault1_message = langConfig.getString("buyLevelFault1-message");
        buyLevelFault2_message = langConfig.getString("buyLevelFault2-message");
        skipLevelFault1_message = langConfig.getString("skipLevelFault1-message");
        skipLevelFault2_message = langConfig.getString("skipLevelFault2-message");
        LocationOne_message = langConfig.getString("LocationOne-message");

        PageFault_message = langConfig.getString("PageFault-message");
        TaskUnlock_message = langConfig.getString("TaskUnlock-message");
        TaskFinished_message = langConfig.getString("TaskFinished-message");
        RewardFault_message = langConfig.getString("RewardFault-message");
        TaskDisable_message = langConfig.getString("TaskDisable-message");
        TaskCollectFault_message = langConfig.getString("TaskCollectFault-message");
        TaskCraftFault_message = langConfig.getString("TaskCraftFault-message");
        TaskKillFault_message = langConfig.getString("TaskKillFault-message");
        TaskLocationFault_message = langConfig.getString("TaskLocationFault-message");
        TaskFinish_bigTitle = langConfig.getString("TaskFinish-bigTitle");
        TaskFinish_smallTitle = langConfig.getString("TaskFinish-smallTitle");
        TaskSkip_bigTitle = langConfig.getString("TaskSkip-bigTitle");
        TaskSkip_smallTitle = langConfig.getString("TaskSkip-smallTitle");

    }

    public static String noPermission(String permission){
        return prefix+permission_message.replace("%permission",permission);
    }

    public static String faultCommand(String command){
        return prefix+commandUsage_message.replace("%command",command);
    }

    public static String setLevelFinish(String name,int level){
        return prefix+setLevelFinish_message.replace("%name",name).replace("%level",String.valueOf(level));
    }

    public static String setLevelFault(String name){
        return prefix+setLevelFault_message.replace("%name",name);
    }

    public static String buyLevelFault1(){
        return prefix+buyLevelFault1_message;
    }

    public static String buyLevelFault2(){
        return prefix+buyLevelFault2_message;
    }

    public static String skipLevelFault1(){
        return prefix+buyLevelFault1_message;
    }

    public static String skipLevelFault2(){
        return prefix+buyLevelFault2_message;
    }

    public static String locationOne(){
        return prefix+LocationOne_message;
    }

    public static String taskUnlock(){
        return prefix+TaskUnlock_message;
    }

    public static String taskFinished(){
        return prefix+TaskFinished_message;
    }

    public static String pageFault(){
        return prefix+PageFault_message;
    }

    public static String rewardFault(ItemStack item){
        return prefix+RewardFault_message.replace("%item",item.toString());
    }

    public static String taskDisable(){
        return prefix+TaskDisable_message;
    }

    public static String taskCollectFault(){
        return prefix+TaskCollectFault_message;
    }

    public static String taskCraftFault(List<ItemStack> craft){
        return prefix+TaskCraftFault_message.replace("%craft",craft.toString());
    }

    public static String taskKillFault(List<EntityType> kill){
        return prefix+TaskKillFault_message.replace("%kill",kill.toString());
    }

    public static String taskLocationFault(List<Location> location){
        return prefix+TaskLocationFault_message.replace("%location",location.toString());
    }

    public static void taskFinishTitle(Player p, int level){
        p.sendTitle(TaskFinish_bigTitle.replace("%level",String.valueOf(level)),TaskFinish_smallTitle.replace("%level",String.valueOf(level)));
    }

    public static void taskSkipTitle(Player p, int level){
        p.sendTitle(TaskSkip_bigTitle.replace("%level",String.valueOf(level)),TaskSkip_smallTitle.replace("%level",String.valueOf(level)));
    }

}
