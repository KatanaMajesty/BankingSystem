package com.katanamajesty.banksystem.account;

import com.katanamajesty.banksystem.BankingSystem;
import com.katanamajesty.banksystem.bank.CardDatabase;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Класс, содержащий все методы
 * для взаимодействия с аккаунтом
 */
public class AccountActions {

    private static final String databaseName = BankingSystem.dataBaseName;

    /**
     * Метод выводит баланс пользователя
     *
     * @param cardNumber   Принимает строку с номером карточки
     *                     вошедшего в аккаунт пользователя
     */
    public static void balance(String cardNumber) {
        CardDatabase cardDatabase = new CardDatabase(databaseName);
        String query = "SELECT balance FROM card WHERE number = " + cardNumber;
        Integer userBalance = (Integer) cardDatabase.getCardInfo(query).get(0);
        System.out.println("\nBalance: " + userBalance + "\n");
    }

    /**
     * Метод добавляет пользователю указанное количество
     * денежных средств
     *
     * @param cardNum   Принимает строку с номером карточки
     *                  вошедшего в аккаунт пользователя
     */
    @SuppressWarnings("all")
    public static void addIncome(String cardNum) {

        Scanner scanner = new Scanner(System.in);
        CardDatabase cardDatabase = new CardDatabase(databaseName);

        System.out.println("\nEnter income:");
        try {
            Integer add = scanner.nextInt();
            String query = "UPDATE card SET balance = balance + " + add + " WHERE number = " + cardNum;
            cardDatabase.updateValues(query);
        } catch (InputMismatchException | NumberFormatException exception) {
            System.out.println("We can't add such a large amount of money!");
        }

        System.out.println("Income was added!\n");
    }

    /**
     * Метод позволяет переводить средства между банковскими карточками
     *
     * @param userCardNum   Принимает строку с номером карточки
     *                      вошедшего в аккаунт пользователя,
     *                      а не пользователя, которому нужно
     *                      перевести средства
     */
    public static void transferAction(String userCardNum) {

        Scanner scanner = new Scanner(System.in);
        CardDatabase cardDatabase = new CardDatabase(databaseName);

        // Интерфейс-составляющая
        System.out.println("\nTransfer");
        System.out.println("Enter card number:");
        String targetCardNum = scanner.nextLine();

        // Строки-запросы из базы данных. Получает данные о пользователях
        String targetCardQuery = "SELECT * FROM card WHERE number = " + targetCardNum;
        String userCardQuery = "SELECT balance FROM card WHERE number = " + userCardNum;

        // Создание обьектов списков с данными из базы данных
        ArrayList<Object> cardInfo = cardDatabase.getCardInfo(targetCardQuery);
        ArrayList<Object> userInfo = cardDatabase.getCardInfo(userCardQuery);

        // Проверка на наличие данных в списках
        if (cardInfo.isEmpty()) {
            System.out.println("Probably you made a mistake in the card number. Please try again!");
        } else {

            // Принимает количество денег к переводу
            System.out.println("Enter how much money you want to transfer:");
            Integer transfer = scanner.nextInt();

            // Проверяет наличие указанных средств у пользователя
            if ((Integer) userInfo.get(0) < transfer) {
                System.out.println("Not enough money!\n");
            } else {

                // Создаёт
                String userQuery = "UPDATE card SET balance = balance - " + transfer + " WHERE number = " + userCardNum;
                String targetQuery = "UPDATE card SET balance = balance + " + transfer + " WHERE number = " + targetCardNum;
                cardDatabase.updateValues(userQuery);
                cardDatabase.updateValues(targetQuery);
                System.out.println("Success\n");

            }
        }

    }

    /**
     * Метод позволяет удалить карточку с параметрами ниже из
     * базы данных
     *
     * @param cardNum   принимает карточку вошедшего в
     *                  аккаунт пользователя
     * @param pin       принимает пин-код от карточки пользователя
     */
    public static void closeAccount(String cardNum, String pin) {
        // Удаление карточки из бд
        CardDatabase cardDatabase = new CardDatabase(databaseName);
        cardDatabase.deleteCard(cardNum, pin);
        System.out.println("\nThe account has been closed!\n");
    }

}
