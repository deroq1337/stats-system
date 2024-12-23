package com.github.deroq1337.stats.data.commands.top;

import com.github.deroq1337.stats.StatsSystem;
import org.jetbrains.annotations.NotNull;

public class AlltimeTopCommand extends BaseTopCommand {

    public AlltimeTopCommand(@NotNull StatsSystem plugin) {
        super(plugin, "alltop", Integer.MAX_VALUE);
    }
}
