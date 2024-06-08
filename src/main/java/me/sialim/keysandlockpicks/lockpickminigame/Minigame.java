package me.sialim.keysandlockpicks.lockpickminigame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import me.sialim.keysandlockpicks.api.events.OpenMinigameEvent;
import me.sialim.keysandlockpicks.main.Main;
import me.sialim.keysandlockpicks.main.extreMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class Minigame {
    public static void openMinigame(Player player, int tier) {
        FileConfiguration cf = Main.getLang();
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 27, ChatColor.translateAlternateColorCodes('&', cf.getString("minigame.name")) + tier);
        ItemStack cylind = extreMethods.ItemStack(Main.getCylinder(), 1, ChatColor.translateAlternateColorCodes('&', cf.getString("minigame.ironcylind")), Main.getCylinderDur());
        ItemStack guide = extreMethods.ItemStack(Main.getGuide(), 1, ChatColor.translateAlternateColorCodes('&', cf.getString("minigame.guide.name")), Main.getGuideDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("minigame.guide.line1")), ChatColor.translateAlternateColorCodes('&', cf.getString("minigame.guide.line2")));
        ItemStack bars = new ItemStack(Main.getBar(), 1);
        ArrayList<Integer> newSequnze = new ArrayList();
        int loop = 0;

        for(int i = 10; i > 1; --i) {
            int loopMid = loop + 10;
            if (loopMid < 12 + tier) {
                inv.setItem(loopMid, cylind);
                newSequnze.add(loopMid);
            }

            int loopBottom = loop + 21 + tier;
            int loopTop = loop + 3 + tier;
            if (loopBottom < 27) {
                inv.setItem(loopBottom, bars);
                inv.setItem(loopTop, bars);
            }

            loopBottom = loop + 19;
            loopTop = loop + 1;
            Random chance = new Random();
            int chanceValue = chance.nextInt(100) + 1;
            if (loopBottom <= 26) {
                if (chanceValue < 50) {
                    inv.setItem(loopBottom, bars);
                } else {
                    inv.setItem(loopTop, bars);
                }
            }

            ++loop;
        }

        Collections.shuffle(newSequnze);
        inv.setItem(0, bars);
        inv.setItem(9, guide);
        inv.setItem(18, bars);
        OpenMinigameEvent event = new OpenMinigameEvent(player, tier, inv, newSequnze);
        Bukkit.getPluginManager().callEvent(event);
        if (!event.isCancelled()) {
            database.setPlayerSequnze(player, event.getSequnze());
            event.getPlayer().openInventory(event.getInv());
        }

    }
}
