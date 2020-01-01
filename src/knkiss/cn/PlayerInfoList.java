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

        public void addPlayerLevel(String name){
                name = name.toLowerCase();
                int level = list.get(name).level + 1;
                if (level == maxLevel) level = 1;
                list.get(name).level = level;
                list.get(name).updateConfig();
        }

        public boolean setPlayerLevel(String name,int level){
                name = name.toLowerCase();
                if (level >= maxLevel || level <= 0) level = 1;
                if (list.containsKey(name)){
                        list.get(name).level = level;
                        list.get(name).updateConfig();
                }else{
                        if(Pass.config.contains("player."+name+".level")){
                                Pass.config.set("player."+name+".level",level);
                        }else{
                                return false;
                        }
                }
                return true;
        }

        public boolean canFinish(Player p){
                if (TaskList.amount < Pass.infoList.getPlayerLevel(p.getName())){
                        p.sendMessage("您已到达当前任务等级上限");
                        return false;
                }
                Task task = Pass.taskList.list.get(String.valueOf(Pass.infoList.getPlayerLevel(p.getName())));
                if (!task.enable){
                        p.sendMessage("下个任务还未开启，请等待任务开启");
                        return false;
                }

                if (task.type.equalsIgnoreCase("collect")){
                        List<ItemStack> submit = task.submit;
                        int ts = 1;
                        ItemStack item;
                        int n;
                        for (ItemStack itemStack : submit) {
                                item = itemStack;
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
                                                        }
                                                }
                                        }
                                }
                                if (n != 0) ts = 0;
                        }
                        if(ts == 0) {
                                p.sendMessage("不满足任务条件，无法完成任务");
                                return false;
                        }else {
                                p.sendMessage("任务完成！");
                                p.sendMessage("你的任务等级为："+Pass.infoList.getPlayerLevel(p.getName()));
                                return true;
                        }
                }else if (task.type.equalsIgnoreCase("break")){

                }
                return false;
        }

        public void Finish(Player p){
                List<ItemStack> submit = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).submit;
                for (ItemStack item : submit) {
                        int n = item.getAmount();
                        for (int i = 0; i < 36 && n != 0; i++) {
                                if (p.getInventory().getItem(i) != null) {
                                        ItemStack is = p.getInventory().getItem(i);
                                        if (is.getTypeId() == item.getTypeId() && is.getDurability() == item.getDurability()) {
                                                if (is.getAmount() >= n) {
                                                        p.getInventory().getItem(i).setAmount(is.getAmount() - n);
                                                        n = 0;
                                                        continue;
                                                }
                                                if (is.getAmount() < n) {
                                                        n = n - is.getAmount();
                                                        p.getInventory().getItem(i).setAmount(0);
                                                }
                                        }
                                }
                        }
                }
                List<ItemStack> reward = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward;
                list.get(p.getName().toLowerCase()).addReward(reward);
                Pass.infoList.addPlayerLevel(p.getName());
        }

        public void check(CommandSender sender){
                list.forEach((name,pinfo)->{
                        sender.sendMessage("-------------playerInfoList-------------");
                        sender.sendMessage("name:" + name + "  level:" + pinfo.level);
                        sender.sendMessage("reward:" + pinfo.reward.toString());
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
                int i = 0, j, l = 0;
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
                                        if(task.get(String.valueOf(l * 9 + j + 1)).enable){
                                                ItemStack logo = task.get(String.valueOf(l * 9 + j + 1)).logo;
                                                inv.setItem(i * 9 + j, logo);
                                        }else{
                                                ItemStack logo = util.newItem(347,0,"任务未开启，请等待开启");
                                                inv.setItem(i * 9 + j, logo);
                                        }
                                }
                                l++;
                        }
                        i++;
                }
                p.openInventory(inv);
        }
}
