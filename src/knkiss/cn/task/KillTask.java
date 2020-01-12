package knkiss.cn.task;

import knkiss.cn.Pass;
import knkiss.cn.util.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class KillTask extends Task {

    public List<EntityType> kill = new ArrayList<>();

    public KillTask(String path) {
        super(path);
        try{
            List<String> craft_item = Pass.taskConfig.getStringList(path+".kill");
            String pattern = "(.*)-(.*)";
            Pattern r = Pattern.compile(pattern);
            craft_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int typeID = Integer.parseInt(m.group(1));
                    int number = Integer.parseInt(m.group(2));
                    for(int i=0;i<number;i++){
                        kill.add(EntityType.fromId(typeID));
                    }
                }
            });
        }catch (Exception e){
            this.enable = false;
            Pass.pass.getLogger().warning("路径为"+path+"的任务扩展内容有误");
        }
    }

    @Override
    public boolean canFinish(Player p) {
        if(Pass.infoList.getPlayerInfo(p).kill.isEmpty()){
            return true;
        }
        p.sendMessage(Messages.taskKillFault(Pass.infoList.getPlayerInfo(p).kill));
        return false;
    }

    @Override
    public void Finish(Player p) {
        Pass.infoList.list.get(p.getName().toLowerCase()).addReward(this.reward);
        Pass.infoList.addPlayerLevel(p.getName());
        Messages.taskFinishTitle(p,Pass.infoList.getPlayerLevel(p.getName()));
    }

    @Override
    public void sendToPlayer(CommandSender sender) {
        super.sendToPlayerSuper(sender);
        sender.sendMessage("Kill: " + kill.toString());
    }
}
