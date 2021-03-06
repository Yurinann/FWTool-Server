package me.yurinan.fwtool.server.bungee;

import me.yurinan.fwtool.server.bungee.bstats.Metrics;
import me.yurinan.fwtool.server.bungee.configurations.ConfigListenerBungee;
import me.yurinan.fwtool.server.universal.FWToolServer;
import me.yurinan.fwtool.server.bungee.configurations.FileConfigBungee;
import me.yurinan.fwtool.server.universal.utils.ColorParser;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * @author Yurinan
 * @since 2022/3/24 11:32
 */

public class FWToolBungee extends Plugin {

    private static FWToolBungee instance;

    @Override
    public void onEnable() {
        instance = this;
        log("&f插件开始加载...");
        FWToolServer.bcMode = true;
        long startTime = System.currentTimeMillis();
        log("&f加载配置文件...");
        FileConfigBungee.initConfig();
        log("&f初始化监听程序...");
        ConfigListenerBungee configListener = new ConfigListenerBungee();
        configListener.watch();
        log("&f注册 bStats 数据统计...");
        int pluginId = 14736;
        Metrics metrics = new Metrics(this, pluginId);
        log("&3插件加载完成, 共耗时 &b" + (System.currentTimeMillis() - startTime) + " &3ms.");
    }

    public static FWToolBungee getInstance() {
        return instance;
    }

    @Override
    public void onDisable() {
        log("&f插件开始卸载...");
        long closeTime = System.currentTimeMillis();
        log("&f取消任务...");
        ProxyServer.getInstance().getScheduler().cancel(getInstance());
        log("&3插件卸载完成, 共耗时 &b" + (System.currentTimeMillis() - closeTime) + " &3ms.");
    }

    public static void log(String message) {
        ProxyServer.getInstance().getConsole().sendMessage(ColorParser.parse("&8[&b" + getInstance().getDescription().getName() + "&8] " + message));
    }

    public static void warn(String message) {
        log("&6警告! &r" + message);
    }

    public static void error(String message) {
        log("&4错误! &r" + message);
    }

}
