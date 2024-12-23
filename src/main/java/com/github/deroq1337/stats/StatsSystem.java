package com.github.deroq1337.stats;

import com.github.deroq1337.stats.commands.stats.AlltimeStatsCommand;
import com.github.deroq1337.stats.commands.stats.DailyStatsCommand;
import com.github.deroq1337.stats.commands.stats.MonthlyStatsCommand;
import com.github.deroq1337.stats.commands.top.AlltimeTopCommand;
import com.github.deroq1337.stats.commands.top.MonthlyTopCommand;
import com.github.deroq1337.stats.database.DefaultMySQL;
import com.github.deroq1337.stats.database.MySQL;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class StatsSystem extends JavaPlugin {

    private MySQL mySQL;
    private StatsManager statsManager;

    @Override
    public void onEnable() {
        this.mySQL = new DefaultMySQL();
        mySQL.connect();

        this.statsManager = new DefaultStatsManager(this);

        new AlltimeStatsCommand(this);
        new MonthlyStatsCommand(this);
        new DailyStatsCommand(this);
        new AlltimeTopCommand(this);
        new MonthlyTopCommand(this);
    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
    }
}
