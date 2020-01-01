package knkiss.cn;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class util {
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

    public static ItemStack newItem(Material material,int Durability,String Display,String Lore)
    {
        ItemStack i = new ItemStack(material);
        i.setDurability((short) Durability);
        ItemMeta iMeta = i.getItemMeta();
        List<String> iMetaLore = new ArrayList<String>();
        iMetaLore.add(Lore);
        iMeta.setLore(iMetaLore);
        iMeta.setDisplayName(Display);
        i.setItemMeta(iMeta);
        return i;
    }
}
