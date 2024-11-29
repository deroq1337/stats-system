package com.github.lukas2o11.stats.models.top10;

import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface TopTenList {

    int getInterval();

    @NotNull Set<TopTenListEntry> getEntries();

    void print();
}
