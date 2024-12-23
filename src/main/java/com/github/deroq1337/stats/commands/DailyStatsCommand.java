package com.github.deroq1337.stats.commands;

import com.github.deroq1337.stats.StatsSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DailyStatsCommand extends BaseStatsCommand {

    public DailyStatsCommand(@NotNull StatsSystem plugin) {
        super(plugin, "statsd");
    }

    @Override
    protected void handleCommand(@NotNull CommandSender commandSender, @NotNull UUID targetUuid, @NotNull String[] args) {
        if (args.length < 1) {
            commandSender.sendMessage("§c/statsd <interval>");
            return;
        }

        int interval;
        try {
            interval = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            commandSender.sendMessage("§cGib ein valides Zeitintervall an!");
            return;
        }

        if (interval < 1 || interval > 30) {
            if (!commandSender.hasPermission("stats.daily.bypass")) {
                commandSender.sendMessage("§cGib ein Zeitintervall zwischen 1 und 30 Tagen an");
                return;
            }
        }

        repository.getStatsByPlayer(targetUuid, interval).thenAccept(stats -> {
            Bukkit.getScheduler().runTask(plugin, () -> stats.print(commandSender));
        });
    }
}
