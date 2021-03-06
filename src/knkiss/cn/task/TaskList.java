package knkiss.cn.task;

import knkiss.cn.Pass;
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
                    if(type.equalsIgnoreCase("location")) list.put(String.valueOf(num),new locationTask(path));
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

    public Task getTask(int level){
        return list.get(String.valueOf(level));
    }

    public void check(CommandSender sender){
        list.forEach((level,Task)-> Task.sendToPlayer(sender));
    }
}
