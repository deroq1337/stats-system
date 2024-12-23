package com.github.deroq1337.stats;

import com.github.deroq1337.stats.entity.Stats;
import com.github.deroq1337.stats.models.top10.TopTenList;
import com.github.deroq1337.stats.repository.DefaultStatsRepository;
import com.github.deroq1337.stats.repository.StatsRepository;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class DefaultStatsManager implements StatsManager {

    private final @NotNull StatsRepository repository;

    public DefaultStatsManager(@NotNull StatsSystem plugin) {
        this.repository = new DefaultStatsRepository(plugin.getMySQL());
    }

    @Override
    public @NotNull CompletableFuture<Stats> getStatsByPlayer(@NotNull UUID player) {
        return repository.getStatsByPlayer(player);
    }

    @Override
    public @NotNull CompletableFuture<Stats> getStatsByPlayer(@NotNull UUID player, int interval) {
        return repository.getStatsByPlayer(player, interval);
    }

    @Override
    public @NotNull CompletableFuture<TopTenList> getTopTenList() {
        return repository.getTopTenList();
    }

    @Override
    public @NotNull CompletableFuture<TopTenList> getTopTenList(int interval) {
        return repository.getTopTenList(interval);
    }
}
