package com.github.deroq1337.stats.models.stat;

import lombok.*;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Stat {

    private final @NotNull StatType type;
    private final @NotNull String localeKey;
    private int value;

    public void incrementValue(int i) {
        this.value += i;
    }
}
