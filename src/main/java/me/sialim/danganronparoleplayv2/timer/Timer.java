package me.sialim.danganronparoleplayv2.timer;

import me.sialim.danganronparoleplayv2.DanganronpaRoleplayV2;
import me.sialim.danganronparoleplayv2.files.MessageConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer implements CommandExecutor {

    private BossBar bossBar;
    private DanganronpaRoleplayV2 main;
    private MessageConfig messageConfig;

    public Timer(DanganronpaRoleplayV2 main, MessageConfig messageConfig) {
        this.main = main;
        this.messageConfig = messageConfig;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (strings.length != 1) {
            String message = ChatColor.translateAlternateColorCodes('&',
                    messageConfig.get().getString("timer.error.args"));
            commandSender.sendMessage(message);
            return true;
        }

        int duration;
        try {
            duration = Integer.parseInt(strings[0]);
        } catch (NumberFormatException e) {
            String message = ChatColor.translateAlternateColorCodes('&',
                    messageConfig.get().getString("timer.error.invalid-int"));
            commandSender.sendMessage(message);
            return true;
        }

        bossBar = null;
        startTimer(duration);
        return true;
    }

    private void startTimer(int durationSeconds) {

        long startTime = System.currentTimeMillis() / 1000;
        final long[] endTime = {startTime + durationSeconds};

        if (bossBar == null) {
            bossBar = Bukkit.createBossBar("Timer", BarColor.PINK, BarStyle.SOLID);
            bossBar.setVisible(true);
            for (Player player : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(player);
            }
        }

        new BukkitRunnable() {
            int countdown = durationSeconds;

            @Override
            public void run() {
                long currentTime = System.currentTimeMillis() / 1000;
                long remainingTime = endTime[0] - currentTime;

                if (countdown <= 0) {
                    bossBar.removeAll();
                    bossBar.setVisible(false);
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                            messageConfig.get().getString("timer.over")));
                    endTime[0] = -1;
                    this.cancel();
                } else {
                    String title = ChatColor.translateAlternateColorCodes('&',
                            messageConfig.get().getString("timer.bossbar-text"));
                    title = title.replace("$time$", String.valueOf(remainingTime));
                    bossBar.setTitle(title);
                    bossBar.setProgress((double) remainingTime / durationSeconds);
                    countdown--;
                }
            }
        }.runTaskTimer(main, 0L, 20L);
    }
}
