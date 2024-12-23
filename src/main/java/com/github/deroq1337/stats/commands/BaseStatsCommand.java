package com.github.deroq1337.stats.commands;

import com.github.deroq1337.stats.StatsSystem;
import com.github.deroq1337.stats.repository.StatsRepository;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public abstract class BaseStatsCommand implements CommandExecutor {

    protected final @NotNull StatsSystem plugin;
    protected final @NotNull StatsRepository repository;

    public BaseStatsCommand(@NotNull StatsSystem plugin, @NotNull String name) {
        this.plugin = plugin;
        this.repository = plugin.getRepository();

        Optional.ofNullable(plugin.getCommand(name)).ifPresent(pluginCommand -> pluginCommand.setExecutor(this));
    }

    protected abstract void handleCommand(@NotNull CommandSender commandSender, @NotNull UUID targetUuid, @NotNull String[] args);

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        UUID targetUuid;
        if (args.length == 0) {
            if (!(commandSender instanceof Player player)) {
                commandSender.sendMessage("§cDu musst ein Spieler sein!");
                return true;
            }

            targetUuid = player.getUniqueId();
        } else {
            try {
                targetUuid = UUID.fromString(args[0]);
            } catch (IllegalArgumentException e) {
                commandSender.sendMessage("§cGib eine valide UUID an!");
                return true;
            }
        }

        Bukkit.getScheduler().runTask(plugin, () -> handleCommand(commandSender, targetUuid, args));
        return true;
    }
}