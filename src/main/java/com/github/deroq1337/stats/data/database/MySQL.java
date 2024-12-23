package com.github.deroq1337.stats.data.database;

import com.github.deroq1337.stats.data.database.result.DBResult;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public interface MySQL {

    void connect();

    void disconnect();

    @NotNull CompletableFuture<Void> update(@NotNull String sql, Object... params);

    @NotNull CompletableFuture<DBResult> query(@NotNull String sql, Object... params);
}
