package com.github.deroq1337.stats.data.repository;

import com.github.deroq1337.stats.data.entity.Stats;
import com.github.deroq1337.stats.data.models.top10.TopTenList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StatsRepository {

    @NotNull CompletableFuture<Stats> getStatsByPlayer(@NotNull UUID player);

    @NotNull CompletableFuture<Stats> getStatsByPlayer(@NotNull UUID player, int interval);

    @NotNull CompletableFuture<TopTenList> getTopTenList();

    @NotNull CompletableFuture<TopTenList> getTopTenList(int interval);
}
