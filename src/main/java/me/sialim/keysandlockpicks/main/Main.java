package me.sialim.keysandlockpicks.main;

import java.io.File;
import java.util.Iterator;
import java.util.Random;

import me.sialim.keysandlockpicks.listerner.ExplosionListerner;
import me.sialim.keysandlockpicks.listerner.GuiListerner;
import me.sialim.keysandlockpicks.listerner.WorldListerner;
import me.sialim.keysandlockpicks.lockpickminigame.database;
import me.sialim.keysandlockpicks.lockpickminigame.minigameListenerClass;
import me.sialim.keysandlockpicks.traps.PotionTrap;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    private static Main instance;
    private static FileConfiguration clang;
    private static FileConfiguration config;
    private static FileConfiguration crafting;
    private static Material lockMaterial;
    private static Material trapMaterial;
    private static Material lockpickMaterial;
    private static Material keyMaterial;
    private static Material cylinderMaterial;
    private static Material guideMaterial;
    private static Material barMaterial;
    private static int lockDur;
    private static int trapDur;
    private static int lockpickDur;
    private static int keyDur;
    private static int cylinderDur;
    private static int guideDur;
    private static int barDur;

    public void onEnable() {
        instance = this;
        this.getLogger().info("The Plugin is Loading");
        PluginManager pm = Bukkit.getServer().getPluginManager();
        this.createConfigs();
        Config c = new Config("config", getInstance());
        File folder = new File(getInstance().getDataFolder(), "language");
        Config clang = new Config(folder, c.getConfig().getString("language"), getInstance());
        Main.clang = clang.getConfig();
        config = c.getConfig();
        crafting = (new Config("crafting", getInstance())).getConfig();
        setupItems();
        pm.registerEvents(new minigameListenerClass(), this);
        pm.registerEvents(new WorldListerner(), this);
        pm.registerEvents(new GuiListerner(), this);
        pm.registerEvents(new PotionTrap(), this);
        if (!c.getConfig().getBoolean("explodable")) {
            pm.registerEvents(new ExplosionListerner(), this);
        }

        this.getCommand("lockpick").setExecutor(new CommandLockpick());
        craftingnew.setupRecipes(this);
        database.loadLootbox();
        final ConfigurationSection cf = config.getConfigurationSection("lootbox.world");
        if (cf.getBoolean("enabled")) {
            final Random ran = new Random();
            final int weak = cf.getInt("weak");
            final int normal = cf.getInt("normal") + weak;
            final int tough = cf.getInt("tough") + normal;
            final int strong = cf.getInt("strong") + tough;
            final int fine = cf.getInt("fine") + strong;
            final Config data = new Config("data", getInstance());

            String[] ls = new String[0];
            World world = null;
            int cx = 0;
            int cz = 0;
            int max = 0;
            World finalWorld = world;
            String[] finalLs = ls;
            int finalCz = cz;
            int finalCx = cx;
            int finalMax = max;
            for(Iterator var14 = cf.getStringList("worlds").iterator(); var14.hasNext(); Bukkit.getScheduler().runTaskTimer(getInstance(), new Runnable() {
                public void run() {
                    int current = data.getConfig().getInt(finalLs[0] + ".current");
                    if (finalMax < 1 || finalMax > current) {
                        ++current;
                        int x = ran.nextInt(cf.getInt("radius"));
                        int z = ran.nextInt(cf.getInt("radius"));
                        if (ran.nextBoolean()) {
                            x *= -1;
                        }

                        if (ran.nextBoolean()) {
                            z *= -1;
                        }

                        x += finalCx;
                        z += finalCz;
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

                        Block lootbox = finalWorld.getHighestBlockAt(x, z);
                        lootbox.setType(Material.CHEST);
                        Chest chest = (Chest)lootbox.getState();
                        chest.setCustomName("§2Loot Box: " + tier);
                        chest.update();
                        Main.getInstance().getLogger().info("x: " + x + " z: " + z + " tier: " + tier);
                        data.set(finalLs[0] + ".current", current);
                        data.save();
                    }

                }
            }, cf.getLong("time") * 1200L, cf.getLong("time") * 1200L)) {
                String s = (String)var14.next();
                ls = s.split(":");
                world = Bukkit.getServer().getWorld(ls[0]);
                cx = Integer.parseInt(ls[1]);
                cz = Integer.parseInt(ls[2]);
                if (ls.length == 4) {
                    max = Integer.parseInt(ls[3]);
                } else {
                    max = -1;
                }
            }
        }

        this.getLogger().info("Plugin has been started.");
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        HandlerList.unregisterAll(this);
        this.getServer().resetRecipes();
        this.getLogger().info("Plugin has shut down.");
    }

    public static void setupItems() {
        String[] item;
        if (config.getString("LockMaterial").contains(":")) {
            item = config.getString("LockMaterial").split(":");
            lockMaterial = Material.getMaterial(item[0]);
            lockDur = Integer.parseInt(item[1]);
        } else {
            lockMaterial = Material.getMaterial(config.getString("LockMaterial"));
            lockDur = 0;
        }

        if (config.getString("TrapMaterial").contains(":")) {
            item = config.getString("TrapMaterial").split(":");
            trapMaterial = Material.getMaterial(item[0]);
            trapDur = Integer.parseInt(item[1]);
        } else {
            trapMaterial = Material.getMaterial(config.getString("TrapMaterial"));
            trapDur = 0;
        }

        if (config.getString("LockpickMaterial").contains(":")) {
            item = config.getString("LockpickMaterial").split(":");
            lockpickMaterial = Material.getMaterial(item[0]);
            lockpickDur = Integer.parseInt(item[1]);
        } else {
            lockpickMaterial = Material.getMaterial(config.getString("LockpickMaterial"));
            lockpickDur = 0;
        }

        if (config.getString("KeyMaterial").contains(":")) {
            item = config.getString("KeyMaterial").split(":");
            keyMaterial = Material.getMaterial(item[0]);
            keyDur = Integer.parseInt(item[1]);
        } else {
            keyMaterial = Material.getMaterial(config.getString("KeyMaterial"));
            keyDur = 0;
        }

        if (config.getString("CylinderMaterial").contains(":")) {
            item = config.getString("CylinderMaterial").split(":");
            cylinderMaterial = Material.getMaterial(item[0]);
            cylinderDur = Integer.parseInt(item[1]);
        } else {
            cylinderMaterial = Material.getMaterial(config.getString("CylinderMaterial"));
            cylinderDur = 0;
        }

        if (config.getString("GuideMaterial").contains(":")) {
            item = config.getString("GuideMaterial").split(":");
            guideMaterial = Material.getMaterial(item[0]);
            guideDur = Integer.parseInt(item[1]);
        } else {
            guideMaterial = Material.getMaterial(config.getString("GuideMaterial"));
            guideDur = 0;
        }

        if (config.getString("BarMaterial").contains(":")) {
            item = config.getString("BarMaterial").split(":");
            barMaterial = Material.getMaterial(item[0]);
            barDur = Integer.parseInt(item[1]);
        } else {
            barMaterial = Material.getMaterial(config.getString("BarMaterial"));
            barDur = 0;
        }

    }

    public static Main getInstance() {
        return instance;
    }

    public static FileConfiguration getLang() {
        return clang;
    }

    public static FileConfiguration getPluginConfig() {
        return config;
    }

    public static FileConfiguration getCrafting() {
        return crafting;
    }

    public static Material getLock() {
        return lockMaterial;
    }

    public static Material getTrap() {
        return trapMaterial;
    }

    public static Material getLockpick() {
        return lockpickMaterial;
    }

    public static Material getKey() {
        return keyMaterial;
    }

    public static Material getCylinder() {
        return cylinderMaterial;
    }

    public static Material getGuide() {
        return guideMaterial;
    }

    public static Material getBar() {
        return barMaterial;
    }

    public static Material getLockMaterial() {
        return lockMaterial;
    }

    public static void setLockMaterial(Material lockMaterial) {
        Main.lockMaterial = lockMaterial;
    }

    public static Material getTrapMaterial() {
        return trapMaterial;
    }

    public static void setTrapMaterial(Material trapMaterial) {
        Main.trapMaterial = trapMaterial;
    }

    public static Material getLockpickMaterial() {
        return lockpickMaterial;
    }

    public static void setLockpickMaterial(Material lockpickMaterial) {
        Main.lockpickMaterial = lockpickMaterial;
    }

    public static int getLockDur() {
        return lockDur;
    }

    public static void setLockDur(int lockDur) {
        Main.lockDur = lockDur;
    }

    public static int getTrapDur() {
        return trapDur;
    }

    public static void setTrapDur(int trapDur) {
        Main.trapDur = trapDur;
    }

    public static int getLockpickDur() {
        return lockpickDur;
    }

    public static void setLockpickDur(int lockpickDur) {
        Main.lockpickDur = lockpickDur;
    }

    public static int getKeyDur() {
        return keyDur;
    }

    public static void setKeyDur(int keyDur) {
        Main.keyDur = keyDur;
    }

    public static int getCylinderDur() {
        return cylinderDur;
    }

    public static void setCylinderDur(int cylinderDur) {
        Main.cylinderDur = cylinderDur;
    }

    public static int getGuideDur() {
        return guideDur;
    }

    public static void setGuideDur(int guideDur) {
        Main.guideDur = guideDur;
    }

    public static int getBarDur() {
        return barDur;
    }

    public static void setBarDur(int barDur) {
        Main.barDur = barDur;
    }

    private void createConfigs() {
        File customConfigFile = new File(this.getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            this.getLogger().info("config.yml isn´t found, creating one");
            customConfigFile.getParentFile().mkdirs();
            this.saveResource("config.yml", false);
            this.getLogger().info("done");
        }

        File customConfigFile2 = new File(this.getDataFolder(), "crafting.yml");
        if (!customConfigFile2.exists()) {
            this.getLogger().info("crafting.yml isn´t found, creating one");
            customConfigFile2.getParentFile().mkdirs();
            this.saveResource("crafting.yml", false);
            this.getLogger().info("done");
        }

        File customConfigFile4 = new File(this.getDataFolder(), "loottable.yml");
        if (!customConfigFile4.exists()) {
            this.getLogger().info("loottable.yml isn´t found, creating one");
            customConfigFile4.getParentFile().mkdirs();
            this.saveResource("loottable.yml", false);
            this.getLogger().info("done");
        }

        File customConfigFile5 = new File(this.getDataFolder(), "language");
        if (!customConfigFile5.exists()) {
            this.getLogger().info("the folder language isn´t found, creating it");
            customConfigFile5.getParentFile().mkdirs();
        }

        this.saveResource("language/en.yml", false);
        this.saveResource("language/de.yml", false);
        this.getLogger().info("done");
    }
}
