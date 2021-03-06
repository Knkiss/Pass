package knkiss.cn.task;

import knkiss.cn.Pass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class collectTask extends Task {
    public List<ItemStack> submit = new ArrayList<>();

    public collectTask(String path) {
        super(path);
        try{
            List<String> submit_item = Pass.config.getStringList(path+".submit");
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
        }catch (Exception e){
            this.enable = false;
            Pass.log.warning("路径为"+path+"的任务扩展内容有误");
        }
    }

    @Override
    public boolean canFinish(Player p) {
        if(!this.enable){
            p.sendMessage("下个任务还未开启，请等待任务开启");
            return false;
        }

        boolean canFinish = true;
        for (ItemStack item : this.submit) {
            int n = item.getAmount();
            for (int i = 0; i < 36 && n != 0; i++) {
                if (p.getInventory().getItem(i) == null) continue;
                if (p.getInventory().getItem(i).getTypeId() == (item.getTypeId())) {
                    if (p.getInventory().getItem(i).getAmount() >= n) {
                        n = 0;
                        break;
                    }else{
                        n = n - p.getInventory().getItem(i).getAmount();
                    }
                }
            }
            if (n != 0) canFinish = false;
        }
        if(!canFinish) {
            p.sendMessage("未收集齐任务需要物品，无法完成任务");
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void Finish(Player p) {
        for (ItemStack item : this.submit) {
            int n = item.getAmount();
            for (int i = 0; i < 36 && n != 0; i++) {
                if(p.getInventory().getItem(i) == null) continue;
                ItemStack is = p.getInventory().getItem(i);
                if (is.getTypeId() != item.getTypeId() || is.getDurability() != item.getDurability()) continue;
                int amounts = is.getAmount();
                if (amounts >= n) {
                    p.getInventory().getItem(i).setAmount(amounts - n);
                    break;
                }else{
                    n = n - amounts;
                    p.getInventory().getItem(i).setAmount(0);
                }
            }
        }
        Pass.infoList.list.get(p.getName().toLowerCase()).addReward(this.reward);
        Pass.infoList.addPlayerLevel(p.getName());
        p.sendTitle("任务完成！","当前等级："+ Pass.infoList.getPlayerLevel(p.getName())+" 已将奖励存储到奖励箱");
    }

    @Override
    public void sendToPlayer(CommandSender sender) {
        super.sendToPlayerSuper(sender);
        sender.sendMessage("Submit: " + submit.toString());
    }
}
