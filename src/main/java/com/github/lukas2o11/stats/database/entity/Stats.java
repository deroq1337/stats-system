package com.github.lukas2o11.stats.database.entity;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Stats {

    @NotNull UUID getPlayer();

    int getInterval();

    long getRank();

    void print();
}
