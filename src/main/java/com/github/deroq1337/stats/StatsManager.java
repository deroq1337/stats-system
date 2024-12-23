package com.github.deroq1337.stats;

import com.github.deroq1337.stats.entity.Stats;
import com.github.deroq1337.stats.models.top10.TopTenList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StatsManager {

    @NotNull CompletableFuture<Stats> getStatsByPlayer(@NotNull UUID player);

    @NotNull CompletableFuture<Stats> getStatsByPlayer(@NotNull UUID player, int interval);

    @NotNull CompletableFuture<TopTenList> getTopTenList();

    @NotNull CompletableFuture<TopTenList> getTopTenList(int interval);
}
