package knkiss.cn.player;

import knkiss.cn.Pass;
import knkiss.cn.task.craftTask;
import knkiss.cn.task.killTask;
import knkiss.cn.task.locationTask;
import knkiss.cn.task.Task;
import knkiss.cn.util;
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

        public PlayerInfoList(){
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
                        }else if(Pass.taskList.getTask(level).type.equalsIgnoreCase("kill")){
                                list.get(name).kill = ((killTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).kill;
                                Pass.config.set("player."+name+".kill",list.get(name).kill);
                        }else if(Pass.taskList.getTask(level).type.equalsIgnoreCase("location")){
                                list.get(name).location = ((locationTask) Pass.taskList.getTask(Pass.infoList.getPlayerLevel(name))).location;
                                Pass.config.set("player."+name+".location",list.get(name).location);
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

        public int canBS(Player p, int n){
                if(Pass.infoList.getPlayerLevel(p.getName()) + n > Pass.taskList.list.size() + 1) return 1;
                for(int i = 0; i < n; i++){
                        if(!Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName()) + i).enable) return 2;
                }
                return 0;
        }

        public void buyLevel(Player p,int n) {
                //将在后续版本修复
                for(int i = 0; i < n; i++){
                        list.get(p.getName().toLowerCase()).addReward(Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward);
                        Pass.infoList.addPlayerLevel(p.getName());
                }
                p.sendTitle("任务完成！","当前等级："+ Pass.infoList.getPlayerLevel(p.getName())+" 已将奖励存储到奖励箱");
        }

        public void skipLevel(Player p,int n){
                //将在后续版本修复
                for(int i = 0; i < n; i++){
                        Pass.infoList.addPlayerLevel(p.getName());
                }
                p.sendTitle("已跳过 "+ n +" 个任务","当前等级："+ Pass.infoList.getPlayerLevel(p.getName()));
        }

        public void check(CommandSender sender){
                list.forEach((name,pinfo)->{
                        sender.sendMessage("-------------playerInfoList-------------");
                        sender.sendMessage("name:" + name + "  level:" + pinfo.level);
                        sender.sendMessage("reward:" + pinfo.reward.toString());
                        sender.sendMessage("craft:" + pinfo.craft.toString());
                        sender.sendMessage("kill:" + pinfo.kill.toString());
                        sender.sendMessage("location:" + pinfo.location.toString());
                });
        }

        public void showTask(Player p, int page) {
                HashMap<String, Task> task = Pass.taskList.list;
                Inventory inv = Bukkit.createInventory(p, 6 * 9, "TaskList");
                ItemStack Locked = util.newItem(Material.STAINED_GLASS_PANE, 14, "Locked");
                ItemStack Doing = util.newItem(Material.STAINED_GLASS_PANE, 4, "Doing");
                ItemStack Passed = util.newItem(Material.STAINED_GLASS_PANE, 5, "Passed");
                ItemStack Next = util.newItem(Material.PAPER, 0, "前往下一页",String.valueOf(page+1));
                ItemStack Last = util.newItem(Material.PAPER, 0, "返回上一页",String.valueOf(page-1));
                int level = Pass.infoList.getPlayerLevel(p.getName()); //36
                int stateLine = ((level - 1) / 9 + 1); //4
                int maxLine = (task.size() - page * 36) / 9 + 1;
                int i = 0, j, l = 0;
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
                                        int i1 = (stateLine - page * 4) * 9 + n;
                                        if(inv.getItem(i1) == null)
                                                inv.setItem(i1, Doing);
                                }
                        }
                        if(l == 4) break;
                        if(page * 4 + i != stateLine) {
                                for (j = 0; j < 9 && page * 36 + (j + 9 * l) + 1 <= task.size(); j++) {
                                        ItemStack logo = task.get(String.valueOf(page * 36 + l * 9 + j + 1)).logo;
                                        inv.setItem(i * 9 + j, logo);
                                }
                                l++;
                        }
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
