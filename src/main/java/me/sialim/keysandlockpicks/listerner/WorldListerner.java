package me.sialim.keysandlockpicks.listerner;

import java.util.Iterator;
import java.util.List;

import me.sialim.keysandlockpicks.main.Config;
import me.sialim.keysandlockpicks.main.Main;
import me.sialim.keysandlockpicks.main.craftingnew;
import me.sialim.keysandlockpicks.main.extreMethods;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

public class WorldListerner implements Listener {
    static FileConfiguration clang = Main.getLang();
    static FileConfiguration config = Main.getPluginConfig();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().discoverRecipes(craftingnew.getCustomRecipes());
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Config cc = new Config("config", Main.getInstance());
        if (cc.getConfig().getBoolean("explodable")) {
            Iterator it = e.blockList().iterator();

            while(it.hasNext()) {
                Block b = (Block)it.next();
                if (b.getRelative(BlockFace.DOWN).getType().name().endsWith("DOOR")) {
                    if (b.getRelative(BlockFace.UP).getType().name().endsWith("DOOR")) {
                        return;
                    }

                    b = b.getRelative(BlockFace.DOWN);
                }

                Config c = new Config("DoorData", Main.getInstance());
                ItemStack trap;
                if (c.getConfig().isSet(b.getLocation().toString() + ".lock")) {
                    trap = c.getConfig().getItemStack(b.getLocation().toString() + ".lock");
                    e.getEntity().getWorld().dropItem(b.getLocation(), trap);
                    c.set(b.getLocation().toString() + ".lock", (Object)null);
                    c.save();
                }

                if (c.getConfig().isSet(b.getLocation().toString() + ".trap")) {
                    trap = c.getConfig().getItemStack(b.getLocation().toString() + ".trap");
                    e.getEntity().getWorld().dropItem(b.getLocation(), trap);
                    c.set(b.getLocation().toString() + ".trap", (Object)null);
                    c.save();
                }
            }
        }

    }

    @EventHandler
    public void onMinecartDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof StorageMinecart) {
            StorageMinecart cart = (StorageMinecart)e.getEntity();
            if (cart.getLootTable() != null && config.getBoolean("lootbox.world.natural")) {
                e.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void onHopper(InventoryMoveItemEvent e) {
        Inventory inv = e.getSource();
        if (inv.getItem(0) != null) {
            ItemStack lock = inv.getItem(0);
            if (lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                Inventory hopper = e.getDestination();
                e.setCancelled(true);
                if (hopper.getType().equals(InventoryType.HOPPER) && hopper.getItem(4) != null) {
                    ItemStack key = hopper.getItem(4);
                    if (key.hasItemMeta() && key.getItemMeta().hasLore()) {
                        List<String> lore = key.getItemMeta().getLore();
                        if (lore.get(0) != null && lore.get(1) != null && ((String)lore.get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && ((String)lore.get(1)).equals(lock.getItemMeta().getLore().get(1))) {
                            for(int i = 1; i < inv.getSize(); ++i) {
                                ItemStack item = inv.getItem(i);
                                if (item != null && item.getType() != Material.AIR && !extreMethods.hasLore(item, ChatColor.translateAlternateColorCodes('&', Main.getLang().getString("crafting.difficultyloreid"))) && !extreMethods.hasLore(item, ChatColor.translateAlternateColorCodes('&', Main.getLang().getString("crafting.potiontraploreid"))) && !extreMethods.hasLore(item, ChatColor.translateAlternateColorCodes('&', Main.getLang().getString("crafting.redstonetraploreid")))) {
                                    break;
                                }
                            }
                        }
                    }
                }

                return;
            }
        } else if (e.getItem().hasItemMeta()) {
            ItemMeta lock = e.getItem().getItemMeta();
            if (lock.hasLore() && ((String)lock.getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                e.setCancelled(true);
            }
        }

        if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && ((String)e.getItem().getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid")))) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onNameTag(PlayerInteractEntityEvent e) {
        PlayerInventory inv = e.getPlayer().getInventory();
        ItemStack key = null;
        if (e.getHand().equals(EquipmentSlot.HAND)) {
            key = inv.getItemInMainHand();
        } else if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
            key = inv.getItemInOffHand();
        }

        if (key != null && key.hasItemMeta() && key.getItemMeta().hasLore()) {
            String lore = (String)key.getItemMeta().getLore().get(0);
            if (lore.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) || lore.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.lockpickloreid"))) || lore.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                e.setCancelled(true);
                if (e.getHand().equals(EquipmentSlot.HAND)) {
                    inv.setItemInMainHand(key);
                } else if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
                    inv.setItemInOffHand(key);
                }
            }
        }

    }
}
