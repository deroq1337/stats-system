package com.github.deroq1337.stats.data.models.top10;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class TopTenListEntry {

    private final @NotNull UUID player;
    private final int rank;
    private final long value;
}
