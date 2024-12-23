package com.github.deroq1337.stats.entity;

import com.github.deroq1337.stats.models.stat.ImmutableStat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class DefaultStats implements Stats {

    private final @NotNull UUID player;
    private final int interval;
    private final long rank;
    private final @NotNull Set<ImmutableStat> stats;

    @Override
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
