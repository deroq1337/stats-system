package com.github.lukas2o11.stats.repository;

import com.github.lukas2o11.stats.database.MySQL;
import com.github.lukas2o11.stats.database.entity.DefaultStats;
import com.github.lukas2o11.stats.database.entity.Stats;
import com.github.lukas2o11.stats.models.stat.ImmutableStat;
import com.github.lukas2o11.stats.models.stat.Stat;
import com.github.lukas2o11.stats.models.stat.StatType;
import com.github.lukas2o11.stats.models.top10.DefaultTopTenList;
import com.github.lukas2o11.stats.models.top10.TopTenList;
import com.github.lukas2o11.stats.models.top10.TopTenListEntry;
import com.github.lukas2o11.stats.database.result.DBResult;
import com.github.lukas2o11.stats.database.result.DBRow;
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

    private static final String STATS_QUERY = "SELECT stats_users.player, stats_users.stat, stats_users.value, stats.locale_key " +
            "FROM stats_users " +
            "INNER JOIN stats ON stats.id = stats_users.stat " +
            "WHERE stats_users.player = ? ";

    private static final String STATS_INTERVAL_QUERY = "SELECT stats_users.player, stats_users.stat, stats_users.value, stats_users.timestamp, stats.locale_key " +
            "FROM stats_users " +
            "INNER JOIN stats ON stats.id = stats_users.stat " +
            "WHERE stats_users.player = ? " +
            "AND stats_users.timestamp <= ? " +
            "AND stats_users.timestamp > ?";

    private static final String RANK_QUERY = "SELECT COUNT(*) + 1 AS rank " +
            "FROM stats_users " +
            "WHERE stat = '" + RANK_SORT_BY + "' " +
            "AND value > (SELECT SUM(value) FROM stats_users WHERE player = ? AND stat = '" + RANK_SORT_BY + "')";

    private static final String RANK_INTERVAL_QUERY = "SELECT COUNT(*) + 1 AS rank " +
            "FROM stats_users " +
            "WHERE stat = '" + RANK_SORT_BY + "' " +
            "AND timestamp <= ? " +
            "AND timestamp > ? " +
            "AND value > (SELECT SUM(value) FROM stats_users WHERE player = ? AND stat = '" + RANK_SORT_BY + "')";

    private static final String TOP_LIST_QUERY = "SELECT player, SUM(value) AS total_value " +
            "FROM stats_users " +
            "WHERE stat = '" + RANK_SORT_BY + "' " +
            "GROUP BY player " +
            "ORDER BY total_value DESC " +
            "LIMIT 10;";

    private static final String TOP_LIST_INTERVAL_QUERY = "SELECT player, SUM(value) AS total_value " +
            "FROM stats_users " +
            "WHERE stat = '" + RANK_SORT_BY + "' " +
            "AND timestamp <= ? " +
            "AND timestamp > ? " +
            "GROUP BY player " +
            "ORDER BY total_value DESC " +
            "LIMIT 10;";

    private final MySQL mySQL;

    @Override
    public @NotNull CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player) {
        return getStatsByPlayer(player, -1);
    }

    @Override
    public @NotNull CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player, int interval) {
        CompletableFuture<DBResult> statsFuture;
        CompletableFuture<Long> rankFuture;
        if (interval > 0 && interval < 31) {
            long now = System.currentTimeMillis();
            long millis = now - ((long) interval * 24 * 60 * 60 * 1000);

            statsFuture = mySQL.query(STATS_INTERVAL_QUERY, player.toString(), now, millis);
            rankFuture = mySQL.query(RANK_INTERVAL_QUERY, now, millis, player.toString()).thenApply(result -> {
                if (result.getRows().isEmpty()) {
                    return (long) -1;
                }
                return result.getRows().getFirst().getValue("rank", Long.class);
            });
        } else {
            statsFuture = mySQL.query(STATS_QUERY, player.toString());
            rankFuture = mySQL.query(RANK_QUERY, player.toString()).thenApply(result -> {
                if (result.getRows().isEmpty()) {
                    return (long) -1;
                }
                return result.getRows().getFirst().getValue("rank", Long.class);
            });
        }

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
    public CompletableFuture<? extends TopTenList> getTopTenList() {
        return getTopTenList(-1);
    }

    @Override
    public CompletableFuture<? extends TopTenList> getTopTenList(int interval) {
        CompletableFuture<DBResult> query;
        if (interval > 0 && interval < 31) {
            long now = System.currentTimeMillis();
            long millis = now - ((long) interval * 24 * 60 * 60 * 1000);
            query = mySQL.query(TOP_LIST_INTERVAL_QUERY, now, millis);
        } else {
            query = mySQL.query(TOP_LIST_QUERY);
        }

        return query.thenApply(result -> {
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
