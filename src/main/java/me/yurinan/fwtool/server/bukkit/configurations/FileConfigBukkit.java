package me.yurinan.fwtool.server.bukkit.configurations;

import me.yurinan.fwtool.server.bukkit.FWToolBukkit;
import org.bukkit.Bukkit;
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

    public static final File terminalDataFile = new File(getDataFolder() + "/terminal-data.yml");
    public static final File serverInfoFile = new File(getDataFolder() + "/server-info.yml");

    public static void initConfig() {
        if (!getDataFolder().exists()) {
            FWToolBukkit.log("&8插件数据文件夹不存在, 创建中...");
            getDataFolder().mkdir();
        }

        if (!terminalDataFile.exists()) {
            FWToolBukkit.log("&8" + terminalDataFile.getName() + " 文件不存在, 创建中...");
            try {
                terminalDataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FWToolBukkit.log("&3数据写入中...");
            getConfig(terminalDataFile).set("dispatch-command", "");
        } else {
            FWToolBukkit.log("&3数据写入中...");
            if (!getConfig(terminalDataFile).contains("dispatch-command")) {
                getConfig(terminalDataFile).set("dispatch-command", "");
            }
        }
        saveConfig(terminalDataFile);

        int port = Bukkit.getPort();
        if (!serverInfoFile.exists()) {
            FWToolBukkit.log("&8" + serverInfoFile.getName() + " 文件不存在, 创建中...");
            try {
                serverInfoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            FWToolBukkit.log("&3数据写入中...");
            getConfig(serverInfoFile).set("port", port);
        } else {
            FWToolBukkit.log("&3数据写入中...");
            if (getConfig(serverInfoFile).getInt("port") != port) {
                getConfig(serverInfoFile).set("port", port);
            }
        }
        saveConfig(serverInfoFile);
    }

    @NotNull
    public static FileConfiguration getConfig(File file) {
        if (config == null) {
            reloadConfig(file);
        }
        return config;
    }

    public static void reloadConfig(File file) {
        config = YamlConfiguration.loadConfiguration(file);

        final InputStream defConfigStream = FWToolBukkit.getInstance().getResource(file.getName());

        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public static void saveConfig(File file) {
        if (file.exists()) {
            try {
                getConfig(file).save(file);
            } catch (IOException e) {
                FWToolBukkit.warn("&3加载配置文件 &f" + file.getName() + " &3时发生错误!\n" + e);
            }
        } else {
            FWToolBukkit.warn("&3正在保存的配置文件 &f" + file.getName() + " &3不存在!");
        }
    }

    public static File getDataFolder() {
        return FWToolBukkit.getInstance().getDataFolder();
    }

}
