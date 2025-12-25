package kz.shyngys.view;


import kz.shyngys.context.ApplicationContext;
import kz.shyngys.db.DatabaseMigration;

import java.util.Scanner;

public class MainView {
    public static void run() {
        ApplicationContext applicationContext = ApplicationContext.getInstance();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Главное меню ===");
            System.out.println("1. Управление Labels");
            System.out.println("2. Управление Posts");
            System.out.println("3. Управление Writers");
            System.out.println("4. Применить патчи миграции Liquibase");
            System.out.println("0. Выход");
            System.out.print("Выберите действие: ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> applicationContext.getLabelView().showMenu();
                case "2" -> applicationContext.getPostView().showMenu();
                case "3" -> applicationContext.getWriterView().showMenu();
                case "4" -> DatabaseMigration.migrate();
                case "0" -> {
                    System.out.println("Выход...");
                    ApplicationContext.closeContext();
                    return;
                }
                default -> System.out.println("Неверный ввод, попробуйте снова.");
            }
        }
    }
}
