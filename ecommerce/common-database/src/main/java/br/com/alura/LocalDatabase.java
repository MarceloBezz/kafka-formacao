package br.com.alura;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LocalDatabase {
    private final Connection connection;

    LocalDatabase(String name) throws SQLException {
        String url = "jdbc:sqlite:" + name + ".db";
        this.connection = DriverManager.getConnection(url);
    }

    // Yes, this is way too generic
    // According to your database, avoid injection.
    public void createIfNotExists(String sql) {
        try {
            connection.createStatement().execute(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(String statement, String... params) throws SQLException {
        prepare(statement, params).execute();
    }

    public ResultSet query(String statement, String... params) throws SQLException {
        return prepare(statement, params).executeQuery();
    }

    private PreparedStatement prepare(String statement, String... params) throws SQLException {
        var preparedStatement = connection.prepareStatement(statement);
        for (int i = 0; i < params.length; i++) {
            preparedStatement.setString(i + 1, params[i]);
        }
        return preparedStatement;
    }
}
