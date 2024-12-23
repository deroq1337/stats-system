package com.github.deroq1337.stats.entity;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public interface Stats {

    @NotNull UUID getPlayer();

    int getInterval();

    long getRank();

    void print(@NotNull CommandSender commandSender);
}
