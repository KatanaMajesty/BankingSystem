package com.katanamajesty.banksystem;

import com.katanamajesty.banksystem.account.AccountActions;
import com.katanamajesty.banksystem.bank.CardDatabase;

import java.util.Random;
import java.util.Scanner;


/**
 * Основной класс банковской системы
 */
public class BankingSystem {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static boolean loggedIn = false;
    private static boolean terminated = false;

    public static String dataBaseName = "bankcard.db";
    public static CardDatabase cardDatabase = new CardDatabase(dataBaseName);

    @SuppressWarnings("all")
    private static String cardPin;
    private static int userNum = 0;

    /**
     * Меню-панель
     * Проверяет boolean loggedIn
     * и выводит в консоль меню
     * в зависимости от его значения
     */
    public static void menuOptions() {

        String firstOption;
        String secondOption;
        String thirdOption;
        String fourthOption;
        String fifthOption;

        final String EXIT_OPTION = "0. Exit";

        // проверяет, вошёл ли пользователь в систему
        if (loggedIn) {

            firstOption = "1. Balance";
            secondOption = "2. Add income";
            thirdOption = "3. Do transfer";
            fourthOption = "4. Close account";
            fifthOption = "5. Log out";
            System.out.println(firstOption + System.lineSeparator() +
                    secondOption + System.lineSeparator() +
                    thirdOption + System.lineSeparator() +
                    fourthOption + System.lineSeparator() +
                    fifthOption);

        // предлагает войти в систему, если loggedIn = false
        } else {

            firstOption = "1. Create an account";
            secondOption = "2. Log into account";
            System.out.println(firstOption + System.lineSeparator() +
                    secondOption + System.lineSeparator() +
                    EXIT_OPTION);

        }

    }

    /**
     * Главный класс
     *
     * @param args      Принимает 2 значения.
     *                  Если первый аргумент = -fileName,
     *                  то название бд будет соответствовать
     *                  второму аргументу
     */
    public static void main(String[] args) {
        // определяет имя бд, если оно отличается от дефолтного
        if (args.length == 2 && "-fileName".equals(args[0])) {
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

    /**
     * Главное меню при запуске программы.
     * Предлагает создание акканута, вход в существующий
     * и выход
     *
     * @param input     параметр получает Integer,
     *                  который передаётся в Switch и определяет
     *                  выбор пользователя в главном меню
     */
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

    /**
     * Генератор банковской карточки
     * Генерирует рандомный номер карточки, учитывая алгоритм Луна
     * и рандомный пин. Вносит данные в базу данных
     */
    public void cardCreation() {

        Random random = new Random();
        String accountIdentifier = "";
        String cardNumber = "400000";
        cardPin = "";

        // создаёт номер карточки из 16 значений
        for (int i = 0; i < 9; i++) {
            int num = random.nextInt(10);
            accountIdentifier = accountIdentifier.concat(String.valueOf(num));
        }

        // создаёт рандомный пин для карточки
        for (int i = 0; i < 4; i++) {
            cardPin = cardPin.concat(String.valueOf(random.nextInt(10)));
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

    /**
     * Метод входа в аккаунт
     */
    public void loggingIn() {
        CardDatabase cardDatabase = new CardDatabase(dataBaseName);
        Scanner scanner = new Scanner(System.in);

        // принимает номер карточки
        System.out.println("\nEnter your card number:");
        String userCardNumber = scanner.nextLine();

        // принимает пин-код карточки
        System.out.println("Enter your PIN:");
        String userCardPin = scanner.nextLine();

        // Создаёт запрос для бд для проверки на наличие указанных выше данных в ней
        String query = "SELECT * FROM card " +
                "WHERE number = " + userCardNumber +
                " AND pin = " + userCardPin;

        // обработка результата запроса
        if (!cardDatabase.getCardInfo(query).isEmpty()) {

            loggedIn = true;

            System.out.println("\nYou have successfully logged in!\n");
            while (loggedIn) { // блок меню для авторизованного пользователя
                menuOptions();
                switch (Integer.parseInt(scanner.nextLine())) {
                    case 1:
                        // проверка баланса
                        AccountActions.balance(userCardNumber);
                        break;
                    case 2:
                        // добавление баланса
                        AccountActions.addIncome(userCardNumber);
                        break;
                    case 3:
                        // перевод на другую карточку
                        AccountActions.transferAction(userCardNumber);
                        break;
                    case 4:
                        // удаление аккаунта
                        AccountActions.closeAccount(userCardNumber, userCardPin);
                        loggedIn = false;
                        break;
                    case 5:
                        // выход из аккаунта
                        loggingOut();
                        break;
                    case 0:
                        // выход из программы
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

    /**
     * Выход из аккаунта.
     * Доступен только если loggedIn = true
     */
    public void loggingOut() {

        // Прерывает цикл
        loggedIn = false;
        System.out.println("\nYou have successfully logged out!\n");

    }

}