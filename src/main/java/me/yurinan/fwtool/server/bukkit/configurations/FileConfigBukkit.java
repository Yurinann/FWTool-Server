package me.yurinan.fwtool.server.bukkit.configurations;

import me.yurinan.fwtool.server.bukkit.FWToolBukkit;
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

public class FileConfigBukkit {

    private static FileConfiguration config = null;

    public static final File configFile = new File(getDataFolder() + "/config.yml");

    public static void initConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
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

        final InputStream defConfigStream = FWToolBukkit.instance.getResource("config.yml");

        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public static void saveConfig() {
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            FWToolBukkit.warn("&3加载配置文件 &f" + configFile.getName() + " &3时发生错误!" + e);
        }
    }

    public static File getDataFolder() {
        return FWToolBukkit.instance.getDataFolder();
    }

}
