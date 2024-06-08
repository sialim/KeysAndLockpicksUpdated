package me.sialim.keysandlockpicks.main;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.Plugin;

public class craftingnew {
    private static ArrayList<NamespacedKey> customRecipes = new ArrayList();

    public static ArrayList<NamespacedKey> getCustomRecipes() {
        return customRecipes;
    }

    public static void setupRecipes(Plugin plugin) {
        Material mlock = Main.getLock();
        FileConfiguration cf = Main.getCrafting();
        FileConfiguration clf = Main.getLang();
        ItemStack lockpick = extreMethods.ItemStack(Main.getLockpick(), 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.lockpick")), Main.getLockpickDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.lockpickloreid")));
        NamespacedKey lockpickKey = new NamespacedKey(plugin, "lockpick");
        customRecipes.add(lockpickKey);
        ShapelessRecipe lockpickRecipe = new ShapelessRecipe(lockpickKey, lockpick);
        Iterator var8 = cf.getStringList("lockpick").iterator();

        while(var8.hasNext()) {
            String s = (String)var8.next();
            lockpickRecipe.addIngredient(Material.getMaterial(s));
        }

        plugin.getServer().addRecipe(lockpickRecipe);
        ItemStack key = extreMethods.ItemStack(Main.getKey(), 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.key")), Main.getKeyDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.keyloreid")), "id: blank");
        NamespacedKey keyKey = new NamespacedKey(plugin, "key");
        customRecipes.add(keyKey);
        ShapedRecipe keyRecipe = new ShapedRecipe(keyKey, key);
        keyRecipe.shape(new String[]{cf.getString("key.1"), cf.getString("key.2"), cf.getString("key.3")});
        ConfigurationSection cs = cf.getConfigurationSection("key.materials");
        Iterator var12 = cs.getKeys(false).iterator();

        while(var12.hasNext()) {
            String s = (String)var12.next();
            keyRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(keyRecipe);
        ItemStack weakLock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.weak")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.difficultyloreid")) + "1", "id: blank");
        NamespacedKey weakLockKey = new NamespacedKey(plugin, "weakLock");
        customRecipes.add(weakLockKey);
        ShapedRecipe weakLockRecipe = new ShapedRecipe(weakLockKey, weakLock);
        weakLockRecipe.shape(new String[]{cf.getString("weak.1"), cf.getString("weak.2"), cf.getString("weak.3")});
        cs = cf.getConfigurationSection("weak.materials");
        Iterator var15 = cs.getKeys(false).iterator();

        while(var15.hasNext()) {
            String s = (String)var15.next();
            weakLockRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(weakLockRecipe);
        ItemStack normalLock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.normal")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.difficultyloreid")) + "2", "id: blank");
        NamespacedKey normalLockKey = new NamespacedKey(plugin, "normalLock");
        customRecipes.add(normalLockKey);
        ShapedRecipe normalLockRecipe = new ShapedRecipe(normalLockKey, normalLock);
        normalLockRecipe.shape(new String[]{cf.getString("normal.1"), cf.getString("normal.2"), cf.getString("normal.3")});
        cs = cf.getConfigurationSection("normal.materials");
        Iterator var18 = cs.getKeys(false).iterator();

        while(var18.hasNext()) {
            String s = (String)var18.next();
            normalLockRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(normalLockRecipe);
        ItemStack toughLock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.tough")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.difficultyloreid")) + "3", "id: blank");
        NamespacedKey toughLockKey = new NamespacedKey(plugin, "toughLock");
        customRecipes.add(toughLockKey);
        ShapedRecipe toughLockRecipe = new ShapedRecipe(toughLockKey, toughLock);
        toughLockRecipe.shape(new String[]{cf.getString("tough.1"), cf.getString("tough.2"), cf.getString("tough.3")});
        cs = cf.getConfigurationSection("tough.materials");
        Iterator var21 = cs.getKeys(false).iterator();

        while(var21.hasNext()) {
            String s = (String)var21.next();
            toughLockRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(toughLockRecipe);
        ItemStack strongLock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.strong")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.difficultyloreid")) + "4", "id: blank");
        NamespacedKey strongLockKey = new NamespacedKey(plugin, "strongLock");
        ShapedRecipe strongLockRecipe = new ShapedRecipe(strongLockKey, strongLock);
        customRecipes.add(strongLockKey);
        strongLockRecipe.shape(new String[]{cf.getString("strong.1"), cf.getString("strong.2"), cf.getString("strong.3")});
        cs = cf.getConfigurationSection("strong.materials");
        Iterator var24 = cs.getKeys(false).iterator();

        while(var24.hasNext()) {
            String s = (String)var24.next();
            strongLockRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(strongLockRecipe);
        ItemStack fineLock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.fine")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.difficultyloreid")) + "5", "id: blank");
        NamespacedKey fineLockKey = new NamespacedKey(plugin, "fineLock");
        customRecipes.add(fineLockKey);
        ShapedRecipe fineLockRecipe = new ShapedRecipe(fineLockKey, fineLock);
        fineLockRecipe.shape(new String[]{cf.getString("fine.1"), cf.getString("fine.2"), cf.getString("fine.3")});
        cs = cf.getConfigurationSection("fine.materials");
        Iterator var27 = cs.getKeys(false).iterator();

        while(var27.hasNext()) {
            String s = (String)var27.next();
            fineLockRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(fineLockRecipe);
        ItemStack masterLock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.master")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.difficultyloreid")) + "6", "id: blank");
        NamespacedKey masterLockKey = new NamespacedKey(plugin, "masterLock");
        ShapedRecipe masterLockRecipe = new ShapedRecipe(masterLockKey, masterLock);
        masterLockRecipe.shape(new String[]{cf.getString("master.1"), cf.getString("master.2"), cf.getString("master.3")});
        cs = cf.getConfigurationSection("master.materials");
        Iterator var30 = cs.getKeys(false).iterator();

        while(var30.hasNext()) {
            String s = (String)var30.next();
            masterLockRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(masterLockRecipe);
        ItemStack potionTrap = extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontrap")), Main.getTrapDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontraploreid")), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontraptype")), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontrapsplash")), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.potiontrapcharges")));
        NamespacedKey potionTrapKey = new NamespacedKey(plugin, "potionTrap");
        customRecipes.add(potionTrapKey);
        ShapedRecipe potionTrapRecipe = new ShapedRecipe(potionTrapKey, potionTrap);
        potionTrapRecipe.shape(new String[]{cf.getString("potiontrap.1"), cf.getString("potiontrap.2"), cf.getString("potiontrap.3")});
        cs = cf.getConfigurationSection("potiontrap.materials");
        Iterator var33 = cs.getKeys(false).iterator();

        while(var33.hasNext()) {
            String s = (String)var33.next();
            potionTrapRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(potionTrapRecipe);
        ItemStack redstoneTrap = extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.redstonetrap")), Main.getTrapDur(), ChatColor.translateAlternateColorCodes('&', clf.getString("crafting.redstonetraploreid")));
        NamespacedKey redstoneTrapKey = new NamespacedKey(plugin, "redstoneTrap");
        customRecipes.add(redstoneTrapKey);
        ShapedRecipe redstoneTrapRecipe = new ShapedRecipe(redstoneTrapKey, redstoneTrap);
        redstoneTrapRecipe.shape(new String[]{cf.getString("redstonetrap.1"), cf.getString("redstonetrap.2"), cf.getString("redstonetrap.3")});
        cs = cf.getConfigurationSection("redstonetrap.materials");
        Iterator var36 = cs.getKeys(false).iterator();

        while(var36.hasNext()) {
            String s = (String)var36.next();
            redstoneTrapRecipe.setIngredient(s.charAt(0), Material.getMaterial(cs.getString(s)));
        }

        plugin.getServer().addRecipe(redstoneTrapRecipe);
    }
}
