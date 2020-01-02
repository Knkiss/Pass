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
                        int ts = 1;
                        for (ItemStack item : task.submit) {
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
                                if (n != 0) ts = 0;
                        }
                        if(ts == 0) {
                                p.sendMessage("不满足任务条件，无法完成任务");
                                return false;
                        }else {
                                return true;
                        }
                }else if (task.type.equalsIgnoreCase("break")){

                }
                return false;
        }

        public void Finish(Player p){
                for (ItemStack item : Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).submit) {
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
                list.get(p.getName().toLowerCase()).addReward(Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward);
                Pass.infoList.addPlayerLevel(p.getName());
                p.sendMessage("任务完成，你的当前等级为："+Pass.infoList.getPlayerLevel(p.getName()));
                p.sendMessage("已将奖励存储到奖励箱,/pass reward查看");
                Pass.infoList.showTask(p);
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
                        inv.setItem(45,util.newItem(Material.PAPER,0,"返回上一页",String.valueOf(page + 1)));
                }
                p.openInventory(inv);
        }

        public PlayerInfo getPlayerInfo(Player p){
                return list.get(p.getName().toLowerCase());
        }
}
