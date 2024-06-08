package me.sialim.keysandlockpicks.traps;

import me.sialim.keysandlockpicks.main.Main;
import me.sialim.keysandlockpicks.main.extreMethods;
import org.bukkit.event.Listener;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;

public class PotionTrap implements Listener {
    private static FileConfiguration clang = Main.getLang();
    private static FileConfiguration config = Main.getPluginConfig();

    public static void potionTrap(PotionType type, int str, boolean splash, Player p) {
        PotionEffect effect = new PotionEffect(type.getEffectType(), 20 * config.getInt("potiontrap.time"), str);
        if (splash) {
            ItemStack itemStack = new ItemStack(Material.SPLASH_POTION);
            PotionMeta potionMeta = (PotionMeta)itemStack.getItemMeta();
            potionMeta.addCustomEffect(effect, true);
            itemStack.setItemMeta(potionMeta);
            ThrownPotion thrownPotion = (ThrownPotion)p.getWorld().spawnEntity(p.getLocation(), EntityType.SPLASH_POTION);
            thrownPotion.setItem(itemStack);
        } else {
            p.addPotionEffect(effect);
        }

    }

    @EventHandler
    public void onTrapClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() == Action.RIGHT_CLICK_AIR && e.getItem().hasItemMeta()) {
            ItemMeta meta = e.getItem().getItemMeta();
            if (meta.hasLore() && ((String)meta.getLore().get(0)).equals(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontraploreid")))) {
                p.getInventory().setItemInHand(extreMethods.ItemStack(Main.getTrap(), 1, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrap")), Main.getTrapDur(), ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontraploreid")), ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontraptype")), ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapsplash")), ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges"))));
                if (config.isSet("sound.potiontrapempty.sound")) {
                    p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.potiontrapempty.sound")), (float)config.getDouble("sound.potiontrapempty.volume"), (float)config.getDouble("sound.potiontrapempty.pitch"));
                }
            }
        }

    }

    @EventHandler
    public void invClickEvent(InventoryClickEvent e) throws NumberFormatException {
        Player p = (Player)e.getWhoClicked();
        if (e.getCursor() != null && e.getCurrentItem() != null) {
            ItemStack cursorItem = e.getCursor();
            ItemStack currentItem = e.getCurrentItem();
            if (cursorItem.hasItemMeta() && cursorItem.getItemMeta() instanceof PotionMeta) {
                PotionMeta cursorMeta = (PotionMeta)cursorItem.getItemMeta();
                if (currentItem.hasItemMeta() && currentItem.getItemMeta().hasLore()) {
                    ItemMeta currentMeta = currentItem.getItemMeta();
                    if (((String)currentMeta.getLore().get(0)).contains(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontraploreid")))) {
                        PotionData data = cursorMeta.getBasePotionData();
                        if (!data.getType().equals(PotionType.INSTANT_DAMAGE) && !data.getType().equals(PotionType.INSTANT_HEAL)) {
                            e.setCancelled(true);
                            List<String> lore = currentMeta.getLore();
                            int newstr = 1;
                            if (data.isUpgraded()) {
                                newstr = 2;
                            }

                            double dura = 3600.0D * data.getType().getEffectType().getDurationModifier() / (double)newstr;
                            if (data.isExtended()) {
                                dura *= 2.6666666666666665D;
                            }

                            int newCharges = (int)Math.round(dura / 20.0D / (double)config.getInt("potiontrap.time"));

                            try {
                                int oldCharges = Integer.parseInt(((String)lore.get(3)).replace(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges")), ""));
                                String[] typelore = ((String)lore.get(1)).replace(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontraptype")), "").split(":");
                                PotionType type = PotionType.valueOf(typelore[0]);
                                int oldstr = Integer.parseInt(typelore[1]);
                                if (!data.getType().equals(type)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("potion.dontmatch")) + 3);
                                    return;
                                }

                                if (newstr != oldstr) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("potion.dontmatch")) + 2);
                                    return;
                                }

                                Material splash = Material.POTION;
                                if (((String)lore.get(2)).replace(ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapsplash")), "").equals("true")) {
                                    splash = Material.SPLASH_POTION;
                                }

                                if (!cursorItem.getType().equals(splash)) {
                                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', clang.getString("potion.dontmatch")) + 1);
                                    return;
                                }

                                if (oldCharges + newCharges >= config.getInt("potiontrap.maxcharges")) {
                                    lore.set(3, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges")) + config.getInt("potiontrap.maxcharges"));
                                } else {
                                    lore.set(3, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges")) + (oldCharges + newCharges));
                                }
                            } catch (NullPointerException | IllegalArgumentException var18) {
                                lore.set(1, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontraptype") + data.getType().toString() + ":" + newstr));
                                String splash = "false";
                                if (cursorItem.getType().equals(Material.SPLASH_POTION)) {
                                    splash = "true";
                                }

                                lore.set(2, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapsplash") + splash));
                                lore.set(3, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges")) + config.getInt("potiontrap.maxcharges"));
                                if (newCharges >= config.getInt("potiontrap.maxcharges")) {
                                    lore.set(3, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges")) + config.getInt("potiontrap.maxcharges"));
                                } else {
                                    lore.set(3, ChatColor.translateAlternateColorCodes('&', clang.getString("crafting.potiontrapcharges")) + newCharges);
                                }
                            }

                            currentMeta.setLore(lore);
                            currentItem.setItemMeta(currentMeta);
                            e.getCursor().setAmount(0);
                            if (config.isSet("sound.potiontrapfill.sound")) {
                                p.playSound(p.getLocation(), Sound.valueOf(config.getString("sound.potiontrapfill.sound")), (float)config.getDouble("sound.potiontrapfill.volume"), (float)config.getDouble("sound.potiontrapfill.pitch"));
                            }
                        }
                    }
                }
            }
        }

    }
}
