package me.sialim.keysandlockpicks.lockpickminigame;

import me.sialim.keysandlockpicks.main.Config;
import me.sialim.keysandlockpicks.main.Main;
import me.sialim.keysandlockpicks.main.extreMethods;
import me.sialim.keysandlockpicks.traps.TrapManager;
import org.bukkit.event.Listener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.Door;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class minigameListenerClass implements Listener {
    static FileConfiguration clang = Main.getLang();
    static FileConfiguration config = Main.getPluginConfig();

    public void doorMenu(Player p, Location door) {
        Inventory inv = Bukkit.createInventory((InventoryHolder)null, 9, ChatColor.translateAlternateColorCodes('&', clang.getString("gui.doormenu")));
        Config c = new Config("DoorData", Main.getInstance());
        if (c.getConfig().getItemStack(door.toString() + ".lock") != null) {
            inv.setItem(0, c.getConfig().getItemStack(door.toString() + ".lock"));
        }

        if (c.getConfig().getItemStack(door.toString() + ".trap") != null) {
            inv.setItem(1, c.getConfig().getItemStack(door.toString() + ".trap"));
        }

        extreMethods.setRow(extreMethods.ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1, "", 0), inv, 2, 8);
        p.openInventory(inv);
    }

    @EventHandler
    public void onItemCraft(CraftItemEvent e) {
        ItemStack key = e.getCurrentItem();
        ItemMeta keymeta;
        if (config.getBoolean("invertid")) {
            if (key.getType().equals(Main.getLock()) && key.hasItemMeta()) {
                keymeta = key.getItemMeta();
                List<String> lore = keymeta.getLore();
                if (lore.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                    UUID uuid = UUID.randomUUID();
                    lore.set(1, "id: " + uuid.toString());
                    keymeta.setLore(lore);
                    key.setItemMeta(keymeta);
                    e.setCurrentItem(key);
                }
            }
        } else if (key.getType().equals(Main.getKey()) && key.hasItemMeta()) {
            keymeta = key.getItemMeta();
            UUID uuid = UUID.randomUUID();
            List<String> lore = keymeta.getLore();
            if (lore.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid")))) {
                lore.set(1, "id: " + uuid.toString());
                keymeta.setLore(lore);
                key.setItemMeta(keymeta);
                e.setCurrentItem(key);
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        Block clickedBlock = e.getBlock();
        Material blockType = clickedBlock.getType();
        ItemStack key = e.getPlayer().getInventory().getItemInMainHand();
        Config c = new Config("DoorData", Main.getInstance());
        if (!clickedBlock.getType().name().endsWith("DOOR") && clickedBlock.getRelative(BlockFace.UP).getType().name().endsWith("DOOR") && c.getConfig().isSet(clickedBlock.getRelative(BlockFace.UP).getLocation().toString())) {
            e.setCancelled(true);
        } else {
            if (!blockType.equals(Material.CHEST) && !blockType.name().endsWith("SHULKER_BOX")) {
                if (blockType.name().endsWith("DOOR")) {
                    if (!blockType.name().contains("TRAPDOOR")) {
                        Door door = (Door)clickedBlock.getState().getBlockData();
                        if (door.getHalf().equals(Half.TOP)) {
                            clickedBlock = clickedBlock.getRelative(BlockFace.DOWN);
                        }
                    }

                    if (c.getConfig().isSet(clickedBlock.getLocation().toString())) {
                        ItemStack lock;
                        if (p.hasPermission("lockpick.override")) {
                            lock = c.getConfig().getItemStack(clickedBlock.getLocation().toString() + ".lock");
                            ItemStack trap = c.getConfig().getItemStack(clickedBlock.getLocation().toString() + ".trap");
                            c.set(clickedBlock.getLocation().toString() + ".lock", (Object)null);
                            c.set(clickedBlock.getLocation().toString() + ".trap", (Object)null);
                            c.save();
                            if (lock != null) {
                                p.getWorld().dropItem(clickedBlock.getLocation(), lock);
                            }

                            if (trap != null) {
                                p.getWorld().dropItem(clickedBlock.getLocation(), trap);
                            }

                            return;
                        }

                        e.setCancelled(true);
                        lock = c.getConfig().getItemStack(clickedBlock.getLocation().toString() + ".lock");
                        if (key != null && key.hasItemMeta() && key.getItemMeta().hasLore() && ((String)key.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && ((String)key.getItemMeta().getLore().get(1)).equals(lock.getItemMeta().getLore().get(1))) {
                            e.setCancelled(false);
                            c.set(clickedBlock.getLocation().toString() + ".lock", (Object)null);
                            c.save();
                            if (lock != null) {
                                p.getWorld().dropItem(clickedBlock.getLocation(), lock);
                            }

                            return;
                        }

                        if (config.isSet("sound.denybreak.sound")) {
                            p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.denybreak.sound")), (float)config.getDouble("sound.denybreak.volume"), (float)config.getDouble("sound.denybreak.pitch"));
                        }
                    } else {
                        e.setCancelled(false);
                    }
                }
            } else {
                if (p.hasPermission("lockpick.override")) {
                    return;
                }

                if (clickedBlock.getState() instanceof Container) {
                    Container con = (Container)clickedBlock.getState();
                    String name = con.getCustomName();
                    if (name != null && (name.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.permlootbox"))) || name.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.lootbox"))))) {
                        if (config.isSet("sound.denybreak.sound")) {
                            p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.denybreak.sound")), (float)config.getDouble("sound.denybreak.volume"), (float)config.getDouble("sound.denybreak.pitch"));
                        }

                        e.setCancelled(true);
                        return;
                    }

                    if (con.getInventory().getItem(0) != null) {
                        ItemStack lock = con.getInventory().getItem(0);
                        if (lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                            e.setCancelled(true);
                            if (key != null && key.hasItemMeta() && key.getItemMeta().hasLore() && ((String)key.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && ((String)key.getItemMeta().getLore().get(1)).equals(lock.getItemMeta().getLore().get(1))) {
                                e.setCancelled(false);
                                return;
                            }

                            if (config.isSet("sound.denybreak.sound")) {
                                p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.denybreak.sound")), (float)config.getDouble("sound.denybreak.volume"), (float)config.getDouble("sound.denybreak.pitch"));
                            }

                            return;
                        }
                    }

                    e.setCancelled(false);
                }
            }

        }
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onMinecartClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof StorageMinecart) {
            StorageMinecart cart = (StorageMinecart)e.getRightClicked();
            if (cart.getLootTable() != null && config.getBoolean("lootbox.world.natural")) {
                e.setCancelled(true);
                if (cart.getCustomName() == null) {
                    ConfigurationSection cf = config.getConfigurationSection("lootbox.world");
                    Random ran = new Random();
                    int weak = cf.getInt("weak");
                    int normal = cf.getInt("normal") + weak;
                    int tough = cf.getInt("tough") + normal;
                    int strong = cf.getInt("strong") + tough;
                    int fine = cf.getInt("fine") + strong;
                    int r = ran.nextInt(100);
                    int tier = 1;
                    if (r > weak) {
                        if (r < normal) {
                            tier = 2;
                        } else if (r < tough) {
                            tier = 3;
                        } else if (r < strong) {
                            tier = 4;
                        } else if (r < fine) {
                            tier = 5;
                        }
                    }

                    cart.setCustomName("" + tier);
                }

                Player p = e.getPlayer();
                Container con = (Container)cart;
                database.setPlayerChest(p, con);
                Minigame.openMinigame(p, Integer.parseInt(cart.getCustomName()));
                return;
            }
        }

    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onBlockClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getHand().equals(EquipmentSlot.OFF_HAND)) {
                return;
            }

            if (e.isBlockInHand() && p.isSneaking()) {
                return;
            }

            Block clickedBlock = e.getClickedBlock();
            Material blockType = clickedBlock.getType();
            String tier;
            int tierint;
            if (!blockType.equals(Material.CHEST) && !blockType.name().endsWith("SHULKER_BOX")) {
                if (blockType.name().endsWith("DOOR")) {
                    if (!blockType.name().contains("TRAPDOOR")) {
                        Door door = (Door)clickedBlock.getState().getBlockData();
                        if (door.getHalf().equals(Half.TOP)) {
                            clickedBlock = clickedBlock.getRelative(BlockFace.DOWN);
                        }
                    }

                    Config c = new Config("DoorData", Main.getInstance());
                    if (c.getConfig().isSet(clickedBlock.getLocation().toString() + ".lock")) {
                        if (p.hasPermission("lockpick.override")) {
                            if (p.isSneaking()) {
                                e.setCancelled(false);
                                database.setPlayerDoor(p, clickedBlock);
                                this.doorMenu(p, clickedBlock.getLocation());
                            }

                            return;
                        }

                        ItemStack lock = c.getConfig().getItemStack(clickedBlock.getLocation().toString() + ".lock");
                        if (((Openable)clickedBlock.getBlockData()).isOpen()) {
                            if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && ((String)e.getItem().getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && ((String)e.getItem().getItemMeta().getLore().get(1)).equals(lock.getItemMeta().getLore().get(1))) {
                                c.set(clickedBlock.getLocation().toString() + ".locked", true);
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("message.locked")));
                                c.save();
                                return;
                            }
                        } else if (c.getConfig().isSet(clickedBlock.getLocation().toString() + ".locked") && c.getConfig().getBoolean(clickedBlock.getLocation().toString() + ".locked")) {
                            e.setCancelled(true);
                            if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && ((String)e.getItem().getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && ((String)e.getItem().getItemMeta().getLore().get(1)).equals(lock.getItemMeta().getLore().get(1))) {
                                if (config.isSet("sound.openlockkey.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.openlockkey.sound")), (float)config.getDouble("sound.openlockkey.volume"), (float)config.getDouble("sound.openlockkey.pitch"));
                                }

                                if (p.isSneaking()) {
                                    database.setPlayerDoor(p, clickedBlock);
                                    this.doorMenu(p, clickedBlock.getLocation());
                                } else {
                                    e.setCancelled(false);
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("message.unlocked")));
                                    c.set(clickedBlock.getLocation().toString() + ".locked", false);
                                    c.save();
                                }

                                return;
                            }

                            if (p.hasPermission("lockpick.pick")) {
                                if (!extreMethods.hasLockpick(p.getInventory())) {
                                    if (config.isSet("sound.nolockpick.sound")) {
                                        p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.nolockpick.sound")), (float)config.getDouble("sound.nolockpick.volume"), (float)config.getDouble("sound.nolockpick.pitch"));
                                    }

                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("message.nolockpick")));
                                    return;
                                }

                                tier = ((String)lock.getItemMeta().getLore().get(0)).replace(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")), "").replace(" ", "");
                                tierint = Integer.parseInt(tier);
                                if (tierint <= 5) {
                                    database.setPlayerDoor(p, clickedBlock);
                                    Minigame.openMinigame(p, tierint);
                                } else {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("message.unpickable")));
                                }
                            }
                        }
                    } else if (p.isSneaking()) {
                        this.doorMenu(p, clickedBlock.getLocation());
                        database.setPlayerDoor(p, clickedBlock);
                        e.setCancelled(true);
                    } else {
                        e.setCancelled(false);
                    }
                }
            } else {
                if (p.hasPermission("lockpick.override")) {
                    e.setCancelled(false);
                    return;
                }

                e.setCancelled(true);
                if (clickedBlock.getState() instanceof Container) {
                    Container con = (Container)clickedBlock.getState();
                    ConfigurationSection cf = config.getConfigurationSection("lootbox.world");
                    int tierint1;
                    if (cf.getBoolean("natural") && blockType.equals(Material.CHEST)) {
                        Chest chest = (Chest)clickedBlock.getState();
                        if (chest.getLootTable() != null) {
                            if (chest.getCustomName() == null) {
                                Random ran = new Random();
                                tierint1 = cf.getInt("weak");
                                int normal = cf.getInt("normal") + tierint1;
                                int tough = cf.getInt("tough") + normal;
                                int strong = cf.getInt("strong") + tough;
                                int fine = cf.getInt("fine") + strong;
                                int r = ran.nextInt(100);
                                int tier1 = 1;
                                if (r > tierint1) {
                                    if (r < normal) {
                                        tier1 = 2;
                                    } else if (r < tough) {
                                        tier1 = 3;
                                    } else if (r < strong) {
                                        tier1 = 4;
                                    } else if (r < fine) {
                                        tier1 = 5;
                                    }
                                }

                                chest.setCustomName("" + tier1);
                                chest.update();
                            }

                            con = (Container)clickedBlock.getState();
                            database.setPlayerChest(p, con);
                            Minigame.openMinigame(p, Integer.parseInt(chest.getCustomName()));
                            return;
                        }
                    }

                    if (con.getCustomName() != null) {
                        tier = con.getCustomName();
                        if (tier.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.permlootbox")))) {
                            tierint = Integer.parseInt(tier.replace(ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.permlootbox")), ""));
                            if (tierint <= 5 && database.canOpenLootbox(p, clickedBlock.getLocation().toString())) {
                                database.setLastLootbox(p, clickedBlock.getLocation().toString(), System.currentTimeMillis());
                                Minigame.openMinigame(p, tierint);
                                if (config.isSet("sound.openlootbox.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.openlootbox.sound")), (float)config.getDouble("sound.openlootbox.volume"), (float)config.getDouble("sound.openlootbox.pitch"));
                                }
                            }

                            return;
                        }

                        if (tier.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.lootbox")))) {
                            tierint = Integer.parseInt(tier.replace(ChatColor.translateAlternateColorCodes('&', clang.getString("lootbox.lootbox")), ""));
                            if (tierint <= 5) {
                                Minigame.openMinigame(p, tierint);
                                clickedBlock.setType(Material.AIR);
                                if (config.isSet("sound.openlootbox.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.openlootbox.sound")), (float)config.getDouble("sound.openlootbox.volume"), (float)config.getDouble("sound.openlootbox.pitch"));
                                }
                            }

                            return;
                        }
                    }

                    if (con.getInventory().getItem(0) != null) {
                        ItemStack lock = con.getInventory().getItem(0);
                        if (lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                            if (e.getItem() != null && e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore() && ((String)e.getItem().getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && ((String)e.getItem().getItemMeta().getLore().get(1)).equals(lock.getItemMeta().getLore().get(1))) {
                                if (config.isSet("sound.openlockkey.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.openlockkey.sound")), (float)config.getDouble("sound.openlockkey.volume"), (float)config.getDouble("sound.openlockkey.pitch"));
                                }

                                e.setCancelled(false);
                                return;
                            }

                            if (p.hasPermission("lockpick.pick")) {
                                if (!extreMethods.hasLockpick(p.getInventory())) {
                                    if (config.isSet("sound.nolockpick.sound")) {
                                        p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.nolockpick.sound")), (float)config.getDouble("sound.nolockpick.volume"), (float)config.getDouble("sound.nolockpick.pitch"));
                                    }

                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("message.nolockpick")));
                                    return;
                                }

                                tier = ((String)lock.getItemMeta().getLore().get(0)).replace(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")), "");
                                tier.replaceAll(" ", "");
                                tierint = Integer.parseInt(tier);
                                if (tierint <= 5) {
                                    database.setPlayerChest(p, con);
                                    Minigame.openMinigame(p, tierint);
                                } else {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("message.unpickable")));
                                }

                                return;
                            }
                        }
                    }

                    e.setCancelled(false);
                }
            }
        }

    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Inventory inv = e.getInventory();
        String name = e.getView().getTitle();
        Player p = (Player)e.getPlayer();
        database.setPlayerChest(p, (Container)null);
        if (name.equals(ChatColor.translateAlternateColorCodes('&', clang.getString("gui.doormenu")))) {
            ItemStack lock = inv.getItem(0);
            ItemStack trap = inv.getItem(1);
            Config c = new Config("DoorData", Main.getInstance());
            if (lock != null) {
                if (lock.hasItemMeta() && lock.getItemMeta().hasLore() && ((String)lock.getItemMeta().getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid")))) {
                    c.set(database.getPlayerDoor(p).toString() + ".lock", lock);
                    c.set(database.getPlayerDoor(p).toString() + ".trap", trap);
                    c.save();
                    database.setPlayerDoor(p, (Block)null);
                }
            } else {
                c.set(database.getPlayerDoor(p).toString() + ".lock", (Object)null);
                c.set(database.getPlayerDoor(p).toString() + ".trap", trap);
                c.save();
                database.setPlayerDoor(p, (Block)null);
            }
        }

    }

    @EventHandler
    public void invDragEvent(InventoryDragEvent e) {
        String name = e.getView().getTitle();
        if ((name.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("minigame.name"))) || name.equals(ChatColor.translateAlternateColorCodes('&', clang.getString("gui.doormenu"))) || name.equals("§6Loot Boxes")) && e.getInventory().getType() != InventoryType.PLAYER) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void invClickEvent(InventoryClickEvent e) {
        if (e.getClickedInventory() != null) {
            Inventory inv = e.getClickedInventory();
            Player p = (Player)e.getWhoClicked();
            if (e.getCurrentItem() != null && e.getCurrentItem().hasItemMeta() && e.getCurrentItem().getItemMeta().hasLore() && e.getCurrentItem().getItemMeta().getLore().contains(ChatColor.translateAlternateColorCodes('&', clang.getString("tags.unstealable"))) && !p.hasPermission("lockpick.move")) {
                e.setCancelled(true);
            }

            InventoryView view = e.getView();
            if (view.getTitle() != null) {
                String name = view.getTitle();
                if (name.equals(ChatColor.translateAlternateColorCodes('&', clang.getString("gui.doormenu")))) {
                    if (e.getClickedInventory().getType() != InventoryType.PLAYER && e.getSlot() >= 2) {
                        e.setCancelled(true);
                    }
                } else if (name.contains(ChatColor.translateAlternateColorCodes('&', clang.getString("minigame.name"))) && e.getClickedInventory().getType() != InventoryType.PLAYER) {
                    e.setCancelled(true);
                    int slot = e.getSlot();
                    int tier = Integer.parseInt(name.replace(ChatColor.translateAlternateColorCodes('&', clang.getString("minigame.name")), ""));
                    ItemStack cylind = extreMethods.ItemStack(Main.getCylinder(), 1, ChatColor.translateAlternateColorCodes('&', clang.getString("minigame.ironcylind")), Main.getCylinderDur());
                    if (extreMethods.hasLockpick(p.getInventory())) {
                        ArrayList<Integer> sequnze = database.getPlayerSequnze(p);
                        int sequnzeInt = database.getPlayerSequnzeInt(p);
                        int correctSlot = (Integer)sequnze.get(sequnzeInt);
                        int move = correctSlot + 9;
                        if (slot == correctSlot) {
                            if (config.isSet("sound.correctcylinder.sound")) {
                                p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.correctcylinder.sound")), (float)config.getDouble("sound.correctcylinder.volume"), (float)config.getDouble("sound.correctcylinder.pitch"));
                            }

                            if (inv.getItem(move) != null) {
                                move = correctSlot - 9;
                            }

                            inv.setItem(correctSlot, (ItemStack)null);
                            inv.setItem(move, cylind);
                            ++sequnzeInt;
                            if (sequnzeInt >= extreMethods.getNumberOfItems(cylind.getType(), inv)) {
                                if (config.isSet("sound.successe.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.successe.sound")), (float)config.getDouble("sound.successe.volume"), (float)config.getDouble("sound.successe.pitch"));
                                }

                                if (database.getPlayerChest(p) != null) {
                                    p.openInventory(database.getPlayerChest(p).getInventory());
                                    database.setPlayerChest(p, (Container)null);
                                } else if (database.getPlayerDoor(p) != null) {
                                    if (config.getBoolean("door.steal")) {
                                        this.doorMenu(p, database.getPlayerDoor(p));
                                    } else {
                                        p.closeInventory();
                                    }

                                    final Block bdoor = database.getPlayerDoor(p).getBlock();
                                    final Openable door = (Openable)bdoor.getBlockData();
                                    door.setOpen(true);
                                    database.getPlayerDoor(p).getBlock().setBlockData(door);
                                    if (config.getInt("door.autoclose") > 0) {
                                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                                            public void run() {
                                                door.setOpen(false);
                                                bdoor.setBlockData(door);
                                            }
                                        }, (long)(config.getInt("door.autoclose") * 20));
                                    }
                                } else {
                                    p.openInventory(database.openLootbox(tier, true));
                                }
                            } else {
                                database.setPlayerSequnzeInt(p, sequnzeInt);
                            }
                        } else {
                            FileConfiguration c = Main.getPluginConfig();
                            Random chance = new Random();
                            int chanceValue = chance.nextInt(100) + 1;
                            if (chanceValue < c.getInt("tierbreakstart") + tier * c.getInt("tierbreaklevel")) {
                                if (config.isSet("sound.lockpickbreak.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.lockpickbreak.sound")), (float)config.getDouble("sound.lockpickbreak.volume"), (float)config.getDouble("sound.lockpickbreak.pitch"));
                                }

                                extreMethods.removeLockpick(p.getInventory());
                            }

                            chanceValue = chance.nextInt(100) + 1;
                            if (chanceValue < c.getInt("tierkickstart") + tier * c.getInt("tierkicklevel")) {
                                if (config.isSet("sound.fail.sound")) {
                                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.fail.sound")), (float)config.getDouble("sound.fail.volume"), (float)config.getDouble("sound.fail.pitch"));
                                }

                                p.closeInventory();
                                ItemStack trap = null;
                                Block block = null;
                                if (database.getPlayerChest(p) != null) {
                                    Container con = database.getPlayerChest(p);
                                    if (!con.getCustomName().equals("1") && !con.getCustomName().equals("2") && !con.getCustomName().equals("3") && !con.getCustomName().equals("4") && !con.getCustomName().equals("5")) {
                                        trap = con.getInventory().getItem(1);
                                        block = con.getBlock();
                                    }

                                    database.setPlayerChest(p, (Container)null);
                                } else if (database.getPlayerDoor(p) != null) {
                                    block = database.getPlayerDoor(p).getBlock();
                                    Config tc = new Config("DoorData", Main.getInstance());
                                    if (tc.getConfig().isSet(database.getPlayerDoor(p).toString() + ".trap")) {
                                        trap = tc.getConfig().getItemStack(database.getPlayerDoor(p).toString() + ".trap");
                                    }

                                    database.setPlayerDoor(p, (Block)null);
                                }

                                if (trap != null) {
                                    TrapManager.activatTrap(trap, p, block);
                                }
                            }
                        }
                    }
                }
            }

            if (e.getCursor() != null && e.getCurrentItem() != null) {
                ItemStack cursorItem = e.getCursor();
                ItemStack currentItem = e.getCurrentItem();
                if (cursorItem.hasItemMeta() && cursorItem.getItemMeta().hasLore()) {
                    ItemMeta cursorMeta = cursorItem.getItemMeta();
                    if (((String)cursorMeta.getLore().get(0)).equals(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid"))) && currentItem.hasItemMeta() && currentItem.getItemMeta().hasLore()) {
                        ItemMeta currentMeta = currentItem.getItemMeta();
                        if (((String)currentMeta.getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.difficultyloreid"))) || ((String)currentMeta.getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.keyloreid")))) {
                            List lore;
                            if (config.getBoolean("invertid")) {
                                e.setCancelled(true);
                                lore = cursorMeta.getLore();
                                lore.set(1, (String)currentMeta.getLore().get(1));
                                cursorMeta.setLore(lore);
                                cursorItem.setItemMeta(cursorMeta);
                                e.setCursor(cursorItem);
                            } else {
                                e.setCancelled(true);
                                lore = currentMeta.getLore();
                                lore.set(1, (String)cursorMeta.getLore().get(1));
                                currentMeta.setLore(lore);
                                currentItem.setItemMeta(currentMeta);
                                e.setCurrentItem(currentItem);
                            }

                            if (config.isSet("sound.linklock.sound")) {
                                p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.linklock.sound")), (float)config.getDouble("sound.linklock.volume"), (float)config.getDouble("sound.linklock.pitch"));
                            }
                        }
                    }
                }
            }
        }

    }
}
