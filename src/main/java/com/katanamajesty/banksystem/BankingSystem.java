package com.katanamajesty.banksystem;

import com.katanamajesty.banksystem.account.AccountActions;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BankingSystem {

    static final Scanner SCANNER = new Scanner(System.in);
    static boolean loggedIn = false;
    static boolean terminated = false;

    static final ArrayList<String> CARD_NUMBER_DATA = new ArrayList<>();
    static final ArrayList<String> CARD_PIN_DATA = new ArrayList<>();
    int userNum = 0;
    String cardNumber;
    String cardPin;

    public static void menuOptions() {

        String firstOption;
        String secondOption;
        final String EXIT_OPTION = "0. Exit";

        if (loggedIn) { // проверяет, вошёл ли пользователь в систему

            firstOption = "1. Balance";
            secondOption = "2. Log out";

        } else { // предлагает войти в систему, если loggedIn = false

            firstOption = "1. Create an account";
            secondOption = "2. Log into account";

        }
        System.out.println(firstOption + "\n" + secondOption + "\n" + EXIT_OPTION);

    }

    public static void main(String[] args) {

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

        // заносит данные о карточке в листы
        CARD_NUMBER_DATA.add(userNum, cardNumber);
        CARD_PIN_DATA.add(userNum, cardPin);
        userNum++;

    }

    public void loggingIn() {

        System.out.println("\nEnter your card number:");
        String userCardNumber = SCANNER.nextLine(); // value
        // принимает номер карточки

        System.out.println("Enter your PIN:");
        String userCardPin = SCANNER.nextLine(); // key
        // принимает пин-код карточки

        if (CARD_NUMBER_DATA.contains(userCardNumber) && CARD_NUMBER_DATA.indexOf(userCardNumber) == CARD_PIN_DATA.indexOf(userCardPin)) {

            loggedIn = true;

            System.out.println("\nYou have successfully logged in!\n");

            while (loggedIn) { // блок меню для авторизованного пользователя
                menuOptions();
                switch (Integer.parseInt(SCANNER.nextLine())) {
                    case 1:
                        AccountActions.balance();
                        continue;
                    case 2:
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