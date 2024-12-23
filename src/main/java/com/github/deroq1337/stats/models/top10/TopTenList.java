package com.github.deroq1337.stats.models.top10;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class TopTenList {

    private final int interval;
    private final @NotNull Set<TopTenListEntry> entries;

    public void print(@NotNull CommandSender commandSender) {
        StringBuilder builder = new StringBuilder("Top 10 (" + interval + "d)\n");
        entries.forEach(entry -> builder.append("#").append(entry.getRank()).append(": ")
                .append(entry.getPlayer().toString()).append(" - ").append(entry.getValue()).append(" Kills"));
        commandSender.sendMessage(builder.toString());
    }
}
