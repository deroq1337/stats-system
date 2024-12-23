package com.github.deroq1337.stats.commands.top;

import com.github.deroq1337.stats.StatsSystem;
import org.jetbrains.annotations.NotNull;

public class MonthlyTopCommand extends BaseTopCommand {

    public MonthlyTopCommand(@NotNull StatsSystem plugin) {
        super(plugin, "top", 30);
    }
}
