package me.yurinan.fwtool.server.bungee.configurations;

import me.yurinan.fwtool.server.bungee.FWToolBungee;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Yurinan
 * @since 2022/3/24 14:07
 */

public class FileConfigBungee {

    private static Configuration config = null;

    public static File configFile = new File(getDataFolder() + "/config.yml");

    public static void initConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @NotNull
    public static Configuration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public static void reloadConfig() {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            FWToolBungee.warn("&3加载配置文件 &f" + configFile.getName() + " &3时发生错误!" + e);
        }
    }

    public static void saveConfig(Configuration config, File file) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            FWToolBungee.warn("&3加载配置文件 &f" + configFile.getName() + " &3时发生错误!" + e);
        }
    }

    public static File getDataFolder() {
        return FWToolBungee.getInstance().getDataFolder();
    }

}
