package me.sialim.keysandlockpicks.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CommandLockpick implements CommandExecutor {
    private FileConfiguration clf = Main.getLang();

    public boolean onCommand(CommandSender sender, Command cmd, String command, String[] args) {
        if ((cmd.getName().equalsIgnoreCase("lockpick") || cmd.getName().equalsIgnoreCase("lp") || cmd.getName().equalsIgnoreCase("lock")) && sender instanceof Player) {
            FileConfiguration cf = Main.getLang();
            Player p = (Player) sender;
            if (args.length >= 1 && args.length <= 3) {
                Inventory inv;
                if (!args[0].equals("recipe")) {
                    if (!p.hasPermission("lockpick.admin")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', cf.getString("command.noperm")));
                        return true;
                    }

                    if (args[0] != null || !args[0].isEmpty()) {
                        int tier;
                        if (args[0].equals("lock")) {
                            if (args[1] != null || !args[1].isEmpty()) {
                                try {
                                    tier = Integer.parseInt(args[1]);
                                    if (tier >= 1 && tier <= 6) {
                                        Material mlock = Main.getLock();
                                        ItemStack lock;
                                        if (tier == 1) {
                                            lock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.weak")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.difficultyloreid")) + "1", "id: blank");
                                        } else if (tier == 2) {
                                            lock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.normal")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.difficultyloreid")) + "2", "id: blank");
                                        } else if (tier == 3) {
                                            lock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.tough")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.difficultyloreid")) + "3", "id: blank");
                                        } else if (tier == 4) {
                                            lock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.strong")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.difficultyloreid")) + "4", "id: blank");
                                        } else if (tier == 5) {
                                            lock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.fine")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.difficultyloreid")) + "5", "id: blank");
                                        } else {
                                            lock = extreMethods.ItemStack(mlock, 1, ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.master")), Main.getLockDur(), ChatColor.translateAlternateColorCodes('&', cf.getString("crafting.difficultyloreid")) + "6", "id: blank");
                                        }

                                        p.getInventory().addItem(new ItemStack[]{lock});
                                        return true;
                                    }
                                } catch (NumberFormatException var12) {
                                }
                            }

                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', cf.getString("command.mustnumber")));
                            return true;
                        }

                        if (args[0].equals("lootbox")) {
                            if (args.length == 3) {
                                boolean var15 = false;

                                try {
                                    tier = Integer.parseInt(args[1]);
                                } catch (NumberFormatException var13) {
                                    return false;
                                }

                                boolean perm = Boolean.parseBoolean(args[2]);
                                String name;
                                if (perm) {
                                    name = ChatColor.translateAlternateColorCodes('&', cf.getString("lootbox.permlootbox")) + tier;
                                } else {
                                    name = ChatColor.translateAlternateColorCodes('&', cf.getString("lootbox.lootbox")) + tier;
                                }

                                Block block = p.getWorld().getBlockAt(p.getLocation());
                                block.setType(Material.CHEST);
                                Chest chest = (Chest)block.getState();
                                chest.setCustomName(name);
                                chest.update();
                                return true;
                            }

                            inv = Bukkit.createInventory((InventoryHolder)null, 36, "§6Loot Boxes");
                            inv.setItem(11, extreMethods.ItemStack(Main.getLock(), 1, "Weak loot table", Main.getLockDur()));
                            inv.setItem(12, extreMethods.ItemStack(Main.getLock(), 1, "§aNormal loot table", Main.getLockDur()));
                            inv.setItem(13, extreMethods.ItemStack(Main.getLock(), 1, "§6Tough loot table", Main.getLockDur()));
                            inv.setItem(14, extreMethods.ItemStack(Main.getLock(), 1, "§4Strong loot table", Main.getLockDur()));
                            inv.setItem(15, extreMethods.ItemStack(Main.getLock(), 1, "§bFine loot table", Main.getLockDur()));
                            inv.setItem(35, extreMethods.ItemStack(Material.BLUE_WOOL, 1, "§fLoad loot tables", 0, "§fthis will apply you changes", "§fto the loot boxes warning", "might curse a little lag"));
                            p.openInventory(inv);
                            return true;
                        }

                        if (!args[0].equals("unstealable") && !args[0].equals("us")) {
                            if (args[0].equals("reload")) {
                                Main.getInstance().onDisable();
                                Main.getInstance().onEnable();
                                p.sendMessage("LocksAndLockpicking has been reloaded");
                                return true;
                            }
                        } else if (p.getInventory().getItemInMainHand() != null) {
                            ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
                            if (meta.hasLore()) {
                                List<String> lore = meta.getLore();
                                lore.add(ChatColor.translateAlternateColorCodes('&', cf.getString("tags.unstealable")));
                                meta.setLore(lore);
                            } else {
                                ArrayList<String> lore = new ArrayList();
                                lore.add(ChatColor.translateAlternateColorCodes('&', cf.getString("tags.unstealable")));
                                meta.setLore(lore);
                            }

                            p.getInventory().getItemInMainHand().setItemMeta(meta);
                            return true;
                        }
                    }

                    p.sendMessage("§b------------§7[§aLockpick§7]§b------------");
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cf.getString("command.help.lock")));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cf.getString("command.help.lootbox")));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cf.getString("command.help.lootbox2")));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', cf.getString("command.help.help")));
                    p.sendMessage("§b----------------------------------");
                    return true;
                }

                if (p.hasPermission("lockpick.recipe.view")) {
                    inv = Bukkit.createInventory((InventoryHolder)null, 36, ChatColor.translateAlternateColorCodes('&', this.clf.getString("gui.recipe.main")));
                    extreMethods.setRow(extreMethods.ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "", 0), inv, 0, 35);
                    inv.setItem(11, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.weak")), Main.getLockDur()));
                    inv.setItem(12, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.normal")), Main.getLockDur()));
                    inv.setItem(13, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.tough")), Main.getLockDur()));
                    inv.setItem(14, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.strong")), Main.getLockDur()));
                    inv.setItem(15, extreMethods.ItemStack(Main.getLock(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.fine")), Main.getLockDur()));
                    inv.setItem(20, extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.potiontrap")), Main.getTrapDur()));
                    inv.setItem(21, extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.redstonetrap")), Main.getTrapDur()));
                    inv.setItem(22, extreMethods.ItemStack(Main.getLockpick(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.lockpick")), Main.getLockpickDur()));
                    inv.setItem(23, extreMethods.ItemStack(Main.getKey(), 1, ChatColor.translateAlternateColorCodes('&', this.clf.getString("crafting.key")), Main.getKeyDur()));
                    p.openInventory(inv);
                }
            }
        }

        return true;
    }
}
