package knkiss.cn;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class Task {
    String path;

    ItemStack logo;
    int level;
    String type;
    boolean enable = true;

    //collect 收集类任务
    List<ItemStack> submit = new ArrayList<>();
    List<ItemStack> reward = new ArrayList<>();

    @SuppressWarnings("deprecation")
    Task(String path){
        //暂时完成

        this.path = path;
        this.level = Pass.config.getInt(path +".level");
        this.type = Pass.config.getString(path +".type");

        String item = Pass.config.getString(path +".info.item");

        String pattern = "(.*)-(.*)-(.*)";
        Pattern r = Pattern.compile(pattern);
        Matcher m1 = r.matcher(item);
        if(m1.find()){
            int ID = Integer.parseInt(m1.group(1));
            int Durability = Integer.parseInt(m1.group(2));
            int amount = Integer.parseInt(m1.group(3));
            String name = Pass.config.getString(path +".info.name");
            List<String> lore = Pass.config.getStringList(path +".info.lore");
            this.logo = util.newItem(ID,amount,Durability,name,lore);

        }else{
            this.type = "disable";
        }


        if(type.equalsIgnoreCase("collect")){
            List<String> submit_item = Pass.config.getStringList(path+".submit");
            List<String> reward_item = Pass.config.getStringList(path+".reward");

            submit_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int ID = Integer.parseInt(m.group(1));
                    int Durability = Integer.parseInt(m.group(2));
                    int amount = Integer.parseInt(m.group(3));
                    submit.add(new ItemStack(ID,amount,(short)Durability));
                }
            });

            reward_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int ID = Integer.parseInt(m.group(1));
                    int Durability = Integer.parseInt(m.group(2));
                    int amount = Integer.parseInt(m.group(3));
                    reward.add(new ItemStack(ID,amount,(short)Durability));
                }
            });
        }else{
            this.enable = false;
            Pass.log.warning("路径为"+path+"的任务类型有误");
        }
    }


}
