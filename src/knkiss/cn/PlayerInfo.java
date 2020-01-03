package knkiss.cn;

import knkiss.cn.task.craftTask;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class PlayerInfo {
    public String path;
    public String name;
    public int level;
    public List<ItemStack> reward = new ArrayList<>();
    public List<String> rewardStr = new ArrayList<>();
    public List<ItemStack> craft = new ArrayList<>();

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
                    reward.add(new ItemStack(ID,amount,(short)Durability));
                }
            }
        }

        if(Pass.config.contains(path+".craft")){
            for(String item:Pass.config.getStringList(path+".craft")){
                String pattern = "(.*)-(.*)-(.*)";
                Pattern r = Pattern.compile(pattern);
                Matcher m1 = r.matcher(item);
                if(m1.find()){
                    int ID = Integer.parseInt(m1.group(1));
                    int Durability = Integer.parseInt(m1.group(2));
                    int amount = Integer.parseInt(m1.group(3));
                    craft.add(new ItemStack(ID,amount,(short) Durability));
                }
            }
        }else{
            if(Pass.taskList.getTask(level).type.equalsIgnoreCase("craft")){
                this.craft = ((craftTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).craft;
                Pass.config.set("player."+name+".craft",this.craft);
                updateConfig();
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
        updateConfig();
    }

    public void addReward(ItemStack item){
        reward.add(item);
        int ID = item.getTypeId();
        int Durability = item.getDurability();
        int amount = item.getAmount();
        String str = ID+"-"+Durability+"-"+amount;
        rewardStr.add(str);
        updateConfig();
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
        updateConfig();
    }

    public void updateConfig(){
        Pass.config.set(path+".level",level);
        Pass.config.set(path+".reward",rewardStr);
        List<String> craftStr = new ArrayList<>();
        craft.forEach(item->{
            craftStr.add(item.getTypeId()+"-"+item.getDurability()+"-"+item.getAmount());
        });
        Pass.config.set(path+".craft",craftStr);
    }
}
