package knkiss.cn.task;

import knkiss.cn.Pass;
import knkiss.cn.util;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public abstract class task {
    public String path;
    public int level;
    public String type;
    public ItemStack logo;
    public boolean enable = true;
    public List<ItemStack> reward = new ArrayList<>();

    public task(String path){
        try{
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
                this.logo = util.newItem(Material.BOOK,0,"图标错误的任务");
            }

            List<String> reward_item = Pass.config.getStringList(path+".reward");
            reward_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int ID = Integer.parseInt(m.group(1));
                    int Durability = Integer.parseInt(m.group(2));
                    int amount = Integer.parseInt(m.group(3));
                    this.reward.add(new ItemStack(ID,amount,(short)Durability));
                }
            });
        }catch (Exception e){
            Pass.log.warning("路径为"+path+"的任务基础内容有误");
            this.enable = false;
        }
    }

    public abstract boolean canFinish(Player p);
    public abstract void Finish(Player p);
    public abstract void sendToPlayer(CommandSender sender);
    public void sendToPlayerSuper(CommandSender sender){
        sender.sendMessage("-------------TaskList-------------------");
        sender.sendMessage("level:" + level+"  type:" + type);
        sender.sendMessage("name:" + logo.getItemMeta().getDisplayName());
        sender.sendMessage("lore:" + logo.getItemMeta().getLore().toString());
        sender.sendMessage("logo: ID:" + logo.getTypeId() +" Damage:"+logo.getDurability()+" Amount:"+logo.getAmount());
        sender.sendMessage("Enable:" + enable);
    }
}
