package com.github.deroq1337.stats.data.exceptions;

import org.jetbrains.annotations.NotNull;

public class StatNotFoundException extends RuntimeException {

    public StatNotFoundException(@NotNull String message) {
        super(message);
    }
}
