package com.github.lukas2o11.stats.models.stat;

import com.github.lukas2o11.stats.exceptions.StatNotFoundException;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum StatType {

    RANKING,
    POINTS,
    KILLS,
    DEATHS,
    MATCHES,
    WINS,
    LOSES,
    DESTROYED_BEDS,
    OPENED_CHESTS;

    public static @NotNull StatType fromString(String s) {
        try {
            return StatType.valueOf(s.toUpperCase(Locale.ENGLISH));
        } catch (Exception e) {
            throw new StatNotFoundException("StatType '" + s + "' was not found");
        }
    }
}
