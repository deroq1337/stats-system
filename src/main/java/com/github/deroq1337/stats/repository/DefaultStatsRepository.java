package com.github.deroq1337.stats.repository;

import com.github.deroq1337.stats.database.MySQL;
import com.github.deroq1337.stats.entity.DefaultStats;
import com.github.deroq1337.stats.entity.Stats;
import com.github.deroq1337.stats.models.stat.ImmutableStat;
import com.github.deroq1337.stats.models.stat.Stat;
import com.github.deroq1337.stats.models.stat.StatType;
import com.github.deroq1337.stats.models.top10.DefaultTopTenList;
import com.github.deroq1337.stats.models.top10.TopTenList;
import com.github.deroq1337.stats.models.top10.TopTenListEntry;
import com.github.deroq1337.stats.database.result.DBResult;
import com.github.deroq1337.stats.database.result.DBRow;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DefaultStatsRepository implements StatsRepository {

    /* Too lazy to create config */
    private static final StatType RANK_SORT_BY = StatType.KILLS;

    private static final String STATS_QUERY = "SELECT stats_users.player, stats_users.stat, stats_users.value, stats_users.timestamp, stats.locale_key " +
            "FROM stats_users " +
            "INNER JOIN stats ON stats.id = stats_users.stat " +
            "WHERE stats_users.player = ? " +
            "AND stats_users.timestamp <= ? " +
            "AND stats_users.timestamp > ?";

    private static final String RANK_QUERY = "SELECT COUNT(*) + 1 AS rank " +
            "FROM stats_users " +
            "WHERE stat = '" + RANK_SORT_BY + "' " +
            "AND timestamp <= ? " +
            "AND timestamp > ? " +
            "AND value > (SELECT SUM(value) FROM stats_users WHERE player = ? AND stat = '" + RANK_SORT_BY + "')";

    private static final String TOP_LIST_INTERVAL_QUERY = "SELECT player, SUM(value) AS total_value " +
            "FROM stats_users " +
            "WHERE stat = '" + RANK_SORT_BY + "' " +
            "AND timestamp <= ? " +
            "AND timestamp > ? " +
            "GROUP BY player " +
            "ORDER BY total_value DESC " +
            "LIMIT 10;";

    private final @NotNull MySQL mySQL;

    @Override
    public @NotNull CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player) {
        return getStatsByPlayer(player, Integer.MAX_VALUE);
    }

    @Override
    public @NotNull CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player, int interval) {
        long now = System.currentTimeMillis();
        long millis = now - ((long) interval * 24 * 60 * 60 * 1000);

        CompletableFuture<DBResult> statsFuture = mySQL.query(STATS_QUERY, player.toString(), now, millis);
        CompletableFuture<Long> rankFuture = mySQL.query(RANK_QUERY, now, millis, player.toString()).thenApply(result -> {
            if (result.getRows().isEmpty()) {
                return (long) -1;
            }
            return result.getRows().getFirst().getValue("rank", Long.class);
        });

        return CompletableFuture.allOf(statsFuture, rankFuture).thenApplyAsync(v -> {
            DBResult statsResult = statsFuture.join();
            Map<StatType, Stat> statMap = new HashMap<>();

            statsResult.getRows().forEach(row -> {
                StatType type = StatType.fromString(row.getValue("stat", String.class).toUpperCase(Locale.ENGLISH));
                String localeKey = row.getValueOptional("locale_key", String.class).orElse("N/A");
                int value = row.getValueOptional("value", Integer.class).orElse(0);

                statMap.computeIfAbsent(type, k -> new Stat(type, localeKey))
                        .incrementValue(value);
            });

            Set<ImmutableStat> statSet = statMap.values().stream()
                    .map(stat -> new ImmutableStat(stat.getType(), stat.getLocaleKey(), stat.getValue()))
                    .collect(Collectors.toSet());
            return new DefaultStats(player, interval, rankFuture.join(), statSet);
        });
    }

    @Override
    public @NotNull CompletableFuture<? extends TopTenList> getTopTenList() {
        return getTopTenList(Integer.MAX_VALUE);
    }

    @Override
    public @NotNull CompletableFuture<? extends TopTenList> getTopTenList(int interval) {
        long now = System.currentTimeMillis();
        long millis = now - ((long) interval * 24 * 60 * 60 * 1000);

        return mySQL.query(TOP_LIST_INTERVAL_QUERY, now, millis).thenApply(result -> {
            Set<TopTenListEntry> entries = new LinkedHashSet<>();
            int rank = 1;
            for (DBRow row : result.getRows()) {
                UUID player = UUID.fromString(row.getValue("player", String.class));
                long value = row.getValueOptional("total_value", BigDecimal.class)
                        .map(BigDecimal::longValue)
                        .orElse((long) 0);

                entries.add(new TopTenListEntry(player, rank, value));
                rank++;
            }

            return new DefaultTopTenList(interval, entries);
        }).exceptionally(throwable -> {
            System.err.println("Error query for top 10: " + throwable.getMessage());
            throw new RuntimeException(throwable);
        });
    }
}
