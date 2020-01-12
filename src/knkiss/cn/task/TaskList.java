package knkiss.cn.task;

import knkiss.cn.Pass;
import knkiss.cn.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TaskList {
    public static int amount = 0;
    public HashMap<String, Task> list = new HashMap<>();

    public TaskList(){
        Set<String> keys= Pass.taskConfig.getKeys(true);
        String pattern = "(task..*).level";
        Pattern r = Pattern.compile(pattern);
        keys.forEach(path->{
            Matcher m = r.matcher(path);
            if(m.find()){
                path = m.group(1);
                int num = Pass.taskConfig.getInt(path+".level");
                if(list.containsKey(String.valueOf(num))){
                    Pass.log.warning("存在多个相同level="+num+"的任务");
                }else{
                    String type = Pass.taskConfig.getString(path+".type");
                    if(type.equalsIgnoreCase("collect")) list.put(String.valueOf(num),new CollectTask(path));
                    if(type.equalsIgnoreCase("craft")) list.put(String.valueOf(num),new CraftTask(path));
                    if(type.equalsIgnoreCase("kill")) list.put(String.valueOf(num),new KillTask(path));
                    if(type.equalsIgnoreCase("location")) list.put(String.valueOf(num),new LocationTask(path));
                    amount ++;
                }
            }
        });
    }

    public boolean canFinish(Player p){
        if(!list.containsKey(String.valueOf(Pass.infoList.getPlayerLevel(p.getName())))){
            p.sendMessage(Messages.taskDisable());
            return false;
        }
        if(!getTask(Pass.infoList.getPlayerLevel(p.getName())).enable){
            p.sendMessage(Messages.taskDisable());
            return false;
        }
        return getTask(Pass.infoList.getPlayerLevel(p.getName())).canFinish(p);
    }

    public void Finish(Player p){
        getTask(Pass.infoList.getPlayerLevel(p.getName())).Finish(p);
        Pass.effect.YuanZhuTi_All(p);
    }

    public Task getTask(int level){
        return list.get(String.valueOf(level));
    }

    public void check(CommandSender sender){
        list.forEach((level,Task)-> Task.sendToPlayer(sender));
    }
}
