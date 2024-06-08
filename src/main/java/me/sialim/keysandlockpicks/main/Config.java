package me.sialim.keysandlockpicks.main;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class Config {
    private final Plugin PLUGIN;
    private final String FILENAME;
    private final File FOLDER;
    private FileConfiguration config;
    private File configFile;

    public Config(String filename, Main instance) {
        if (!filename.endsWith(".yml")) {
            filename = filename + ".yml";
        }

        this.FILENAME = filename;
        this.PLUGIN = instance;
        this.FOLDER = Main.getInstance().getDataFolder();
        this.config = null;
        this.configFile = null;
        this.reload();
    }

    public Config(File folder, String filename, Main instance) {
        if (!filename.endsWith(".yml")) {
            filename = filename + ".yml";
        }

        this.FILENAME = filename;
        this.PLUGIN = instance;
        this.FOLDER = folder;
        this.config = null;
        this.configFile = null;
        this.reload();
    }

    public FileConfiguration getConfig() {
        if (this.config == null) {
            this.reload();
        }

        return this.config;
    }

    public void reload() {
        if (!this.FOLDER.exists()) {
            try {
                if (this.FOLDER.mkdir()) {
                    this.PLUGIN.getLogger().log(Level.INFO, "Folder " + this.FOLDER.getName() + " created.");
                } else {
                    this.PLUGIN.getLogger().log(Level.WARNING, "Unable to create folder " + this.FOLDER.getName() + ".");
                }
            } catch (Exception var3) {
            }
        }

        this.configFile = new File(this.FOLDER, this.FILENAME);
        if (!this.configFile.exists()) {
            try {
                this.configFile.createNewFile();
            } catch (IOException var2) {
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.configFile);
    }

    public void saveDefaultConfig() {
        if (this.configFile == null) {
            this.configFile = new File(this.PLUGIN.getDataFolder(), this.FILENAME);
        }

        if (!this.configFile.exists()) {
            this.PLUGIN.saveResource(this.FILENAME, false);
        }

    }

    public void save() {
        if (this.config != null && this.configFile != null) {
            try {
                this.getConfig().save(this.configFile);
            } catch (IOException var2) {
                this.PLUGIN.getLogger().log(Level.WARNING, "Could not save config to " + this.configFile.getName(), var2);
            }

        }
    }

    public void set(String path, Object o) {
        this.getConfig().set(path, o);
    }
}
