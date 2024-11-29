package com.github.lukas2o11.stats.database.models.stat;

public record ImmutableStat(StatType type, String localeKey, int value) {
}
