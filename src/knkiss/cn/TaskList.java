package knkiss.cn;

import knkiss.cn.task.collectTask;
import knkiss.cn.task.craftTask;
import knkiss.cn.task.task;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class TaskList {
    public static int amount = 0;
    public HashMap<String, task> list = new HashMap<>();

    TaskList(){

        Set<String> keys= Pass.config.getKeys(true);
        String pattern = "(task..*).level";
        Pattern r = Pattern.compile(pattern);
        keys.forEach(path->{
            Matcher m = r.matcher(path);
            if(m.find()){
                path = m.group(1);
                int num = Pass.config.getInt(path+".level");
                if(list.containsKey(String.valueOf(num))){
                    Pass.log.warning("存在多个相同level="+num+"的任务");
                }else{

                    if(Pass.config.getString(path+".type").equalsIgnoreCase("collect")) list.put(String.valueOf(num),new collectTask(path));
                    if(Pass.config.getString(path+".type").equalsIgnoreCase("craft")) list.put(String.valueOf(num),new craftTask(path));
                    amount ++;
                }
            }
        });
    }

    public boolean canFinish(Player p){
        return getTask(Pass.infoList.getPlayerLevel(p.getName())).canFinish(p);
    }

    public void Finish(Player p){
        getTask(Pass.infoList.getPlayerLevel(p.getName())).Finish(p);
    }

    public task getTask(int level){
        return list.get(String.valueOf(level));
    }

    public void check(CommandSender sender){
        list.forEach((level,Task)->{
            ItemStack logo = Task.logo;
            String type = Task.type;
            String name = logo.getItemMeta().getDisplayName();
            List<String> lore = logo.getItemMeta().getLore();
            int amount = logo.getAmount();
            int ID = logo.getTypeId();
            int Durability = logo.getDurability();
            boolean enable = Task.enable;
            sender.sendMessage("-------------TaskList-------------------");
            sender.sendMessage("level:" + level);
            sender.sendMessage("type:" + type);
            sender.sendMessage("name:" + name);
            sender.sendMessage("lore:" + lore.toString());
            sender.sendMessage("item:" + ID +" "+Durability+" "+amount);
            sender.sendMessage("Enable:" + enable);
        });
    }
}
