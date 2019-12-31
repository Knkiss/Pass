package knkiss.cn;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class util {
    public static ItemStack newItem(int ID, int amount, int Durability, String name, String lore)
    {
        ItemStack i = new ItemStack(ID,amount,(short)Durability);
        ItemMeta iMeta = i.getItemMeta();
        iMeta.setDisplayName(name);
        List<String> iMetaLore = new ArrayList<String>();
        iMetaLore.add(lore);
        iMeta.setLore(iMetaLore);
        i.setItemMeta(iMeta);
        return i;
    }
}
