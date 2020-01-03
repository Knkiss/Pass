package knkiss.cn.Listener;

import knkiss.cn.Pass;
import knkiss.cn.PlayerInfo;
import knkiss.cn.task.craftTask;
import knkiss.cn.util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class InfoListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        Pass.infoList.addPlayer(e.getPlayer().getName());
    }

    @EventHandler
    public void onCraftItem(InventoryClickEvent e){
        if(!e.getSlotType().equals(InventoryType.SlotType.RESULT)) return;
        if(e.getAction().equals(InventoryAction.NOTHING))return;
        if(!util.isCraftAction(e.getAction()))return;
        if(e.getCurrentItem()==null)return;
        Player p = (Player) e.getWhoClicked();
        if(!Pass.taskList.list.containsKey(String.valueOf(Pass.infoList.getPlayerLevel(p.getName()))))return;
        if(!Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).type.equalsIgnoreCase("craft"))return;
        List<ItemStack> now = Pass.infoList.getPlayerInfo(p).craft;
        if(now.isEmpty()){
            if(Pass.taskList.canFinish(p)){
                Pass.taskList.Finish(p);
            }
            return;
        }
        List<ItemStack> last = new ArrayList<>();
        now.forEach(item->{
            int amount = item.getAmount();
            if(e.getCurrentItem().getTypeId() == item.getTypeId() && e.getCurrentItem().getDurability() == item.getDurability()){
                if(amount > e.getCurrentItem().getAmount()) {
                    last.add(new ItemStack(item.getTypeId(), amount - e.getCurrentItem().getAmount(), item.getDurability()));
                }
            }else{
                last.add(item);
            }
        });
        Pass.infoList.getPlayerInfo(p).craft = last;
        Pass.infoList.getPlayerInfo(p).updateConfig();
        if(last.isEmpty()){
            if(Pass.taskList.canFinish(p)){
                Pass.taskList.Finish(p);
            }
        }
    }
}
