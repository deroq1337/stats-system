package com.github.lukas2o11.stats;

import com.github.lukas2o11.stats.database.DefaultMySQL;
import com.github.lukas2o11.stats.database.MySQL;
import com.github.lukas2o11.stats.database.repository.DefaultStatsRepository;
import com.github.lukas2o11.stats.database.repository.StatsRepository;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.UUID;

public class StatsSystem extends JavaPlugin {

    private MySQL mySQL;
    private StatsRepository repository;

    @Override
    public void onEnable() {
        this.mySQL = new DefaultMySQL();
        mySQL.connect();

        this.repository = new DefaultStatsRepository(mySQL);
        createTablesAndIndices();
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