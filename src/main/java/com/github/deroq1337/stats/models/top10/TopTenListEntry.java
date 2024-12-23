package com.github.deroq1337.stats.models.top10;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class TopTenListEntry {

    private @NotNull final UUID player;
    private final int rank;
    private final long value;
}
