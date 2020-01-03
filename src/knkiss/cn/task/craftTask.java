package knkiss.cn.task;

import knkiss.cn.Pass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class craftTask extends task{
    public List<ItemStack> craft = new ArrayList<>();

    public craftTask(String path) {
        super(path);
        try{
            List<String> craft_item = Pass.config.getStringList(path+".craft");
            String pattern = "(.*)-(.*)-(.*)";
            Pattern r = Pattern.compile(pattern);
            craft_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int ID = Integer.parseInt(m.group(1));
                    int Durability = Integer.parseInt(m.group(2));
                    int amount = Integer.parseInt(m.group(3));
                    craft.add(new ItemStack(ID,amount,(short)Durability));
                }
            });
        }catch (Exception e){
            this.enable = false;
            Pass.log.warning("路径为"+path+"的任务扩展内容有误");
        }
    }

    @Override
    public boolean canFinish(Player p) {
        if(Pass.infoList.getPlayerInfo(p).craft.isEmpty()){
            return true;
        }
        p.sendMessage("还需制作："+Pass.infoList.getPlayerInfo(p).craft.toString());
        return false;
    }

    @Override
    public void Finish(Player p) {
        Pass.infoList.list.get(p.getName().toLowerCase()).addReward(this.reward);
        Pass.infoList.addPlayerLevel(p.getName());
        p.sendTitle("任务完成！","当前等级："+ Pass.infoList.getPlayerLevel(p.getName())+" 已将奖励存储到奖励箱");
    }
}
