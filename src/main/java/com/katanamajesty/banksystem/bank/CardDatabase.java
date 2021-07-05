package com.katanamajesty.banksystem.bank;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;

public class CardDatabase {

    private final String dataBaseName;

    public CardDatabase(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    SQLiteDataSource dataSource = new SQLiteDataSource();

    public void establishConnection() {

        // создание адреса бд
        String databaseUrl = "jdbc:sqlite:./" + dataBaseName;
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

    public void insertValues(Object[] cardInfo) { // FIXME: 06.07.2021 // сделать через параметры

        // создание адреса бд
        String databaseUrl = "jdbc:sqlite:./" + dataBaseName;
        dataSource.setUrl(databaseUrl);

        String insert = "INSERT INTO card (id, number, pin) VALUES (?, ?, ?)";

        // заполнение таблицы
        try (Connection connection = dataSource.getConnection()) {

            try (PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setObject(1, cardInfo[0]);
                statement.setObject(2, cardInfo[1]);
                statement.setObject(3, cardInfo[2]);
                // balance is default at the beginning
                statement.executeUpdate();
            }

        } catch (SQLException exception) {

            System.out.println("Не подключено");
            exception.printStackTrace();

        }

    }

    @SuppressWarnings("unused")
    public ArrayList<Object> getCardInfo(String query) {

        // создание адреса бд
        String databaseUrl = "jdbc:sqlite:./" + dataBaseName;
        dataSource.setUrl(databaseUrl);

        ArrayList<Object> returnStatement = new ArrayList<>();

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            while (resultSet.next()) {
                for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                    returnStatement.add(resultSet.getObject(i));
                }
                System.out.println(returnStatement);
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return returnStatement;

    }

    public void deleteCard(String cardNumber, String pin) {

        String databaseUrl = "jdbc:sqlite:./" + dataBaseName;
        dataSource.setUrl(databaseUrl);

        String query = "DELETE FROM card WHERE number = " + cardNumber + " AND pin = " + pin;

        try (Connection connection = dataSource.getConnection()) {

            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

}
