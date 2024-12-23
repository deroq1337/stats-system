package com.github.deroq1337.stats.commands.top;

import com.github.deroq1337.stats.StatsManager;
import com.github.deroq1337.stats.StatsSystem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class BaseTopCommand implements CommandExecutor {

    private final @NotNull StatsSystem plugin;
    private final @NotNull StatsManager statsManager;
    private final int interval;

    public BaseTopCommand(@NotNull StatsSystem plugin, @NotNull String name, int interval) {
        this.plugin = plugin;
        this.statsManager = plugin.getStatsManager();
        this.interval = interval;

        Optional.ofNullable(plugin.getCommand(name)).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        statsManager.getTopTenList(interval).thenAccept(top -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                top.print(commandSender);
            });
        });
        return false;
    }
}
