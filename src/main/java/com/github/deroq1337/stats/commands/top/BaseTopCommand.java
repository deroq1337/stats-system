package com.github.deroq1337.stats.commands.top;

import com.github.deroq1337.stats.StatsSystem;
import com.github.deroq1337.stats.repository.StatsRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class BaseTopCommand implements CommandExecutor {

    private final @NotNull StatsSystem plugin;
    private final @NotNull StatsRepository repository;
    private final int interval;

    public BaseTopCommand(@NotNull StatsSystem plugin, @NotNull String name, int interval) {
        this.plugin = plugin;
        this.repository = plugin.getRepository();
        this.interval = interval;

        Optional.ofNullable(plugin.getCommand(name)).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        repository.getTopTenList(interval).thenAccept(top -> {
            Bukkit.getScheduler().runTask(plugin, () -> {
                top.print(commandSender);
            });
        });
        return false;
    }
}
