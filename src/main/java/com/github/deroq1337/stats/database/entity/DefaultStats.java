package com.github.deroq1337.stats.database.entity;

import com.github.deroq1337.stats.models.stat.ImmutableStat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class DefaultStats implements Stats {

    private @NotNull final UUID player;
    private final int interval;
    private final long rank;
    private @NotNull final Set<ImmutableStat> stats;

    @Override
    public void print() {
        String headline = "Stats von " + player.toString();
        if (interval > 0 && interval <= 30) {
            headline += " (" + interval + "d)";
        }

        System.out.println(headline);
        System.out.println("Ranking: #" + rank);
        stats.forEach(stat -> System.out.println(stat.localeKey() + ": " + stat.value()));
    }
}
