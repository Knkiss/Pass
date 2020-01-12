package knkiss.cn.config;

import knkiss.cn.Pass;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigListener implements Listener {
    ItemStack logo;
    static List<ItemStack> submit = null;
    static List<String> location = null;
    static List<ItemStack> reward = null;

    @EventHandler
    public void onCreateTask(InventoryClickEvent e) {
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        String title = e.getInventory().getTitle();
        //选择任务图标
        if (title.equals("选择任务图标")) {
            if (e.getInventory().getItem(e.getRawSlot()) != null) {
                logo = e.getInventory().getItem(e.getRawSlot());
                Pass.Face.mainInv(p);
            }
        }

        if (title.equals("选择任务类型")) {
            //选择任务类型
            int temp;
            temp = e.getRawSlot();
            if (temp == 0 || temp == 1) {
                submit.clear();
                reward.clear();
                Pass.Face.collectOrcraftInv(p);
            }
            if (temp == 2) Pass.Face.killInv(p);
            if (temp == 3) {
                location.clear();
                reward.clear();
                Pass.Face.locationInv(p);
            }
        }

        if (title.equals("添加需求物品") || title.equals("设置需求物品数量")) {
            ItemStack item = null;

            if(title.equals("添加需求物品")) {
                if(submit.size() <= 45 && e.getRawSlot() > 53 && e.getRawSlot() < 90) {
                    item = e.getInventory().getItem(e.getRawSlot());
                    Pass.Face.setSubmitAmountInv(p);
                }
                if(e.getRawSlot() == 53) Pass.Face.rewardInv(p);
            }

            if(title.equals("设置需求物品数量")) {
                assert false;
                item.setAmount(1);
                if (e.getRawSlot() == 0) item.setAmount(item.getAmount() + 1);
                if (e.getRawSlot() == 1) item.setAmount(item.getAmount() + 5);
                if (e.getRawSlot() == 2) item.setAmount(item.getAmount() + 10);
                if (e.getRawSlot() == 3) item.setAmount(item.getAmount() + 50);
                if (e.getRawSlot() == 4) item.setAmount(item.getAmount() + 100);
                if (e.getRawSlot() == 9 && item.getAmount() - 1 >= 1) item.setAmount(item.getAmount() - 1);
                if (e.getRawSlot() == 10 && item.getAmount() - 5 >= 1) item.setAmount(item.getAmount() - 5);
                if (e.getRawSlot() == 11 && item.getAmount() - 10 >= 1) item.setAmount(item.getAmount() - 10);
                if (e.getRawSlot() == 12 && item.getAmount() - 50 >= 1) item.setAmount(item.getAmount() - 50);
                if (e.getRawSlot() == 13 && item.getAmount() - 100 >= 1) item.setAmount(item.getAmount() - 100);
                if (e.getRawSlot() == 17) {
                    submit.add(item);
                    Pass.Face.collectOrcraftInv(p);
                }
            }
        }

        if (title.equals("添加奖励物品") || title.equals("设置奖励物品数量")){
            ItemStack item = null;

            if (reward.size() <= 45 && e.getRawSlot() > 53 && e.getRawSlot() < 90) {
                item = e.getInventory().getItem(e.getRawSlot());
                Pass.Face.setRewardAmountInv(p);
            }
            if(e.getRawSlot() == 53) Pass.Face.rewardInv(p);

            if(title.equals("设置奖励物品数量")) {
                assert item != null;
                item.setAmount(1);
                if (e.getRawSlot() == 0) item.setAmount(item.getAmount() + 1);
                if (e.getRawSlot() == 1) item.setAmount(item.getAmount() + 5);
                if (e.getRawSlot() == 2) item.setAmount(item.getAmount() + 10);
                if (e.getRawSlot() == 3) item.setAmount(item.getAmount() + 50);
                if (e.getRawSlot() == 4) item.setAmount(item.getAmount() + 100);
                if (e.getRawSlot() == 9 && item.getAmount() - 1 >= 1) item.setAmount(item.getAmount() - 1);
                if (e.getRawSlot() == 10 && item.getAmount() - 5 >= 1) item.setAmount(item.getAmount() - 5);
                if (e.getRawSlot() == 11 && item.getAmount() - 10 >= 1) item.setAmount(item.getAmount() - 10);
                if (e.getRawSlot() == 12 && item.getAmount() - 50 >= 1) item.setAmount(item.getAmount() - 50);
                if (e.getRawSlot() == 13 && item.getAmount() - 100 >= 1) item.setAmount(item.getAmount() - 100);
                if (e.getRawSlot() == 17) {
                    reward.add(item);
                    Pass.Face.collectOrcraftInv(p);
                }
            }
        }

        if(title.equals("添加标记坐标")) {
            //这里我不会写交给白总了
            //将玩家所在地点的坐标变为string格式传到location 的list 中
            if(e.getRawSlot() == 49);
        }












    }


}