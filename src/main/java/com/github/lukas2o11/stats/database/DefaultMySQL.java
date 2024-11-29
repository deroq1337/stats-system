package com.github.lukas2o11.stats.database;

import com.github.lukas2o11.stats.database.result.DBResult;
import com.github.lukas2o11.stats.database.result.DBRow;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class DefaultMySQL implements MySQL {

    private final HikariConfig config;

    public DefaultMySQL() {
        this.config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/stats-system?autoReconnect=true");
        config.setUsername("root");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    }

    private Optional<HikariDataSource> dataSource = Optional.empty();

    @Override
    public void connect() {
        if (dataSource.isEmpty()) {
            dataSource = Optional.of(new HikariDataSource(config));
            System.out.println("Initialized DataSource");
        }
    }

    @Override
    public void disconnect() {
        dataSource.ifPresent(ds -> {
            ds.close();
            System.out.println("Closed DataSource");
        });
    }

    @Override
    public CompletableFuture<Void> update(@NotNull String sql, Object... params) {
        return dataSource.map(ds -> CompletableFuture.runAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                bindParams(statement, params);
                statement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error executing update: " + e.getMessage());
            }
        })).orElseThrow(() -> new RuntimeException("Error creating query: no MySQL connection established"));
    }

    @Override
    public CompletableFuture<DBResult> query(@NotNull String sql, Object... params) {
        return dataSource.map(ds -> CompletableFuture.supplyAsync(() -> {
            try (Connection connection = getConnection()) {
                PreparedStatement statement = connection.prepareStatement(sql);
                bindParams(statement, params);

                ResultSet result = statement.executeQuery();
                List<DBRow> rows = new ArrayList<>();
                while (result.next()) {
                    ResultSetMetaData resultMetaData = result.getMetaData();
                    Map<String, Object> columns = new HashMap<>();

                    for (int columnIndex = 1; columnIndex <= resultMetaData.getColumnCount(); columnIndex++) {
                        String columnName = resultMetaData.getColumnName(columnIndex);
                        Object value = result.getObject(columnName);
                        columns.put(columnName, value);
                    }

                    rows.add(new DBRow(columns));
                }

                return new DBResult(rows);
            } catch (SQLException e) {
                System.err.println("Error executing query: " + e.getMessage());
                return new DBResult(new ArrayList<>());
            }
        })).orElseThrow(() -> new RuntimeException("Error creating query: no MySQL connection established"));
    }

    private @NotNull Connection getConnection() {
        return dataSource.map(ds -> {
            try {
                return ds.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException("Error getting connection from pool: " + e.getMessage());
            }
        }).orElseThrow(() -> new RuntimeException("DataSource was not initialized yet"));
    }

    private void bindParams(PreparedStatement statement, Object... params) throws SQLException {
        for (int index = 0; index < params.length; index++) {
            int paramIndex = index + 1;
            statement.setObject(paramIndex, params[index]);
        }
    }
}
