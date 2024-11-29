package com.github.lukas2o11.stats.database.entity;

import com.github.lukas2o11.stats.models.stat.ImmutableStat;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class DefaultStats implements Stats {

    private final UUID player;
    private final int interval;
    private final long rank;
    private final Set<ImmutableStat> stats;

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
