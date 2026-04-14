package com.mycompany.QuickChat;

import java.util.Scanner;

public class QuickChat {

    private static Scanner scanner = new Scanner(System.in);

    public static String menu() {
        System.out.println("\n1. Login");
        System.out.println("2. Exit");
        System.out.print(": ");
        return scanner.nextLine();
    }

    public static void main(String[] args) {

        while (true) {
            String choice = menu();

            switch (choice) {
                case "1":
                    Login.loginUser(); // ✅ LINKED HERE
                    break;

                case "2":
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
