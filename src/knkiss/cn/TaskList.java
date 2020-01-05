package knkiss.cn;

import knkiss.cn.task.collectTask;
import knkiss.cn.task.craftTask;
import knkiss.cn.task.killTask;
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
                    String type = Pass.config.getString(path+".type");
                    if(type.equalsIgnoreCase("collect")) list.put(String.valueOf(num),new collectTask(path));
                    if(type.equalsIgnoreCase("craft")) list.put(String.valueOf(num),new craftTask(path));
                    if(type.equalsIgnoreCase("kill")) list.put(String.valueOf(num),new killTask(path));
                    amount ++;
                }
            }
        });
    }

    public boolean canFinish(Player p){
        if(!list.containsKey(String.valueOf(Pass.infoList.getPlayerLevel(p.getName())))){
            p.sendMessage("暂时无下个任务，请等候开启");
            return false;
        }
        if(!getTask(Pass.infoList.getPlayerLevel(p.getName())).enable){
            p.sendMessage("下个任务还未开启，请等候开启");
            return false;
        }
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
