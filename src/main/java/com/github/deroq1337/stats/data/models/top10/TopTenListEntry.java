package com.github.deroq1337.stats.data.models.top10;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record TopTenListEntry(@NotNull UUID player, int rank, long value) {
}
