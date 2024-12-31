package com.github.deroq1337.stats.data.entity;

import com.github.deroq1337.stats.data.models.stat.ImmutableStat;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

public record Stats(@NotNull UUID player, int interval, long rank, @NotNull Set<ImmutableStat> stats) {

    public void print(@NotNull CommandSender commandSender) {
        StringBuilder builder = new StringBuilder("Stats von " + player + "\n");
        if (interval > 0 && interval <= 30) {
           builder.append(" (").append(interval).append("d)\n");
        }

        builder.append("Ranking: #").append(rank).append("\n");
        stats.forEach(stat -> builder.append(stat.localeKey()).append(": ").append(stat.value()));
        commandSender.sendMessage(builder.toString());
    }
}
