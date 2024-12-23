package com.github.deroq1337.stats.data.commands.stats;

import com.github.deroq1337.stats.StatsSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AlltimeStatsCommand extends BaseStatsCommand {

    private static final int ALLTIME_INTERVAL = Integer.MAX_VALUE;

    public AlltimeStatsCommand(@NotNull StatsSystem plugin) {
        super(plugin, "statsall");
    }

    @Override
    public void handleCommand(@NotNull CommandSender commandSender, @NotNull UUID targetUuid, @NotNull String[] args) {
        statsManager.getStatsByPlayer(targetUuid, ALLTIME_INTERVAL).thenAccept(stats -> {
            Bukkit.getScheduler().runTask(plugin, () -> stats.print(commandSender));
        });
    }
}
