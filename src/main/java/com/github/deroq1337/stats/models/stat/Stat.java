package com.github.deroq1337.stats.models.stat;

import lombok.*;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Stat {

    private final StatType type;
    private final String localeKey;
    private int value;

    public void incrementValue(int i) {
        this.value += i;
    }
}
