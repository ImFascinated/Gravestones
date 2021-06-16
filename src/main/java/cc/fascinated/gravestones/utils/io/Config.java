package cc.fascinated.gravestones.utils.io;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Project: Gravestones
 * Created by Fascinated#4735 on 15/06/2021
 */

public class Config {

    private JavaPlugin plugin;
    private String configName;
    private String folder;

    private File configurationFile;
    private FileConfiguration configuration;

    public Config(JavaPlugin plugin, String configName, String folderName) {
        if (plugin == null) {
            throw new IllegalStateException("Plugin must not be null!");
        }
        this.plugin = plugin;
        this.configName = configName;
        folder = folderName;
        if (folder == null) {
            this.configurationFile = new File(plugin.getDataFolder(), configName);
        } else {
            this.configurationFile = new File(plugin.getDataFolder() + File.separator + folder, configName);
        }
        this.saveDefaultConfig();
    }

    public FileConfiguration getConfiguration() {
        if (configuration == null) {
            this.reloadConfig();
        }
        return configuration;
    }

    public File getFile() {
        return configurationFile;
    }

    public void reloadConfig() {
        configuration = YamlConfiguration.loadConfiguration(configurationFile);
        // Look for defaults in the jar
        InputStream defConfigStream = plugin.getResource(configName);
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream));
            configuration.setDefaults(defConfig);
        }
    }

    /**
     * configuration = FileConfiguration instance
     */
    public void saveConfig() {
        if (this.configuration != null && this.configurationFile != null) {
            try {
                this.getConfiguration().save(this.configurationFile);
            } catch (IOException ex) {
                this.plugin.getLogger().info("Configuration save failed!");
            }
        }
    }

    public void saveDefaultConfig() {
        if (!configurationFile.exists()) {
            this.plugin.saveResource(configName, false);
        }
    }

    public void deleteConfig() {
        configurationFile.delete();
    }
}