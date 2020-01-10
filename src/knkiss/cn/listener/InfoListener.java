package knkiss.cn.listener;

import knkiss.cn.Pass;
import knkiss.cn.Util;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
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
        if(!Util.isCraftAction(e.getAction()))return;
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

    @EventHandler
    public void onKillEntity(EntityDamageByEntityEvent e){
        if(!(e.getDamager() instanceof Player))return;
        if(!(e.getEntity() instanceof LivingEntity))return;
        if(((LivingEntity) e.getEntity()).getHealth() - e.getFinalDamage() > 0) return;

        Player p = (Player) e.getDamager();
        if(!Pass.taskList.list.containsKey(String.valueOf(Pass.infoList.getPlayerLevel(p.getName()))))return;
        if(!Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).type.equalsIgnoreCase("kill"))return;
        List<EntityType> killList = Pass.infoList.getPlayerInfo(p).kill;
        killList.remove(e.getEntityType());
        if(killList.isEmpty()){
            if(Pass.taskList.canFinish(p)){
                Pass.taskList.Finish(p);
            }
            return;
        }
        Pass.infoList.getPlayerInfo(p).kill = killList;
        Pass.infoList.getPlayerInfo(p).updateConfig();
    }

    @EventHandler
    public void onMarkLocation(PlayerInteractEvent e){
        if(e.getPlayer().getInventory().getItemInMainHand().getTypeId()!=259)return;
        if(!e.getAction().equals(Action.RIGHT_CLICK_BLOCK))return;
        Player p = e.getPlayer();
        if(!Pass.taskList.list.containsKey(String.valueOf(Pass.infoList.getPlayerLevel(p.getName()))))return;
        if(!Pass.taskList.getTask(Pass.infoList.getPlayerLevel(p.getName())).type.equalsIgnoreCase("location"))return;
        List<Location> last = new ArrayList<>();
        Pass.infoList.getPlayerInfo(p).location.forEach(location -> {
            if(location.getWorld().equals(p.getWorld())){
                double dx = location.getX() - p.getLocation().getX();
                double dy = location.getY() - p.getLocation().getY();
                double dz = location.getZ() - p.getLocation().getZ();
                double r = Math.sqrt(dx*dx+dy*dy+dz*dz);
                if(r >= 3) last.add(location);
                else p.sendMessage("已成功标记此地点");
            }else{
                last.add(location);
            }
        });
        Pass.infoList.getPlayerInfo(p).location = last;
        Pass.infoList.getPlayerInfo(p).updateConfig();
        if(last.isEmpty()){
            if(Pass.taskList.canFinish(p)){
                Pass.taskList.Finish(p);
            }
        }
    }
}
