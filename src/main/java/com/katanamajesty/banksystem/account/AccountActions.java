package com.katanamajesty.banksystem.account;

public class AccountActions {

    static Integer userBalance = 0;

    public static void balance() {

        System.out.println("\nBalance: " + userBalance + "\n");

    }

    public static Integer getUserBalance() {

        return userBalance;

    }

}
