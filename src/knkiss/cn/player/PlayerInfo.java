package knkiss.cn.player;

import knkiss.cn.Pass;
import knkiss.cn.task.CraftTask;
import knkiss.cn.task.KillTask;
import knkiss.cn.task.LocationTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
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
    public List<EntityType> kill = new ArrayList<>();
    public List<Location> location = new ArrayList<>();

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

        if(Pass.taskList.getTask(level).type.equalsIgnoreCase("craft")){
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
                this.craft = ((CraftTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).craft;
            }
        }else if(Pass.taskList.getTask(level).type.equalsIgnoreCase("kill")){
            if(Pass.config.contains(path+".kill")){
                for(String entity:Pass.config.getStringList(path+".kill")){
                    String pattern = "(.*)-(.*)";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m1 = r.matcher(entity);
                    if(m1.find()){
                        int typeID = Integer.parseInt(m1.group(1));
                        int number = Integer.parseInt(m1.group(2));
                        for(int i=0;i<number;i++){
                            kill.add(EntityType.fromId(typeID));
                        }
                    }
                }
            }else{
                this.kill = ((KillTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).kill;
            }
        }else if(Pass.taskList.getTask(level).type.equalsIgnoreCase("location")){
            if(Pass.config.contains(path+".location")){
                for(String locate:Pass.config.getStringList(path+".location")){
                    String pattern = "(.*)-(.*)-(.*)-(.*)";
                    Pattern r = Pattern.compile(pattern);
                    Matcher m1 = r.matcher(locate);
                    if(m1.find()){
                        int x = Integer.parseInt(m1.group(1));
                        int y = Integer.parseInt(m1.group(2));
                        int z = Integer.parseInt(m1.group(3));
                        String world = m1.group(4);
                        if(Bukkit.getWorld(world) == null){
                            location.add(new Location(Bukkit.getWorlds().get(0),x,y,z));
                        }else{
                            location.add(new Location(Bukkit.getWorld(world),x,y,z));
                        }
                    }
                }
            }else{
                this.location = ((LocationTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).location;
            }
        }
        updateConfig();
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
        craft.forEach(item-> craftStr.add(item.getTypeId()+"-"+item.getDurability()+"-"+item.getAmount()));
        Pass.config.set(path+".craft",craftStr);


        HashMap<Integer,Integer> killList=new HashMap<>();
        List<String> killStr = new ArrayList<>();
        kill.forEach(entity->{
            if(killList.containsKey((int)entity.getTypeId())){
                killList.put((int) entity.getTypeId(),killList.get((int)entity.getTypeId())+1);
            }else{
                killList.put((int) entity.getTypeId(),1);
            }
        });
        killList.forEach((typeID,number)-> killStr.add(typeID+"-"+number));
        Pass.config.set(path+".kill",killStr);


        List<String> locationStr = new ArrayList<>();
        location.forEach(locate-> locationStr.add((int)locate.getX()+"-"+(int)locate.getY()+"-"+(int)locate.getZ()+"-"+locate.getWorld().getName()));
        Pass.config.set(path+".location",locationStr);
    }
}
