package knkiss.cn;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class PlayerInfoList {
        public static int maxLevel = 100;
        public HashMap<String, PlayerInfo> list = new HashMap<>();

        PlayerInfoList(){
                maxLevel = Pass.config.getInt("settings.maxlevel");
        }

        public void addPlayer(String name){
                name = name.toLowerCase();
                if(!Pass.config.contains("player."+name+".level")){
                        Pass.config.set("player."+name+".level", 1);
                }
                list.put(name,new PlayerInfo(name));
        }

        public int getPlayerLevel(String name){
                name = name.toLowerCase();
                return list.get(name).level;
        }

        public boolean addPlayerLevel(String name){
                name = name.toLowerCase();
                int level = list.get(name).level + 1;
                if (level == maxLevel) level = 1;
                list.get(name).level = level;
                list.get(name).updateConfig();
                if (level == 1) return true;
                else return false;
        }

        public boolean setPlayerLevel(String name,int level){
                name = name.toLowerCase();
                if (level >= maxLevel) level = 1;
                list.get(name).level = level;
                list.get(name).updateConfig();
                if (level == 1) return true;
                else return false;
        }

        public boolean canFinish(Player p){
                if (Pass.taskList.amount < Pass.infoList.getPlayerLevel(p.getName())){
                        p.sendMessage("您已到达当前任务等级上限");
                        return false;
                }

                List<ItemStack> submit = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).submit;
                List<ItemStack> reward = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward;
                int tr = 1, ts = 1;
                ItemStack item;
                int n;
                for(int j = 0; j < reward.size(); j++) {
                        item = reward.get(j);
                        n = item.getAmount();
                        int space = 0;
                        for (int i = 0; i < 36 && n != 0; i++) {
                                if (p.getInventory().getItem(i) == null) space += 64;
                                else if (p.getInventory().getItem(i).getTypeId() == item.getTypeId())
                                        space += (64 - p.getInventory().getItem(i).getAmount());
                        }
                        if(space < item.getAmount()) tr = 0;
                }
                for(int j = 0; j < submit.size(); j++) {
                        item = submit.get(j);
                        n = item.getAmount();
                        for (int i = 0; i < 36 && n != 0; i++) {
                                if (p.getInventory().getItem(i) != null) {
                                        if (p.getInventory().getItem(i).getTypeId() == (item.getTypeId())) {
                                                if (p.getInventory().getItem(i).getAmount() >= n) {
                                                        n = 0;
                                                        continue;
                                                }
                                                if (p.getInventory().getItem(i).getAmount() < n) {
                                                        n = n - p.getInventory().getItem(i).getAmount();
                                                        continue;
                                                }
                                        }
                                }
                        }
                        if (n != 0) ts = 0;
                }
                if(ts == 0) {
                        p.sendMessage("不满足任务条件，无法完成任务");
                        return false;
                }else if(tr == 0){
                        p.sendMessage("背包空间不足，请清理后重试");
                        return false;
                }else{
                        p.sendMessage("任务完成！");
                        p.sendMessage("你的任务等级为："+Pass.infoList.getPlayerLevel(p.getName()));
                        return true;
                }
        }

        public void Finish(Player p){
                List<ItemStack> submit = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).submit;
                List<ItemStack> reward = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward;
                for(int j = 0; j < submit.size(); j++) {
                        ItemStack item = submit.get(j);
                        int n = item.getAmount();
                        for (int i = 0; i < 36 && n != 0; i++) {
                                if (p.getInventory().getItem(i) != null) {
                                        if (p.getInventory().getItem(i).getTypeId() == item.getTypeId()
                                                &&  p.getInventory().getItem(i).getDurability() == item.getDurability()) {
                                                if (p.getInventory().getItem(i).getAmount() >= n) {
                                                        p.getInventory().getItem(i).setAmount(p.getInventory().getItem(i).getAmount() - n);
                                                        n = 0;
                                                        continue;
                                                }
                                                if (p.getInventory().getItem(i).getAmount() < n) {
                                                        n = n - p.getInventory().getItem(i).getAmount();
                                                        p.getInventory().getItem(i).setAmount(0);
                                                        continue;
                                                }
                                        }
                                }
                        }
                }
                for(int i = 0; i < reward.size(); i++)
                        p.getInventory().addItem(reward.get(i));
                Pass.infoList.addPlayerLevel(p.getName());
        }

        public void check(CommandSender sender){
                list.forEach((name,pinfo)->{
                        sender.sendMessage("-------------playerInfoList-------------");
                        sender.sendMessage("name:" + name + "  level:" + pinfo.level);
                });
        }

        public void showTask(Player p)
        {
                HashMap<String,Task> task = Pass.taskList.list;
                Inventory inv = Bukkit.createInventory(p,6 * 9, "TaskList");
                ItemStack Locked = util.newItem(org.bukkit.Material.STAINED_GLASS_PANE, 14, "Locked");
                ItemStack Doing = util.newItem(org.bukkit.Material.STAINED_GLASS_PANE, 4, "Doing");
                ItemStack Passed = util.newItem(org.bukkit.Material.STAINED_GLASS_PANE, 5, "Passed");
                int level = Pass.infoList.getPlayerLevel(p.getName());
                int stateLine = (level - 1) / 9 + 1;
                int maxLine = task.size() / 9 + 1;
                int i = 0, j = 0, l = 0;
                p.sendMessage(""+stateLine+maxLine);
                while(i <= maxLine && i < 6) {
                        if (i == stateLine) {
                                for (int k = 0; k < (level % 9) - 1; k++) {
                                        inv.setItem(stateLine * 9 + k, Passed);
                                }
                                inv.setItem(stateLine * 9 + (level % 9) - 1, Doing);
                                for (int k = 8; k > (level % 9) - 1 ; k--) {
                                        if(((l - 1) * 9 + k + 1) <= task.size()){
                                                inv.setItem(stateLine * 9 + k, Locked);
                                        }

                                }
                        }
                        else{
                                for(j = 0; j < 9 && (j + 9 * l) + 1 <= task.size(); j++){
                                        p.sendMessage("task");
                                        ItemStack logo = task.get(String.valueOf(l * 9 + j + 1)).logo;
                                        inv.setItem(i * 9 + j, logo);
                                        p.sendMessage("task__");
                                }
                                l++;
                        }
                        p.sendMessage("i++"+i);
                        i++;
                }
                p.openInventory(inv);
        }
}
