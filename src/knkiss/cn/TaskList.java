package knkiss.cn;

import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class TaskList {
    public static int amount = 0;
    public HashMap<String,Task> list = new HashMap<>();

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
                    list.put(String.valueOf(num),new Task(path));
                    amount ++;
                }
            }
        });
        for(int i=amount+1;i<=60;i++){
            list.put(String.valueOf(i),new Task(i));
            amount++;
        }
    }

    public Task getTask(int level){
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
