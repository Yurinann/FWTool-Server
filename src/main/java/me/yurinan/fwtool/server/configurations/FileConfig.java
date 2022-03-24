package me.yurinan.fwtool.server.configurations;

import me.yurinan.fwtool.server.FWToolServer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author Yurinan
 * @since 2022/3/23 20:40
 */

public class FileConfig {

    private static FileConfiguration config = null;

    public static final File dataFolder = FWToolServer.instance.getDataFolder();
    public static final File configFile = new File(dataFolder + "/config.yml");

    public static void initConfig() {
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NotNull
    public static FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = FWToolServer.instance.getResource("config.yml");
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public static void saveConfig() {
        try {
            getConfig().save("config.yml");
        } catch (IOException e) {
            FWToolServer.warn("Could not save config to" + configFile + e);
        }
    }

}
