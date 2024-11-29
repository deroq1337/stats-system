package com.github.lukas2o11.stats.database.repository;

import com.github.lukas2o11.stats.database.entity.Stats;
import com.github.lukas2o11.stats.database.models.top10.TopTenList;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface StatsRepository {

    CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player);

    CompletableFuture<? extends Stats> getStatsByPlayer(@NotNull UUID player, int interval);

    CompletableFuture<? extends TopTenList> getTopTenList();

    CompletableFuture<? extends TopTenList> getTopTenList(int interval);
}
