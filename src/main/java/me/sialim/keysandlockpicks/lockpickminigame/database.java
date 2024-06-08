package me.sialim.keysandlockpicks.lockpickminigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import me.sialim.keysandlockpicks.main.Config;
import me.sialim.keysandlockpicks.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class database {
    private static HashMap<String, Location> blockdoor = new HashMap();
    private static HashMap<String, Container> chestc = new HashMap();
    private static HashMap<String, ArrayList<Integer>> sequnze = new HashMap();
    private static HashMap<String, Integer> sequnzeInt = new HashMap();
    private static HashMap<Integer, ArrayList<ItemStack>> lootboxes = new HashMap();
    private static Random ran = new Random();

    public static void setPlayerDoor(Player player, Block door) {
        String playeruuid = player.getUniqueId().toString();
        if (door != null) {
            blockdoor.put(playeruuid, door.getLocation());
        } else {
            blockdoor.put(playeruuid, door.getLocation());
        }

    }

    public static Location getPlayerDoor(Player player) {
        String playeruuid = player.getUniqueId().toString();
        return (Location)blockdoor.get(playeruuid);
    }

    public static void setPlayerChest(Player player, Container chest) {
        String playeruuid = player.getUniqueId().toString();
        chestc.put(playeruuid, chest);
    }

    public static Container getPlayerChest(Player player) {
        String playeruuid = player.getUniqueId().toString();
        return (Container)chestc.get(playeruuid);
    }

    public static void setPlayerSequnze(Player player, ArrayList<Integer> slot) {
        String playeruuid = player.getUniqueId().toString();
        sequnze.put(playeruuid, slot);
        sequnzeInt.put(playeruuid, 0);
    }

    public static ArrayList<Integer> getPlayerSequnze(Player player) {
        String playeruuid = player.getUniqueId().toString();
        return (ArrayList)sequnze.get(playeruuid);
    }

    public static void setPlayerSequnzeInt(Player player, int slot) {
        String playeruuid = player.getUniqueId().toString();
        sequnzeInt.put(playeruuid, slot);
    }

    public static int getPlayerSequnzeInt(Player player) {
        String playeruuid = player.getUniqueId().toString();
        return sequnzeInt.containsKey(playeruuid) ? (Integer)sequnzeInt.get(playeruuid) : 0;
    }

    public static void setLastLootbox(Player player, String loc, long now) {
        Config c = new Config("userdata", Main.getInstance());
        c.set(player.getUniqueId().toString() + "." + loc, now);
        c.save();
    }

    public static boolean canOpenLootbox(Player player, String loc) {
        Config c = new Config("config", Main.getInstance());
        Config cu = new Config("userdata", Main.getInstance());
        FileConfiguration cf = c.getConfig();
        FileConfiguration cuf = cu.getConfig();
        if (cf.getLong("lootbox.delay") >= 0L && (System.currentTimeMillis() - cuf.getLong(player.getUniqueId().toString() + "." + loc)) / 1000L >= cf.getLong("lootbox.delay")) {
            return true;
        } else {
            return !cuf.isSet(player.getUniqueId().toString() + "." + loc);
        }
    }

    public static void saveLootbox(Inventory inv, int tier) {
        Config c = new Config("loottable", Main.getInstance());
        FileConfiguration cf = c.getConfig();
        cf.set("loot." + tier + ".items", (Object)null);
        ConfigurationSection ncf = cf.createSection("loot." + tier + ".items");
        int curr = 0;
        ItemStack[] var9;
        int var8 = (var9 = inv.getContents()).length;

        for(int var7 = 0; var7 < var8; ++var7) {
            ItemStack item = var9[var7];
            ncf.set("" + curr, item);
            ++curr;
        }

        c.save();
    }

    public static void loadLootbox() {
        Config c = new Config("loottable", Main.getInstance());
        ConfigurationSection cf = c.getConfig().getConfigurationSection("loot");
        Iterator var3 = cf.getKeys(false).iterator();

        while(var3.hasNext()) {
            String s = (String)var3.next();
            ConfigurationSection loottable = cf.getConfigurationSection(s + ".items");
            ArrayList<ItemStack> items = new ArrayList();
            int tier = Integer.parseInt(s);
            Iterator var8 = loottable.getKeys(false).iterator();

            while(var8.hasNext()) {
                String sl = (String)var8.next();
                ItemStack item = loottable.getItemStack(sl);
                items.add(item);
            }

            lootboxes.put(tier, items);
        }

    }

    public static Inventory openLootbox(int tier, boolean perm) {
        Config c = new Config("config", Main.getInstance());
        int min = c.getConfig().getInt("lootbox.min");
        int max = c.getConfig().getInt("lootbox.max");
        int chance = c.getConfig().getInt("lootbox.chance");
        int curr = 0;
        ArrayList<ItemStack> items = (ArrayList)lootboxes.get(tier);
        boolean[] chosen = new boolean[54];
        FileConfiguration clang = Main.getLang();
        Inventory inv;
        if (perm) {
            inv = Bukkit.createInventory((InventoryHolder)null, 54, ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.permlootbox")));
        } else {
            inv = Bukkit.createInventory((InventoryHolder)null, 54, ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.lootbox")));
        }

        while(curr < min) {
            while(curr < max) {
                int slot;
                do {
                    slot = ran.nextInt(54);
                } while(chosen[slot]);

                chosen[slot] = true;
                inv.setItem(slot, (ItemStack)items.get(ran.nextInt(items.size())));
                ++curr;
                if (ran.nextInt(100) < chance) {
                    break;
                }
            }
        }

        return inv;
    }
}
