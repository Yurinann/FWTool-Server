package me.yurinan.fwtool.server.bukkit.configurations;

import me.yurinan.fwtool.server.bukkit.FWToolBukkit;
import org.bukkit.Bukkit;

import java.nio.file.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

/**
 * @author Yurinan
 * @since 2022/3/23 20:39
 */

public class ConfigListenerBukkit {

    private static long LAST_MOD = 0L;
    private static WatchService watchService;
    private static String filePath;

    public void watch() {
        filePath = FileConfigBukkit.getDataFolder().getAbsolutePath();
        FWToolBukkit.log("&f监听路径: &8" + filePath);

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

                    long lastModified = FileConfigBukkit.configFile.lastModified();

                    for (String ignored : fileNameSet) {
                        if (lastModified != LAST_MOD && FileConfigBukkit.configFile.length() > 0) {
                            FileConfigBukkit.reloadConfig();
                            FWToolBukkit.log("&3从工具箱成功接收指令, 正在执行...");
                            if (FileConfigBukkit.getConfig().contains("command") && !FileConfigBukkit.getConfig().getString("command").isEmpty() && !Objects.equals(FileConfigBukkit.getConfig().getString("command"), "")) {
                                Bukkit.getScheduler().runTask(FWToolBukkit.instance, () ->
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), FileConfigBukkit.getConfig().getString("command")));
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
