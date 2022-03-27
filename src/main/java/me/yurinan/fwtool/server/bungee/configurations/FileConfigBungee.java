package me.yurinan.fwtool.server.bungee.configurations;

import me.yurinan.fwtool.server.bungee.FWToolBungee;
import net.md_5.bungee.api.config.ListenerInfo;
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
    public static final File serverInfoFile = new File(getDataFolder() + "/server-info.yml");

    public static void initConfig() {
        if (!getDataFolder().exists()) {
            FWToolBungee.log("&8插件数据文件夹不存在, 创建中...");
            getDataFolder().mkdir();
        }

        if (!terminalDataFile.exists()) {
            FWToolBungee.log("&8" + terminalDataFile.getName() + " 不存在, 创建中...");
            try {
                terminalDataFile.createNewFile();
                reloadConfig(terminalDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FWToolBungee.log("&3数据写入中...");
            getConfig(terminalDataFile).set("dispatch-command", "");
            saveConfig(getConfig(terminalDataFile), terminalDataFile);
        } else {
            FWToolBungee.log("&3数据写入中...");
            if (!getConfig(terminalDataFile).contains("dispatch-command")) {
                getConfig(terminalDataFile).set("dispatch-command", "");
            }
            saveConfig(getConfig(terminalDataFile), terminalDataFile);
        }

        int port = ((ListenerInfo) FWToolBungee.getInstance().getProxy().getConfig().getListeners().toArray()[0]).getHost().getPort();
        if (!serverInfoFile.exists()) {
            FWToolBungee.log("&8" + serverInfoFile.getName() + " 不存在, 创建中...");
            try {
                serverInfoFile.createNewFile();
                reloadConfig(serverInfoFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            FWToolBungee.log("&3数据写入中...");
            getConfig(serverInfoFile).set("port", port);
        } else {
            FWToolBungee.log("&3数据写入中...");
            if (!getConfig(serverInfoFile).contains("port")) {
                getConfig(serverInfoFile).set("port", port);
            } else {
                if (getConfig(serverInfoFile).getInt("port") != port) {
                    getConfig(serverInfoFile).set("port", port);
                }
            }
        }
        saveConfig(getConfig(serverInfoFile), serverInfoFile);
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
