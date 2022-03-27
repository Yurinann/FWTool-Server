package me.yurinan.fwtool.server.bukkit;

import me.yurinan.fwtool.server.bukkit.bstats.Metrics;
import me.yurinan.fwtool.server.universal.FWToolServer;
import me.yurinan.fwtool.server.bukkit.configurations.ConfigListenerBukkit;
import me.yurinan.fwtool.server.bukkit.configurations.FileConfigBukkit;
import me.yurinan.fwtool.server.universal.utils.ColorParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author Yurinan
 * @since 2022/3/24 11:23
 */

public class FWToolBukkit extends JavaPlugin {

    private static FWToolBukkit instance;

    @Override
    public void onEnable() {
        instance = this;
        log("&f插件开始加载...");
        FWToolServer.bcMode = false;
        long startTime = System.currentTimeMillis();
        log("&f加载配置文件...");
        FileConfigBukkit.initConfig();
        log("&f初始化监听程序...");
        ConfigListenerBukkit configListener = new ConfigListenerBukkit();
        configListener.watch();
        log("&f注册 bStats 数据统计...");
        int pluginId = 14735;
        Metrics metrics = new Metrics(this, pluginId);
        log("&3插件加载完成, 共耗时 &b" + (System.currentTimeMillis() - startTime) + " &3ms.");
    }

    @Override
    public void onDisable() {
        log("&f插件开始卸载...");
        long closeTime = System.currentTimeMillis();
        log("&f取消任务...");
        Bukkit.getScheduler().cancelTasks(instance);
        log("&3插件卸载完成, 共耗时 &b" + (System.currentTimeMillis() - closeTime) + " &3ms.");
    }

    public static FWToolBukkit getInstance() {
        return instance;
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
