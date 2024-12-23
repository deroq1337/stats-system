package com.github.deroq1337.stats.repository;

import com.github.deroq1337.stats.entity.Stats;
import com.github.deroq1337.stats.models.top10.TopTenList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StatsRepository {

    @NotNull CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player);

    @NotNull CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player, int interval);

    @NotNull CompletableFuture<? extends TopTenList> getTopTenList();

    @NotNull CompletableFuture<? extends TopTenList> getTopTenList(int interval);
}
