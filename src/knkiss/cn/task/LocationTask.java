package knkiss.cn.task;

import knkiss.cn.Pass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class LocationTask extends Task {

    public List<Location> location = new ArrayList<>();

    public LocationTask(String path) {
        super(path);
        try{
            List<String> craft_item = Pass.taskConfig.getStringList(path+".location");
            String pattern = "(.*)-(.*)-(.*)-(.*)";
            Pattern r = Pattern.compile(pattern);
            craft_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int x = Integer.parseInt(m.group(1));
                    int y = Integer.parseInt(m.group(2));
                    int z = Integer.parseInt(m.group(3));
                    String world = m.group(4);
                    if(Bukkit.getWorld(world) == null){
                        location.add(new Location(Bukkit.getWorlds().get(0),x,y,z));
                    }else{
                        location.add(new Location(Bukkit.getWorld(world),x,y,z));
                    }
                }
            });
        }catch (Exception e){
            this.enable = false;
            Pass.log.warning("路径为"+path+"的任务扩展内容有误");
        }
    }

    @Override
    public boolean canFinish(Player p) {
        if(Pass.infoList.getPlayerInfo(p).location.isEmpty()){
            return true;
        }
        p.sendMessage("还需抵达："+Pass.infoList.getPlayerInfo(p).location.toString());
        return false;
    }

    @Override
    public void Finish(Player p) {
        Pass.infoList.list.get(p.getName().toLowerCase()).addReward(this.reward);
        Pass.infoList.addPlayerLevel(p.getName());
        p.sendTitle("任务完成！","当前等级："+ Pass.infoList.getPlayerLevel(p.getName())+" 已将奖励存储到奖励箱");
    }

    @Override
    public void sendToPlayer(CommandSender sender) {
        super.sendToPlayerSuper(sender);
        sender.sendMessage("Location: " + location.toString());
    }

}
