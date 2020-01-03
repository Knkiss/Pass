package knkiss.cn.Listener;

import knkiss.cn.Pass;
import knkiss.cn.PlayerInfoList;
import knkiss.cn.util;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InventoryListener implements Listener {
    @EventHandler
    public void atInventory(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equalsIgnoreCase("TaskList")) {
            e.setCancelled(true);
            if (e.getCurrentItem() == null) return;
            if (!e.getAction().equals(InventoryAction.PICKUP_ALL)) return;
            int i = e.getInventory().first(util.newItem(Material.STAINED_GLASS_PANE, 4, "Doing"));
            int index = e.getRawSlot();
            if (i == -1) {
                //已完成或未进行的任务页面
                if (index == 45 || index == 53) {//翻页
                    PlayerInfoList.showTask(p, Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(0)));
                } else {
                    p.sendMessage("此页非任务进行页");
                }
            } else {
                //正在进行的任务页面
                if ((index >= i - i % 9 && index <= i + 8 - i % 9) || e.getRawSlot() >= 54) return;
                if (index < i - 9) {
                    p.sendMessage("您已完成此任务");
                } else if (index > i - 9 && index != 45 && index != 53) {
                    p.sendMessage("任务未解锁");
                } else if (index == 45 || index == 53) {//翻页
                    PlayerInfoList.showTask(p, Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(0)));
                } else {
                    if(Pass.taskList.canFinish(p)){
                        Pass.taskList.Finish(p);
                    }
                }
            }

        }else if(e.getInventory().getTitle().equalsIgnoreCase("RewardList")){
            e.setCancelled(true);
            if(e.getCurrentItem() == null)return;

            if(e.getAction().equals(InventoryAction.PICKUP_ALL) && e.getRawSlot()<54){
                if(e.getRawSlot() == 45 || e.getRawSlot() == 53){
                    Pass.infoList.showReward(p, Integer.parseInt(e.getCurrentItem().getItemMeta().getLore().get(0)));
                    return;
                }
                Pass.infoList.getPlayerInfo(p).removeReward(e.getCurrentItem());
                p.getInventory().addItem(e.getCurrentItem()).forEach((Integer, ItemStack)->{
                    Pass.infoList.getPlayerInfo(p).addReward(ItemStack);
                    p.sendMessage("背包已满，请清空后再次领取，已重新储存"+ItemStack.toString());
                });
                Pass.infoList.showReward(p);
            }
        }
    }
}
