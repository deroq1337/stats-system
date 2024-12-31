package com.github.deroq1337.stats.data.models.top10;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public record TopTenList(int interval, @NotNull Set<TopTenListEntry> entries){

    public void print(@NotNull CommandSender commandSender) {
        StringBuilder builder = new StringBuilder("Top 10 (" + interval + "d)\n");
        entries.forEach(entry -> builder.append("#").append(entry.rank()).append(": ")
                .append(entry.player()).append(" - ").append(entry.value()).append(" Kills"));
        commandSender.sendMessage(builder.toString());
    }
}
