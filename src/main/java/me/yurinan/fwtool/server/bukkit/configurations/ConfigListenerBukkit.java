package me.yurinan.fwtool.server.bukkit.configurations;

import me.yurinan.fwtool.server.bukkit.FWToolBukkit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.File;
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

                    File data = FileConfigBukkit.terminalDataFile;
                    long lastModified = data.lastModified();

                    for (String ignored : fileNameSet) {
                        if (lastModified != LAST_MOD && data.length() > 0) {
                            FileConfigBukkit.reloadConfig(data);
                            FWToolBukkit.log("&3从工具箱成功接收指令, 正在执行...");
                            if (FileConfigBukkit.getConfig(data).contains("dispatch-command")) {
                                String command = FileConfigBukkit.getConfig(data).getString("dispatch-command");
                                if (!command.isEmpty() && !Objects.equals(command, "")) {
                                    if (command.startsWith("<console>")) {
                                        String finalCommandConsole = FileConfigBukkit.getConfig(data).getString("dispatch-command");
                                        Bukkit.getScheduler().runTask(FWToolBukkit.getInstance(), () ->
                                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), finalCommandConsole.replaceFirst("<console>", "")));
                                    } else if (command.startsWith("<player")) {
                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            String commandCheck = command.replaceFirst("<player:", "").split(">")[0];
                                            if (player.getName().equalsIgnoreCase(commandCheck)) {
                                                String finalCommandPlayer = FileConfigBukkit.getConfig(data).getString("dispatch-command");
                                                Bukkit.getScheduler().runTask(FWToolBukkit.getInstance(), () ->
                                                        Bukkit.dispatchCommand(Bukkit.getPlayer(player.getName()), finalCommandPlayer.replaceFirst("<player:" + player.getName() + ">", "")));
                                            } else {
                                                FWToolBukkit.log("&f指定的玩家不存在!");
                                            }
                                        }
                                    }
                                }
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
