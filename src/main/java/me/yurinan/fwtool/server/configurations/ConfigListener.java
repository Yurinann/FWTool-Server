package me.yurinan.fwtool.server.configurations;

import me.yurinan.fwtool.server.FWToolServer;
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

public class ConfigListener {

    private static long LAST_MOD = 0L;
    private static WatchService watchService;
    private static String filePath;

    public void watch() {

        filePath = FileConfig.dataFolder.getAbsolutePath();
        FWToolServer.log("&f监听路径: &8" + filePath);

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

                    Thread.sleep(50);

                    for (WatchEvent<?> event : key.pollEvents()) {
                        if (event.kind() == OVERFLOW) {
                            continue;
                        }
                        fileNameSet.add(event.context() + "");
                    }

                    long lastModified = FileConfig.configFile.lastModified();

                    for (String name : fileNameSet) {
                        if (lastModified != LAST_MOD && FileConfig.configFile.length() > 0) {
                            FileConfig.reloadConfig();
                            FWToolServer.log("&3文件 &f" + filePath + "\\" + name + " &3已自动重载!");
                            FWToolServer.log("&3检测并执行指令...");
                            if (FileConfig.getConfig().contains("command") && !FileConfig.getConfig().getString("command").isEmpty() && !Objects.equals(FileConfig.getConfig().getString("command"), "")) {
                                Bukkit.getScheduler().runTask(FWToolServer.instance, () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), FileConfig.getConfig().getString("command")));
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
