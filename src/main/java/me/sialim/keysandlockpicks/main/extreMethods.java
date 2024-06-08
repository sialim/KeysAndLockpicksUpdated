package me.sialim.keysandlockpicks.main;
import java.util.Arrays;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class extreMethods {
    public static ItemStack ItemStack(Material item, int amount, String display, int durr) {
        ItemStack customitem = new ItemStack(item, amount);
        ItemMeta custommeta = customitem.getItemMeta();
        custommeta.setDisplayName(display);
        if (custommeta instanceof Damageable) {
            ((Damageable)custommeta).setDamage(durr);
            custommeta.setUnbreakable(true);
            custommeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        }

        customitem.setItemMeta(custommeta);
        return customitem;
    }

    public static ItemStack ItemStack(Material item, int amount, String display, int durr, String... lore) {
        ItemStack customitem = new ItemStack(item, amount);
        ItemMeta custommeta = customitem.getItemMeta();
        custommeta.setDisplayName(display);
        if (custommeta instanceof Damageable) {
            ((Damageable)custommeta).setDamage(durr);
            custommeta.setUnbreakable(true);
            custommeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_UNBREAKABLE});
        }

        custommeta.setLore(Arrays.asList(lore));
        customitem.setItemMeta(custommeta);
        return customitem;
    }

    public static int getNumberOfItems(Material myMaterial, Inventory inv) {
        int amount = 0;
        ItemStack[] var6;
        int var5 = (var6 = inv.getContents()).length;

        for(int var4 = 0; var4 < var5; ++var4) {
            ItemStack item = var6[var4];
            if (item != null && item.getType() == myMaterial) {
                amount += item.getAmount();
            }
        }

        return amount;
    }

    public static boolean hasLockpick(Inventory inv) {
        ItemStack[] var4;
        int var3 = (var4 = inv.getContents()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            ItemStack item = var4[var2];
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', Main.getLang().getString("crafting.lockpickloreid")))) {
                return true;
            }
        }

        return false;
    }

    public static void removeLockpick(Inventory inv) {
        ItemStack[] var4;
        int var3 = (var4 = inv.getContents()).length;

        for(int var2 = 0; var2 < var3; ++var2) {
            ItemStack item = var4[var2];
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', Main.getLang().getString("crafting.lockpickloreid")))) {
                item.setAmount(item.getAmount() - 1);
                return;
            }
        }

    }

    public static void setRow(ItemStack item, Inventory inv, int start, int end) {
        int amountLoops = end - start;

        for(int i = amountLoops; i >= 0; --i) {
            int slot = start + i;
            inv.setItem(slot, item);
        }

    }

    public static boolean hasLore(ItemStack lock, String lore) {
        return lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(lore);
    }
}
