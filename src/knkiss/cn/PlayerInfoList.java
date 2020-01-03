package knkiss.cn;

import knkiss.cn.task.craftTask;
import knkiss.cn.task.task;
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
                if(Pass.taskList.list.containsKey(String.valueOf(level))){
                        if(Pass.taskList.getTask(level).type.equalsIgnoreCase("craft")){
                                list.get(name).craft = ((craftTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).craft;
                                Pass.config.set("player."+name+".craft",list.get(name).craft);
                        }
                }
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

        public void check(CommandSender sender){
                list.forEach((name,pinfo)->{
                        sender.sendMessage("-------------playerInfoList-------------");
                        sender.sendMessage("name:" + name + "  level:" + pinfo.level);
                        sender.sendMessage("reward:" + pinfo.reward.toString());
                        sender.sendMessage("craft:" + pinfo.craft.toString());
                });
        }

        public static void showTask(Player p, int page) {
                HashMap<String, task> task = Pass.taskList.list;
                Inventory inv = Bukkit.createInventory(p, 6 * 9, "TaskList");
                ItemStack Locked = util.newItem(Material.STAINED_GLASS_PANE, 14, "Locked");
                ItemStack Doing = util.newItem(Material.STAINED_GLASS_PANE, 4, "Doing");
                ItemStack Passed = util.newItem(Material.STAINED_GLASS_PANE, 5, "Passed");
                ItemStack Next = util.newItem(Material.PAPER, 0, "前往下一页",String.valueOf(page+1));
                ItemStack Last = util.newItem(Material.PAPER, 0, "返回上一页",String.valueOf(page-1));
                int level = Pass.infoList.getPlayerLevel(p.getName()); //36
                int stateLine = ((level - 1) / 9 + 1); //4
                int maxLine = (task.size() - page * 36) / 9 + 1;
                //page = 0
                int i = 0, j = 0, l = 0;
                p.sendMessage("" + stateLine + maxLine);
                if(stateLine > 4 + page * 4){
                        for(int k = 36; k < 45; k++) inv.setItem(k, Passed);
                }
                if(stateLine <= page * 4){
                        for(int k = 0; k < 9; k++) inv.setItem(k, Locked);
                        i = 1;
                }
                while (i <= maxLine && i < 5) {
                        if (page * 4 + i == stateLine) {
                                int n = (level % 9) - 1;
                                if(level % 9 == 0 && level != 0){
                                        for (int k = 0; k < 8; k++) {
                                                inv.setItem((stateLine - page * 4) * 9 + k, Passed);
                                        }
                                        if(inv.getItem((stateLine - page * 4) * 9 + 8) == null)
                                                inv.setItem((stateLine - page * 4) * 9 + 8, Doing);
                                }
                                else{
                                        for (int k = 0; k < n; k++) {
                                                inv.setItem((stateLine - page * 4) * 9 + k, Passed);
                                        }
                                        for (int k = 8; k > n; k--) {
                                                if (((l - 1) * 9 + k + 1) + page * 36 <= task.size()) {
                                                        inv.setItem((stateLine - page * 4) * 9 + k, Locked);
                                                }
                                        }
                                        if(inv.getItem((stateLine - page * 4) * 9 + n) == null)
                                                inv.setItem((stateLine - page * 4) * 9 + n, Doing);
                                }
                        }
                        if(l == 4) break;
                        if(page * 4 + i != stateLine) {
                                for (j = 0; j < 9 && page * 36 + (j + 9 * l) + 1 <= task.size(); j++) {
                                        p.sendMessage("task");
                                        ItemStack logo = task.get(String.valueOf(page * 36 + l * 9 + j + 1)).logo;
                                        inv.setItem(i * 9 + j, logo);
                                        p.sendMessage("task__");
                                }
                                l++;
                        }
                        p.sendMessage("i++" + i);
                        i++;
                }
                if(page != 0) inv.setItem(45, Last);
                if(inv.getItem(35) != null) inv.setItem(53, Next);
                p.openInventory(inv);
        }

        public void showTask(Player p){
                showTask(p,0);
        }

        public void showReward(Player p){
                showReward(p,1);
        }

        public void showReward(Player p,int page){
                List<ItemStack> item = Pass.infoList.list.get(p.getName().toLowerCase()).reward;
                int maxpage = (item.size()-1)/45+1;
                if(page > maxpage || page < 1) page = 1;
                //0~44 45~89
                // 1     2
                //i + ((page-1)*45)
                Inventory inv = Bukkit.createInventory(p,6 * 9, "RewardList");
                for(int i=0;i<45;i++){
                        int index = i + ((page-1)*45);
                        if(index <= item.size()-1){
                                inv.setItem(i,item.get(index));
                        }
                }
                if(page > 1){
                        inv.setItem(45,util.newItem(Material.PAPER,0,"返回上一页",String.valueOf(page - 1)));
                }
                if(page < maxpage){
                        inv.setItem(53,util.newItem(Material.PAPER,0,"前往下一页",String.valueOf(page + 1)));
                }
                p.openInventory(inv);
        }

        public PlayerInfo getPlayerInfo(Player p){
                return list.get(p.getName().toLowerCase());
        }
}
