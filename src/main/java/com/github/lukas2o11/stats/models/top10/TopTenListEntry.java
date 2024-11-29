package com.github.lukas2o11.stats.models.top10;

import lombok.*;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class TopTenListEntry {

    private final UUID player;
    private final int rank;
    private final long value;
}
