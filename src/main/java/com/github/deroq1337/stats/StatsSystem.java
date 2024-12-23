package com.github.deroq1337.stats;

import com.github.deroq1337.stats.commands.AlltimeStatsCommand;
import com.github.deroq1337.stats.commands.DailyStatsCommand;
import com.github.deroq1337.stats.commands.MonthlyStatsCommand;
import com.github.deroq1337.stats.database.DefaultMySQL;
import com.github.deroq1337.stats.database.MySQL;
import com.github.deroq1337.stats.repository.DefaultStatsRepository;
import com.github.deroq1337.stats.repository.StatsRepository;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class StatsSystem extends JavaPlugin {

    private MySQL mySQL;
    private StatsRepository repository;

    @Override
    public void onEnable() {
        this.mySQL = new DefaultMySQL();
        mySQL.connect();

        this.repository = new DefaultStatsRepository(mySQL);
        createTablesAndIndices();

        new AlltimeStatsCommand(this);
        new MonthlyStatsCommand(this);
        new DailyStatsCommand(this);
    }

    @Override
    public void onDisable() {
        mySQL.disconnect();
    }

    private void createTablesAndIndices() {
        mySQL.update("CREATE TABLE IF NOT EXISTS stats(" +
                "id VARCHAR(32) NOT NULL," +
                "locale_key VARCHAR(32) NOT NULL," +
                "description VARCHAR(64)," +
                "PRIMARY KEY(id)" +
                ");").join();

        mySQL.update("CREATE TABLE IF NOT EXISTS stats_users(" +
                "id INT AUTO_INCREMENT NOT NULL," +
                "player VARCHAR(36) NOT NULL," +
                "stat VARCHAR(32) NOT NULL," +
                "value INT NOT NULL," +
                "timestamp BIGINT NOT NULL," +
                "PRIMARY KEY(id)," +
                "FOREIGN KEY(stat) REFERENCES stats(id)" +
                ");").join();

        mySQL.update("CREATE INDEX IF NOT EXISTS idx_stats_users_player_timestamp ON stats_users(player, timestamp);");
        mySQL.update("CREATE INDEX IF NOT EXISTS idx_stats_users_stat ON stats_users(stat);");
        mySQL.update("CREATE INDEX IF NOT EXISTS idx_stats_users_value ON stats_users(value);");
        mySQL.update("CREATE INDEX IF NOT EXISTS idx_stats_users_value_timestamp ON stats_users(value);");
    }
}
