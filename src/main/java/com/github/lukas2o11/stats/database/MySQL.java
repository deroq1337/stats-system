package com.github.lukas2o11.stats.database;

import com.github.lukas2o11.stats.database.result.DBResult;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface MySQL {

    void connect();

    void disconnect();

    CompletableFuture<Void> update(@NotNull String sql, Object... params);

    CompletableFuture<DBResult> query(@NotNull String sql, Object... params);
}
