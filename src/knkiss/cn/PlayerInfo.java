package knkiss.cn;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class PlayerInfo {
    String path;
    String name;
    int level;
    List<ItemStack> reward = new ArrayList<>();
    List<String> rewardStr = new ArrayList<>();

    PlayerInfo(String name){
        this.path = "player."+name;
        this.name = name;
        this.level = Pass.config.getInt(path+".level");

        if(Pass.config.contains(path+".reward")){
            rewardStr = Pass.config.getStringList(path+".reward");
            for(String item:rewardStr){
                String pattern = "(.*)-(.*)-(.*)";
                Pattern r = Pattern.compile(pattern);
                Matcher m1 = r.matcher(item);
                if(m1.find()){
                    int ID = Integer.parseInt(m1.group(1));
                    int Durability = Integer.parseInt(m1.group(2));
                    int amount = Integer.parseInt(m1.group(3));
                    ItemStack logo = new ItemStack(ID,amount,(short)Durability);
                    reward.add(logo);
                }
            }
        }
    }

    public void removeReward(ItemStack item){
        reward.remove(item);
        int ID = item.getTypeId();
        int Durability = item.getDurability();
        int amount = item.getAmount();
        String str = ID+"-"+Durability+"-"+amount;
        rewardStr.remove(str);
        Pass.config.set(path+".reward",rewardStr);
    }

    public void addReward(ItemStack item){
        reward.add(item);
        int ID = item.getTypeId();
        int Durability = item.getDurability();
        int amount = item.getAmount();
        String str = ID+"-"+Durability+"-"+amount;
        rewardStr.add(str);
        Pass.config.set(path+".reward",rewardStr);
    }

    public void addReward(List<ItemStack> itemList){
        for(ItemStack item:itemList){
            reward.add(item);
            int ID = item.getTypeId();
            int Durability = item.getDurability();
            int amount = item.getAmount();
            String str = ID+"-"+Durability+"-"+amount;
            rewardStr.add(str);
        }
        Pass.config.set(path+".reward",rewardStr);
    }

    public void updateConfig(){
        Pass.config.set(path+".level",level);
    }
}
