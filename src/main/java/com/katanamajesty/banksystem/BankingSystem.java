package com.katanamajesty.banksystem;

import com.katanamajesty.banksystem.account.AccountActions;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BankingSystem {

    static final Scanner SCANNER = new Scanner(System.in);
    static boolean loggedIn;
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
        cardNumber = "400000";
        cardPin = "";

        for (int i = 0; i < 4; i++) {
            cardPin = cardPin.concat(String.valueOf(random.nextInt(10)));
            // создаёт рандомный пин для карточки
        }

        for (int i = 0; i < 10; i++) {
            int num = random.nextInt(10);
            cardNumber = cardNumber.concat(String.valueOf(num));
            // создаёт номер карточки из 16 значений
        }

        System.out.println();
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(cardPin + "\n");
        // возвращает данные о новой карточке

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
        System.out.println("You have successfully logged out!");

    }

}