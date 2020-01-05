package knkiss.cn.task;

import knkiss.cn.Pass;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class killTask extends task {

    public List<EntityType> kill = new ArrayList<>();

    public killTask(String path) {
        super(path);
        try{
            List<String> craft_item = Pass.config.getStringList(path+".kill");
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
            Pass.log.warning("路径为"+path+"的任务扩展内容有误");
        }
    }

    @Override
    public boolean canFinish(Player p) {
        if(Pass.infoList.getPlayerInfo(p).kill.isEmpty()){
            return true;
        }
        p.sendMessage("还需击杀："+Pass.infoList.getPlayerInfo(p).kill.toString());
        return false;
    }

    @Override
    public void Finish(Player p) {
        Pass.infoList.list.get(p.getName().toLowerCase()).addReward(this.reward);
        Pass.infoList.addPlayerLevel(p.getName());
        p.sendTitle("任务完成！","当前等级："+ Pass.infoList.getPlayerLevel(p.getName())+" 已将奖励存储到奖励箱");
    }
}
