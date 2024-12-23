package com.github.deroq1337.stats.commands;

import com.github.deroq1337.stats.StatsSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class MonthlyStatsCommand extends BaseStatsCommand {

    private static final int MONTHLY_INTERVAL = 30;

    public MonthlyStatsCommand(@NotNull StatsSystem plugin) {
        super(plugin, "stats");
    }

    @Override
    public void handleCommand(@NotNull CommandSender commandSender, @NotNull UUID targetUuid, @NotNull String[] args) {
        repository.getStatsByPlayer(targetUuid, MONTHLY_INTERVAL).thenAccept(stats -> {
            Bukkit.getScheduler().runTask(plugin, () -> stats.print(commandSender));
        });
    }
}

