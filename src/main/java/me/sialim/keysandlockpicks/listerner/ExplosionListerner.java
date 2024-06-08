package me.sialim.keysandlockpicks.listerner;

import java.util.Iterator;

import me.sialim.keysandlockpicks.main.Config;
import me.sialim.keysandlockpicks.main.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class ExplosionListerner implements Listener {
    static FileConfiguration clang = Main.getLang();

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        Iterator it = e.blockList().iterator();

        while(true) {
            while(it.hasNext()) {
                Block b = (Block)it.next();
                Material blockType = b.getType();
                if (!blockType.equals(Material.CHEST) && !blockType.name().endsWith("SHULKER_BOX")) {
                    if (blockType.name().endsWith("DOOR")) {
                        Block bc = b;
                        if (b.getRelative(BlockFace.DOWN).getType().name().endsWith("DOOR")) {
                            if (b.getRelative(BlockFace.UP).getType().name().endsWith("DOOR")) {
                                return;
                            }

                            bc = b.getRelative(BlockFace.DOWN);
                        }

                        Config c = new Config("DoorData", Main.getInstance());
                        if (c.getConfig().isSet(bc.getLocation().toString() + ".lock")) {
                            it.remove();
                        }
                    }
                } else if (b.getState() instanceof InventoryHolder) {
                    InventoryHolder con = (InventoryHolder)b.getState();
                    if (con.getInventory().getItem(0) != null) {
                        ItemStack lock = con.getInventory().getItem(0);
                        if (lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                            it.remove();
                        }
                    }
                }
            }

            return;
        }
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        Iterator it = e.blockList().iterator();

        while(true) {
            while(it.hasNext()) {
                Block b = (Block)it.next();
                Material blockType = b.getType();
                if (!blockType.equals(Material.CHEST) && !blockType.name().endsWith("SHULKER_BOX")) {
                    if (blockType.name().endsWith("DOOR")) {
                        Block bc = b;
                        if (b.getRelative(BlockFace.DOWN).getType().name().endsWith("DOOR")) {
                            if (b.getRelative(BlockFace.UP).getType().name().endsWith("DOOR")) {
                                return;
                            }

                            bc = b.getRelative(BlockFace.DOWN);
                        }

                        Config c = new Config("DoorData", Main.getInstance());
                        if (c.getConfig().isSet(bc.getLocation().toString() + ".lock")) {
                            it.remove();
                        }
                    }
                } else if (b.getState() instanceof InventoryHolder) {
                    InventoryHolder con = (InventoryHolder)b.getState();
                    if (con.getInventory().getItem(0) != null) {
                        ItemStack lock = con.getInventory().getItem(0);
                        if (lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                            it.remove();
                        }
                    }
                }
            }

            return;
        }
    }
}
