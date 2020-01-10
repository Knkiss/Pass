package knkiss.cn;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Util {
    public static ItemStack newItem(int ID, int amount, int Durability, String name, List<String> lore)
    {
        ItemStack i = new ItemStack(ID,amount,(short)Durability);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(name);
        iMeta.setLore(lore);
        i.setItemMeta(iMeta);
        return i;
    }

    public static ItemStack newItem(Material material,int Durability,String Display)
    {
        ItemStack i = new ItemStack(material);
        i.setDurability((short) Durability);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(Display);
        i.setItemMeta(iMeta);
        return i;
    }

    public static ItemStack newItem(int ID,int Durability,String Display)
    {
        ItemStack i = new ItemStack(ID);
        i.setDurability((short) Durability);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(Display);
        i.setItemMeta(iMeta);
        return i;
    }

    public static ItemStack newItem(Material material,int Durability,String Display,String Lore)
    {
        ItemStack i = new ItemStack(material);
        i.setDurability((short) Durability);
        ItemMeta iMeta = i.getItemMeta();
        List<String> iMetaLore = new ArrayList<>();
        iMetaLore.add(Lore);
        iMeta.setLore(iMetaLore);
        iMeta.setDisplayName(Display);
        i.setItemMeta(iMeta);
        return i;
    }

    public static boolean canParseInt(String str){
        if(str == null){
            return false;
        }
        return str.matches("\\d+");
    }

    public static boolean isCraftAction(InventoryAction ac){
        if(ac.equals(InventoryAction.UNKNOWN))return false;
        if(ac.equals(InventoryAction.NOTHING))return false;
        if(ac.equals(InventoryAction.PLACE_ALL))return false;
        if(ac.equals(InventoryAction.PLACE_ONE))return false;
        if(ac.equals(InventoryAction.PLACE_SOME))return false;
        if(ac.equals(InventoryAction.SWAP_WITH_CURSOR))return false;
        if(ac.equals(InventoryAction.HOTBAR_MOVE_AND_READD))return false;
        if(ac.equals(InventoryAction.CLONE_STACK))return false;
        return !ac.equals(InventoryAction.COLLECT_TO_CURSOR);
    }


}
