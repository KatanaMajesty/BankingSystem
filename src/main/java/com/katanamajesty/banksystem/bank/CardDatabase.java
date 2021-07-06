package com.katanamajesty.banksystem.bank;

import org.sqlite.SQLiteDataSource;

import java.sql.*;
import java.util.ArrayList;

public class CardDatabase {

    private final SQLiteDataSource dataSource = new SQLiteDataSource();
    private final String dataBaseName;
    private final String databaseUrl;

    /**
     * Конструктор для инициализации базы данных
     *
     * @param dataBaseName    final имя файла для базы данных
     */
    public CardDatabase(String dataBaseName) {
        this.dataBaseName = dataBaseName;
         databaseUrl = "jdbc:sqlite:./" + dataBaseName;
    }

    /**
     * Метод устанавливает соединение с базой данных
     */
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

    @SuppressWarnings("unused")
    public ArrayList<Object> getCardInfo(String query) {

        // создание адреса бд
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
            }
            System.out.println(returnStatement);
            statement.close();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return returnStatement;

    }

    /**
     * Метод пополняет базу данных новыми карточками
     * при регистрации нового пользователя
     *
     * @param cardInfo  Параметр типа обьект-массив.
     *                  Принимает информацию о карточке,
     *                  полученную ранее из getCardInfo()
     */
    public void insertValues(Object[] cardInfo) {

        // создание адреса бд
        dataSource.setUrl(databaseUrl);

        // Строка запроса в бд
        String query = "INSERT INTO card (id, number, pin) VALUES (?, ?, ?)";

        // попытка заполнения таблицы
        try (Connection connection = dataSource.getConnection()) {

            // Создаёт временное заготовленое заявление от String query
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setObject(1, cardInfo[0]);
                statement.setObject(2, cardInfo[1]);
                statement.setObject(3, cardInfo[2]);
                // balance is default at the beginning
                statement.executeUpdate();
            }

        } catch (SQLException exception) {

            // Возвращает в случае сбоя при подключении
            System.out.println("Не подключено");
            exception.printStackTrace();

        }

    }

    /**
     * Метод обновляет данные в базе данных по
     * заданой строке запроса для бд
     *
     * @param query     Параметр принимает строку
     *                  запроса для базы данных
     */
    public void updateValues(String query) {

        // создание адреса бд
        dataSource.setUrl(databaseUrl);

        // Попытка подключения и выполнения запроса
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Метод позволяет удалить карточку с параметрами ниже из
     * базы данных
     *
     * @param cardNumber    принимает карточку вошедшего в
     *                      аккаунт пользователя
     * @param pin           принимает пин-код от карточки пользователя
     */
    public void deleteCard(String cardNumber, String pin) {

        dataSource.setUrl(databaseUrl);

        String query = "DELETE FROM card WHERE number = " + cardNumber + " AND pin = " + pin;

        try (Connection connection = dataSource.getConnection()) {
            try(PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

    }

}
