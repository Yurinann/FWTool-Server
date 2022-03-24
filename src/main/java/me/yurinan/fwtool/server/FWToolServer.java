package me.yurinan.fwtool.server;

import me.yurinan.fwtool.server.configurations.ConfigListener;
import me.yurinan.fwtool.server.configurations.FileConfig;
import me.yurinan.fwtool.server.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Yurinan
 * @since 2022/3/23 20:37
 */

public final class FWToolServer extends JavaPlugin {

    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        log("&f插件开始加载...");
        long startTime = System.currentTimeMillis();
        log("&f加载配置文件...");
        FileConfig.initConfig();
        log("&f初始化监听程序...");
        ConfigListener configListener = new ConfigListener();
        configListener.watch();
        log("&3插件加载完成, 共耗时 &b" + (System.currentTimeMillis() - startTime) + " &3ms.");
    }

    @Override
    public void onDisable() {
        log("&f插件开始卸载...");
        long closeTime = System.currentTimeMillis();
        log("&f保存配置文件...");
        FileConfig.saveConfig();
        log("&3插件卸载完成, 共耗时 &b" + (System.currentTimeMillis() - closeTime) + " &3ms.");
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(ColorParser.parse("&8[&b" + instance.getName() + "&8] " + message));
    }

    public static void warn(String message) {
        log("&e警告! &r" + message);
    }

    public static void error(String message) {
        log("&c错误! &r" + message);
    }

}
