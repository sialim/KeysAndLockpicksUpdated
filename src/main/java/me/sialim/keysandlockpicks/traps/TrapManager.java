package me.sialim.keysandlockpicks.traps;

import java.util.List;

import me.sialim.keysandlockpicks.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

public class TrapManager {
    public static boolean activatTrap(ItemStack trap, Player p, Block block) {
        FileConfiguration clf = Main.getLang();
        if (trap.hasItemMeta() && trap.getItemMeta().hasLore()) {
            List<String> lore = trap.getItemMeta().getLore();
            if (((String)lore.get(0)).equals(ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontraploreid")))) {
                try {
                    String[] typelore = ((String)lore.get(1)).replace(ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontraptype")), "").split(":");
                    PotionType type = PotionType.valueOf(typelore[0]);
                    int strength = Integer.parseInt(typelore[1]) - 1;
                    int charges = Integer.parseInt(((String)lore.get(3)).replace(ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontrapcharges")), ""));
                    boolean splash = Boolean.parseBoolean(((String)lore.get(2)).replace(ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontrapsplash")), ""));
                    if (charges >= 1) {
                        lore.set(3, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontrapcharges")) + (charges - 1));
                        ItemMeta newmeta = trap.getItemMeta();
                        newmeta.setLore(lore);
                        trap.setItemMeta(newmeta);
                        PotionTrap.potionTrap(type, strength, splash, p);
                        return true;
                    }
                } catch (IllegalArgumentException var11) {
                    return false;
                }
            } else if (((String)lore.get(0)).equals(ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.redstonetraploreid")))) {
                RedstoneTrap.redstoneTrap(block);
            }
        }

        return false;
    }
}
