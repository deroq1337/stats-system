package com.github.deroq1337.stats.models.top10;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class DefaultTopTenList implements TopTenList {

    private final int interval;
    private final @NotNull Set<TopTenListEntry> entries;

    @Override
    public void print() {
        System.out.println("Top 10 (" + interval + "d)");
        entries.forEach(entry -> System.out.println("#" + entry.getRank() + ": " + entry.getPlayer().toString() + " - " + entry.getValue() + " Kills"));
    }
}
