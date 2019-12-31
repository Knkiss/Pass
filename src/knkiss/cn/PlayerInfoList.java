package knkiss.cn;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
public class PlayerInfoList {
        static int maxLevel = 100;

        private static HashMap<String, PlayerInfo> list = new HashMap<>();

        PlayerInfoList(){
                maxLevel = Pass.config.getInt("settings.maxlevel");
        }

        public static void addPlayer(String name){
                name = name.toLowerCase();
                if(!Pass.config.contains("player."+name+".level")){
                        Pass.config.set("player."+name+".level", 1);
                }
                list.put(name,new PlayerInfo(name));
        }

        public static int getPlayerLevel(String name){
                name = name.toLowerCase();
                return list.get(name).level;
        }

        public static boolean addPlayerLevel(String name){
                name = name.toLowerCase();
                int level = list.get(name).level + 1;
                if (level == maxLevel){
                        level = 1;
                        list.get(name).level = level;
                        return true;
                }else{
                        list.get(name).level = level;
                        return false;
                }
        }

        public static boolean canFinish(Player p){
                List<ItemStack> submit = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).submit;
                List<ItemStack> reward = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward;
                int s = submit.size(), r = reward.size();
                int tr = 1, ts = 1;
                ItemStack item;
                int n;
                for(int j = 0; j < s; j++) {
                        item = submit.get(j);
                        n = item.getAmount();
                        int space = 0;
                        for (int i = 0; i < 36 && n != 0; i++) {
                                if (p.getInventory().getItem(i) == null) space += 64;
                                else if (p.getInventory().getItem(i).getTypeId() == item.getTypeId())
                                        space += (64 - p.getInventory().getItem(i).getAmount());
                        }
                        if(space < item.getAmount()) tr = 0;
                }
                for(int j = 0; j < s; j++) {
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
                if(ts == 1 && tr == 1) return true;
                else return false;
        }

        public static void Finish(Player p){
                List<ItemStack> submit = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).submit;
                List<ItemStack> reward = Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).reward;
                int s = submit.size();
                for(int j = 0; j < s; j++) {
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
}
