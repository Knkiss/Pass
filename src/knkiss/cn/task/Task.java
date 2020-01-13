package knkiss.cn.task;

import knkiss.cn.Pass;
import knkiss.cn.util.Messages;
import knkiss.cn.util.Utils;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public abstract class Task {
    public String path;
    public int level;
    public String type;
    public ItemStack logo;
    public boolean enable = true;
    public List<ItemStack> reward = new ArrayList<>();
    public List<String> command = new ArrayList<>();

    public Task(String path){
        try{
            this.path = path;
            this.level = Pass.taskConfig.getInt(path +".level");
            this.type = Pass.taskConfig.getString(path +".type");

            String item = Pass.taskConfig.getString(path +".info.item");

            String pattern = "(.*)-(.*)-(.*)";
            Pattern r = Pattern.compile(pattern);
            Matcher m1 = r.matcher(item);
            if(m1.find()){
                int ID = Integer.parseInt(m1.group(1));
                int Durability = Integer.parseInt(m1.group(2));
                int amount = Integer.parseInt(m1.group(3));
                String name = Pass.taskConfig.getString(path +".info.name");
                List<String> lore = Pass.taskConfig.getStringList(path +".info.lore");
                this.logo = Utils.newItem(ID,amount,Durability,name,lore);
            }else{
                this.logo = Utils.newItem(Material.BOOK,0,"图标错误的任务");
            }

            List<String> reward_item = Pass.taskConfig.getStringList(path+".reward");
            reward_item.forEach(str->{
                Matcher m = r.matcher(str);
                if(m.find()){
                    int ID = Integer.parseInt(m.group(1));
                    int Durability = Integer.parseInt(m.group(2));
                    int amount = Integer.parseInt(m.group(3));
                    this.reward.add(new ItemStack(ID,amount,(short)Durability));
                }
            });

            pattern = "/(.*)";
            Pattern r1 = Pattern.compile(pattern);
            reward_item.forEach(str->{
                Matcher m = r1.matcher(str);
                if(m.find()){
                    this.command.add(m.group(1));
                }
            });
        }catch (Exception e){
            Pass.pass.getLogger().warning("路径为"+path+"的任务基础内容有误");
            this.enable = false;
        }
    }

    public abstract boolean canFinish(Player p);
    public abstract void Finish(Player p);
    public abstract void sendToPlayer(CommandSender sender);

    public void FinishSuper(Player p){
        Pass.infoList.list.get(p.getName().toLowerCase()).addReward(this.reward);
        this.command.forEach(command-> Utils.runCommandOp(p,command.replace("%p",p.getName())));
        Pass.infoList.addPlayerLevel(p.getName());
        Messages.taskFinishTitle(p,Pass.infoList.getPlayerLevel(p.getName()));
    }

    public void sendToPlayerSuper(CommandSender sender){
        sender.sendMessage("-------------TaskList-------------------");
        sender.sendMessage("level:" + level+"  type:" + type);
        sender.sendMessage("name:" + logo.getItemMeta().getDisplayName());
        sender.sendMessage("lore:" + logo.getItemMeta().getLore().toString());
        sender.sendMessage("logo: ID:" + logo.getTypeId() +" Damage:"+logo.getDurability()+" Amount:"+logo.getAmount());
        sender.sendMessage("Enable:" + enable);
    }
}
