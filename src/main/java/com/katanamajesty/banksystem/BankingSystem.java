package com.katanamajesty.banksystem;

import com.katanamajesty.banksystem.account.AccountActions;
import com.katanamajesty.banksystem.bank.CardDatabase;

import java.util.Random;
import java.util.Scanner;

public class BankingSystem {

    private static final Scanner SCANNER = new Scanner(System.in);
    static boolean loggedIn = false;
    static boolean terminated = false;

    public static String dataBaseName = "bankcard.db";
    public static CardDatabase cardDatabase = new CardDatabase(dataBaseName);

    static int userNum = 0;
    static String cardNumber;
    static String cardPin;

    public static void menuOptions() {

        String firstOption;
        String secondOption;
        String thirdOption;
        String fourthOption;
        String fifthOption;

        final String EXIT_OPTION = "0. Exit";

        if (loggedIn) { // проверяет, вошёл ли пользователь в систему

            firstOption = "1. Balance";
            secondOption = "2. Add income";
            thirdOption = "3. Do transfer";
            fourthOption = "4. Close account";
            fifthOption = "5. Log out";
            System.out.println(firstOption + System.lineSeparator() +
                    secondOption + System.lineSeparator() +
                    thirdOption + System.lineSeparator() +
                    fourthOption + System.lineSeparator() +
                    fifthOption + System.lineSeparator());

        } else { // предлагает войти в систему, если loggedIn = false

            firstOption = "1. Create an account";
            secondOption = "2. Log into account";
            System.out.println(firstOption + System.lineSeparator() +
                    secondOption + System.lineSeparator() +
                    EXIT_OPTION);

        }

    }

    public static void main(String[] args) {
        // определяет имя бд, если оно отличается от дефолтного
        if (args.length >= 2 && "-fileName".equals(args[0])) {
            dataBaseName = args[1];
            cardDatabase = new CardDatabase(dataBaseName);
        }

        // начинает подключение к бд
        cardDatabase.establishConnection();

        // запускает начальное меню для пользователя и зацикливает его
        while (!terminated) {
            menuOptions();
            BankingSystem bankingSystem = new BankingSystem();
            bankingSystem.application(Integer.parseInt(SCANNER.nextLine()));
        }

    }

    public void application(Integer input) {

        switch (input) {
            case 1: // создание аккаунта / проверка баланса
                cardCreation();
                break;
            case 2: // вход / выход из акканута
                loggingIn();
                break;
            case 0: // выход из системы
                System.out.println("Bye!");
                terminated = true;
                break;
        }

    }

    public void cardCreation() {

        Random random = new Random();
        String accountIdentifier = "";
        cardNumber = "400000";
        cardPin = "";

        for (int i = 0; i < 9; i++) {
            int num = random.nextInt(10);
            accountIdentifier = accountIdentifier.concat(String.valueOf(num));
            // создаёт номер карточки из 16 значений
        }

        for (int i = 0; i < 4; i++) {
            cardPin = cardPin.concat(String.valueOf(random.nextInt(10)));
            // создаёт рандомный пин для карточки
        }

        // алгоритм Луна
        cardNumber = cardNumber.concat(accountIdentifier);
        char[] intChars = cardNumber.toCharArray();
        int checksum = 0;
        int cardNumSum = 0;
        int cardNum;
        for (int i = 0; i < intChars.length; i++) {
            // умножает числа нечётного номера цифры на карточке на 2
            cardNum = Integer.parseInt(Character.toString(intChars[i]));
            if (i % 2 == 0) {
                cardNum = cardNum * 2;
            }
            // отнимает 9 от чисел > 9
            if (cardNum > 9) {
                cardNum -= 9;
            }
            cardNumSum += cardNum;
        }
        while ((cardNumSum + checksum) % 10 != 0) {
            checksum++;
        }
        String checksumString = Integer.toString(checksum);
        cardNumber = cardNumber.concat(checksumString);

        // вывод данных о карточке
        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(cardPin + "\n");

        // вносит данные о карточке в базу данных
        userNum++;
        cardDatabase.insertValues(new Object[]{userNum, cardNumber, cardPin});

    }

    public void loggingIn() {

        System.out.println("\nEnter your card number:");
        String userCardNumber = SCANNER.nextLine(); // value
        // принимает номер карточки

        System.out.println("Enter your PIN:");
        String userCardPin = SCANNER.nextLine(); // key
        // принимает пин-код карточки

        String query = "SELECT * FROM card " +
                "WHERE number = " + userCardNumber +
                " AND pin = " + userCardPin;

        if (!cardDatabase.getCardInfo(query).isEmpty()) {

            loggedIn = true;

            System.out.println("\nYou have successfully logged in!\n");

            while (loggedIn) { // блок меню для авторизованного пользователя
                menuOptions();
                switch (Integer.parseInt(SCANNER.nextLine())) {
                    case 1:
                        AccountActions.balance(cardNumber);
                        break;
                    case 2:
                        AccountActions.addIncome();
                        break;
                    case 3:
                        System.out.println("bababooey");
                        break;
                    case 4:
                        AccountActions.closeAccount(userCardNumber, userCardPin);
                        loggedIn = false;
                        break;
                    case 5:
                        loggingOut();
                        break;
                    case 0:
                        System.out.println("\nBye!");
                        terminated = true;
                        loggedIn = false;
                        break;
                }
            }


        } else {
            System.out.println("\nWrong card number or PIN!\n");
        }

    }

    public void loggingOut() {

        loggedIn = false;
        System.out.println("\nYou have successfully logged out!\n");

    }

}