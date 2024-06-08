package me.sialim.keysandlockpicks.listerner;

import java.util.Iterator;

import me.sialim.keysandlockpicks.lockpickminigame.database;
import me.sialim.keysandlockpicks.main.Config;
import me.sialim.keysandlockpicks.main.Main;
import me.sialim.keysandlockpicks.main.extreMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class GuiListerner implements Listener {
    private FileConfiguration clf = Main.getLang();
    private FileConfiguration cf = Main.getCrafting();

    public Inventory showRecipe(String recipe) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 45, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.recipe")));
        extreMethods.setRow(extreMethods.ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "", 0), inv, 0, 44);
        int i;
        if (recipe.equals("lockpick")) {
            inv.setItem(40, extreMethods.ItemStack(Material.CRAFTING_TABLE, 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.workbench")), 0, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.shapeless"))));
            i = 9;

            for(Iterator var5 = this.cf.getStringList("lockpick").iterator(); var5.hasNext(); ++i) {
                String s = (String)var5.next();
                inv.setItem(i, new ItemStack(Material.getMaterial(s)));
            }
        } else {
            for(i = 3; i > 0; --i) {
                for(int a = 2; a >= 0; --a) {
                    if (this.cf.isSet(recipe + ".materials." + this.cf.getString(recipe + "." + i).charAt(a))) {
                        inv.setItem(i * 9 + a + 3, new ItemStack(Material.getMaterial(this.cf.getString(recipe + ".materials." + this.cf.getString(recipe + "." + i).charAt(a)))));
                    } else {
                        inv.setItem(i * 9 + a + 3, (ItemStack)null);
                    }
                }
            }

            inv.setItem(40, extreMethods.ItemStack(Material.CRAFTING_TABLE, 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.workbench")), 0));
        }

        inv.setItem(0, extreMethods.ItemStack(Material.RED_WOOL, 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.back")), 0));
        return inv;
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        if (e.getView().getTitle() != null) {
            String name = e.getView().getTitle();
            if (name.contains("§6Loot Table tier: ")) {
                database.saveLootbox(inv, Integer.parseInt(name.replace("§6Loot Table tier: ", "")));
            }
        }

    }

    @EventHandler
    public void invClickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inv = e.getClickedInventory();
            Player p = (Player)e.getWhoClicked();
            if (e.getView().getTitle() != null && e.getClickedInventory().getType() != InventoryType.PLAYER) {
                String name = e.getView().getTitle();
                int slot;
                Inventory ninv;
                if (name.equals("§6Loot Boxes")) {
                    slot = e.getSlot();
                    e.setCancelled(true);
                    if (slot >= 11) {
                        if (slot <= 15) {
                            ninv = Bukkit.createInventory((InventoryHolder)null, 54, "§6Loot Table tier: " + (slot - 10));
                            Config c = new Config("loottable", Main.getInstance());
                            ConfigurationSection cf = c.getConfig().getConfigurationSection("loot." + (slot - 10) + ".items");
                            int curr = 0;

                            for(Iterator var11 = cf.getKeys(false).iterator(); var11.hasNext(); ++curr) {
                                String sl = (String)var11.next();
                                Main.getInstance().getLogger().info(sl);
                                ItemStack item = cf.getItemStack(sl);
                                ninv.setItem(curr, item);
                            }

                            p.openInventory(ninv);
                        } else if (slot == 35) {
                            database.loadLootbox();
                        }
                    }
                } else if (name.equals(ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.main")))) {
                    e.setCancelled(true);
                    slot = e.getSlot();
                    if (slot >= 11 && slot <= 15 || slot >= 20 && slot <= 23) {
                        String recipe;
                        if (slot == 11) {
                            recipe = "weak";
                        } else if (slot == 12) {
                            recipe = "normal";
                        } else if (slot == 13) {
                            recipe = "tough";
                        } else if (slot == 14) {
                            recipe = "strong";
                        } else if (slot == 15) {
                            recipe = "fine";
                        } else if (slot == 20) {
                            recipe = "potiontrap";
                        } else if (slot == 21) {
                            recipe = "redstonetrap";
                        } else if (slot == 22) {
                            recipe = "lockpick";
                        } else {
                            recipe = "key";
                        }

                        p.openInventory(this.showRecipe(recipe));
                    }
                } else if (name.equals(ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.recipe")))) {
                    e.setCancelled(true);
                    slot = e.getSlot();
                    if (slot == 40) {
                        ninv = Bukkit.createInventory((InventoryHolder)null, 36, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.main")));
                        extreMethods.setRow(extreMethods.ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "", 0), inv, 0, 35);
                        ninv.setItem(11, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.weak")), Main.getLockDur()));
                        ninv.setItem(12, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.normal")), Main.getLockDur()));
                        ninv.setItem(13, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.tough")), Main.getLockDur()));
                        ninv.setItem(14, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.strong")), Main.getLockDur()));
                        ninv.setItem(15, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.fine")), Main.getLockDur()));
                        ninv.setItem(20, extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.potiontrap")), Main.getTrapDur()));
                        ninv.setItem(21, extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.redstonetrap")), Main.getTrapDur()));
                        ninv.setItem(22, extreMethods.ItemStack(Main.getLockpick(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.lockpick")), Main.getLockpickDur()));
                        ninv.setItem(23, extreMethods.ItemStack(Main.getKey(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.key")), Main.getKeyDur()));
                        p.openInventory(ninv);
                    }
                }
            }
        }

    }
}
