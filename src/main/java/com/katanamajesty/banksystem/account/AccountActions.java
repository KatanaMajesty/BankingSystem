package com.katanamajesty.banksystem.account;

import java.util.Scanner;

import static com.katanamajesty.banksystem.BankingSystem.cardDatabase;

public class AccountActions {

    private static Integer userBalance;
    private static final Scanner scanner = new Scanner(System.in);

    public static void balance(String cardNumber) {

        String query = "SELECT balance FROM card WHERE number = " + cardNumber;
        userBalance = (Integer) cardDatabase.getCardInfo(query).get(0);
        System.out.println("\nBalance: " + userBalance + "\n");

    }

    @SuppressWarnings("all")
    public static void addIncome() { // FIXME: 06.07.2021 // сделать через бд
        System.out.println("\nEnter income:");

        Integer add = scanner.nextInt();
        userBalance += add;

        System.out.println("Income was added!\n");
    }

    public static void transferAction() {


    }

    public static void closeAccount(String cardNum, String pin) {
        // Удаление карточки из бд
        cardDatabase.deleteCard(cardNum, pin);
        System.out.println("\nThe account has been closed!\n");
    }

}
