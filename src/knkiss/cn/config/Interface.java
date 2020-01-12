package knkiss.cn.config;

import knkiss.cn.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Interface {
    static ItemStack SetAmount_add1 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 +1");
    static ItemStack SetAmount_add5 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 +5");
    static ItemStack SetAmount_add10 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 +10");
    static ItemStack SetAmount_add50 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 +50");
    static ItemStack SetAmount_add100 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 +100");
    static ItemStack SetAmount_rem1 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 -1");
    static ItemStack SetAmount_rem5 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 -5");
    static ItemStack SetAmount_rem10 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 -10");
    static ItemStack SetAmount_rem50 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 -50");
    static ItemStack SetAmount_rem100 = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击设置数量 -100");
    static ItemStack SetAmount_End  = Utils.newItem(Material.ENCHANTED_BOOK, 0, "结束设置", "点击结束设置");

    static ItemStack main_Collect = Utils.newItem(Material.BUCKET, 0, "收集类任务", "点击创建收集类任务");
    static ItemStack main_Craft = Utils.newItem(Material.WORKBENCH, 0, "合成类任务", "点击创建合成类任务");
    static ItemStack main_Kill = Utils.newItem(Material.DIAMOND_SWORD, 0, "击杀类任务", "点击创建击杀类任务");
    static ItemStack main_Location = Utils.newItem(Material.FLINT_AND_STEEL, 0, "标记类任务", "点击创建标记类任务");

    static ItemStack logo_Tip = Utils.newItem(Material.ENCHANTED_BOOK, 0, "选择任务图标", "左键点击你想设置的任务图标");
    static ItemStack CollectOrCraft_Tip = Utils.newItem(Material.ENCHANTED_BOOK, 0, "添加任务需求", "左键点击你想添加的任务需求物品");
    static ItemStack CollectOrCraft_End = Utils.newItem(Material.ENCHANTED_BOOK, 0, "结束添加", "点击结束添加");
    static ItemStack kill_Tip = Utils.newItem(Material.ENCHANTED_BOOK, 0, "此任务暂不支持可视化编辑", "请自行打开config进行设置，爱你，啾咪~");
    static ItemStack Location_Tip = Utils.newItem(Material.ENCHANTED_BOOK, 0, "点击获得标记书", "");
    static ItemStack Location_End = Utils.newItem(Material.ENCHANTED_BOOK, 0, "结束添加", "点击结束添加");

    static ItemStack reward_Tip = Utils.newItem(Material.ENCHANTED_BOOK, 0, "添加奖励图标", "左键点击你想添加的奖励物品");
    static ItemStack reward_End = Utils.newItem(Material.ENCHANTED_BOOK, 0, "结束添加", "点击结束添加");

    public void logoInv(Player p){
        Inventory logo = Bukkit.createInventory(p, 9, "选择任务图标");
        logo.setItem(4, logo_Tip);
        p.openInventory(logo);
    }

    public void mainInv(Player p){
        Inventory main = Bukkit.createInventory(p, 9, "选择任务类型");
        main.setItem(0, main_Collect);
        main.setItem(1, main_Craft);
        main.setItem(2, main_Kill);
        main.setItem(3, main_Location);
        p.openInventory(main);
    }

    public void collectOrcraftInv(Player p){
        Inventory collectOrcraft = Bukkit.createInventory(p, 6*9, "添加需求物品");
        for(int i = 0; i < ConfigListener.submit.size(); i++){
            collectOrcraft.setItem(i, ConfigListener.submit.get(i));
        }
        collectOrcraft.setItem(49, CollectOrCraft_Tip);
        collectOrcraft.setItem(53, CollectOrCraft_End);
        p.openInventory(collectOrcraft);
    }

    public void killInv(Player p){
        Inventory kill = Bukkit.createInventory(p, 9, "击杀类任务可视化暂不支持");
        kill.setItem(4, kill_Tip);
        p.openInventory(kill);
    }

    public void locationInv(Player p){
        Inventory location = Bukkit.createInventory(p,6*9, "添加标记坐标");
        for(int i = 0; i < ConfigListener.location.size(); i++){
            ItemStack item = Utils.newItem(Material.TORCH, 0, "坐标点", ConfigListener.location.get(i));
            location.setItem(i, item);
        }
        location.setItem(49, Location_Tip);
        location.setItem(53, Location_End);
        p.openInventory(location);
    }

    public void rewardInv(Player p){
        Inventory reward = Bukkit.createInventory(p, 6*9, "添加奖励物品");
        reward.setItem(49, reward_Tip);
        reward.setItem(53, reward_End);
        p.openInventory(reward);
    }

    public void setSubmitAmountInv(Player p){
        Inventory setAmount = Bukkit.createInventory(p,2*9, "设置需求物品数量");
        setAmount.setItem(0, SetAmount_add1);
        setAmount.setItem(1, SetAmount_add5);
        setAmount.setItem(2, SetAmount_add10);
        setAmount.setItem(3, SetAmount_add50);
        setAmount.setItem(4, SetAmount_add100);

        setAmount.setItem(9, SetAmount_rem1);
        setAmount.setItem(10, SetAmount_rem5);
        setAmount.setItem(11, SetAmount_rem10);
        setAmount.setItem(12, SetAmount_rem50);
        setAmount.setItem(13, SetAmount_rem100);

        setAmount.setItem(17, SetAmount_End);
        p.openInventory(setAmount);
    }

    public void setRewardAmountInv(Player p){
        Inventory setAmount = Bukkit.createInventory(p,2*9, "设置奖励物品数量");
        setAmount.setItem(0, SetAmount_add1);
        setAmount.setItem(1, SetAmount_add5);
        setAmount.setItem(2, SetAmount_add10);
        setAmount.setItem(3, SetAmount_add50);
        setAmount.setItem(4, SetAmount_add100);

        setAmount.setItem(9, SetAmount_rem1);
        setAmount.setItem(10, SetAmount_rem5);
        setAmount.setItem(11, SetAmount_rem10);
        setAmount.setItem(12, SetAmount_rem50);
        setAmount.setItem(13, SetAmount_rem100);

        setAmount.setItem(17, SetAmount_End);
        p.openInventory(setAmount);
    }
























}
