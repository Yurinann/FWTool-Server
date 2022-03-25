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

    public static File terminalDataFile = new File(getDataFolder() + "/terminal-data.yml");

    public static void initConfig() {
        if (!getDataFolder().exists()) {
            FWToolBungee.log("&8插件数据文件夹不存在, 创建中...");
            getDataFolder().mkdir();
        }
        if (!terminalDataFile.exists()) {
            FWToolBungee.log("&8" + terminalDataFile.getName() + " 不存在, 创建中...");
            try {
                terminalDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @NotNull
    public static Configuration getConfig(File file) {
        if (config == null) {
            reloadConfig(file);
        }
        return config;
    }

    public static void reloadConfig(File file) {
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            FWToolBungee.warn("&8加载配置文件 &f" + file.getName() + " &8时发生错误!\n" + e);
        }
    }

    public static void saveConfig(Configuration config, File file) {
        if (file.exists()) {
            try {
                ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
            } catch (IOException e) {
                FWToolBungee.warn("&8加载配置文件 &f" + file.getName() + " &8时发生错误!\n" + e);
            }
        } else {
            FWToolBungee.warn("&3正在保存的配置文件 &f" + file.getName() + " &3不存在!");
        }
    }

    public static File getDataFolder() {
        return FWToolBungee.getInstance().getDataFolder();
    }

}
