package me.yurinan.fwtool.server.bungee.configurations;

import me.yurinan.fwtool.server.bungee.FWToolBungee;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
                            if (FileConfigBungee.getConfig(data).contains("dispatch-command")) {
                                String command = FileConfigBungee.getConfig(data).getString("dispatch-command");
                                if (!command.isEmpty() && !Objects.equals(command, "")) {
                                    if (command.startsWith("<console>")) {
                                        String finalCommandConsole = FileConfigBungee.getConfig(data).getString("dispatch-command");
                                        ProxyServer.getInstance().getScheduler().runAsync(FWToolBungee.getInstance(), () ->
                                                ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getConsole(), finalCommandConsole.replaceFirst("<console>", "")));
                                    } else if (command.startsWith("<player")) {
                                        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
                                            String commandCheck = command.replaceFirst("<player:", "").split(">")[0];
                                            if (player.getName().equalsIgnoreCase(commandCheck)) {
                                                String finalCommandPlayer = FileConfigBungee.getConfig(data).getString("dispatch-command");
                                                ProxyServer.getInstance().getScheduler().runAsync(FWToolBungee.getInstance(), () ->
                                                        ProxyServer.getInstance().getPluginManager().dispatchCommand(ProxyServer.getInstance().getPlayer(player.getName()), finalCommandPlayer.replaceFirst("<player:" + player.getName() + ">", "")));
                                            } else {
                                                FWToolBungee.log("&f指定的玩家不存在!");
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
