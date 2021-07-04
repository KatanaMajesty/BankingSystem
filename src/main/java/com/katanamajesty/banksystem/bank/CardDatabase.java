package com.katanamajesty.banksystem.bank;

import org.sqlite.SQLiteDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CardDatabase {

    private final String dataBaseName;

    public CardDatabase(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    String databaseUrl;

    public void establishConnection() {

        // создание адреса бд
        databaseUrl = "jdbc:sqlite:./" + dataBaseName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(databaseUrl);

        // создание таблицы
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                        "id INTEGER," +
                        "number TEXT," +
                        "pin VARCHAR(4)," +
                        "balance INTEGER DEFAULT 0" +
                        ");");
            }
        } catch (SQLException exception) {
            System.out.println("Не подключено");
            exception.printStackTrace();
        }
    }

    public void insertValues(Object[] cardInfo) {

        // создание адреса бд
        databaseUrl = "jdbc:sqlite:./" + dataBaseName;
        SQLiteDataSource dataSource = new SQLiteDataSource();
        dataSource.setUrl(databaseUrl);

        // заполнение таблицы
        try (Connection connection = dataSource.getConnection()) {
            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate("INSERT INTO card VALUES ("
                        + cardInfo[0] + ", "
                        + cardInfo[1] + ", "
                        + "'" + cardInfo[2] + "'"
                        + ", " + cardInfo[3]
                        + ")");
            }
        } catch (SQLException exception) {
            System.out.println("Не подключено");
            exception.printStackTrace();
        }

    }

}
