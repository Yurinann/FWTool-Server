package me.yurinan.fwtool.server.bungee.configurations;

import me.yurinan.fwtool.server.bungee.FWToolBungee;
import net.md_5.bungee.api.ProxyServer;

import java.io.File;
import java.nio.file.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * @author Yurinan
 * @since 2022/3/24 18:20
 */

public class ConfigListenerBungee {

    private static long LAST_MOD = 0L;
    private static WatchService watchService;
    private static String filePath;

    public void watch() {
        filePath = FileConfigBungee.getDataFolder().getAbsolutePath();
        FWToolBungee.log("&f监听路径: &8" + filePath);

        try {
            watchService = FileSystems.getDefault().newWatchService();
            Paths.get(filePath).register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Thread(() -> {

            final Set<String> fileNameSet = new HashSet<>();

            try {
                while (true) {
                    WatchKey key = watchService.take();

                    // TODO: timestamps check duplicate reload
                    Thread.sleep(50);

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == OVERFLOW) {
                            continue;
                        }
                        fileNameSet.add(event.context() + "");
                    }

                    File data = FileConfigBungee.terminalDataFile;
                    long lastModified = data.lastModified();

                    for (String ignored : fileNameSet) {
                        if (lastModified != LAST_MOD && data.length() > 0) {
                            FileConfigBungee.reloadConfig(data);
                            FWToolBungee.log("&3从工具箱成功接收指令, 正在执行...");
                            if (FileConfigBungee.getConfig(data).contains("dispatch-command") && !FileConfigBungee.getConfig(data).getString("dispatch-command").isEmpty() && !Objects.equals(FileConfigBungee.getConfig(data).getString("dispatch-command"), "")) {
                                ProxyServer.getInstance().getScheduler().runAsync(FWToolBungee.getInstance(), () ->
                                        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), FileConfigBungee.getConfig(data).getString("dispatch-command")));
                            }
                            LAST_MOD = lastModified;
                        }
                    }

                    boolean valid = key.reset();
                    fileNameSet.clear();
                    if (!valid) {
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }

}
