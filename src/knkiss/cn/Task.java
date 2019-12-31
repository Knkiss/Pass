package knkiss.cn;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    String path;
    String name;
    String lore;
    int level;
    String type;
    boolean enable = true;

    //collection 收集类任务
    List<ItemStack> submit = new ArrayList<>();
    List<ItemStack> reward = new ArrayList<>();

    @SuppressWarnings("deprecation")
    Task(String path){
        this.path = path;
        this.level = Pass.config.getInt(path +".level");
        this.type = Pass.config.getString(path +".type");
        this.name = Pass.config.getString(path +".name");
        this.lore = Pass.config.getString(path +".lore");

        if(type.equalsIgnoreCase("collection")){
            List<String> submit_item = Pass.config.getStringList(path+".submit");
            List<String> reward_item = Pass.config.getStringList(path+".reward");

            String pattern = "(.*)-(.*)-(.*)";
            Pattern r = Pattern.compile(pattern);

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
            Pass.log.info("警告：名为"+path+"的任务类型有误");
        }
    }


}
