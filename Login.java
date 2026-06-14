package com.example;

import java.io.*;
import java.util.*;

/**
 *
 * @author Student
*/

public class Login {
   private static Scanner scanner = new Scanner(System.in);

    // ── Ensures login.txt file exists ─────────────────────────────────────────
    public static void ensureFileExists() {
        try {
            File file = new File("login.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    // ── Register a new user ───────────────────────────────────────────────────
    public static void registerUser() {
        ensureFileExists();

        System.out.print("Enter a username (no spaces): ");
        String username = scanner.nextLine().trim();

        if (!username.contains("_") || username.length() > 5) {
            System.out.println("Username is not correctly formatted. " +
                "Please ensure that your username contains an underscore " +
                "and is no more than 5 characters in length.");
            return;
        }

        System.out.print("Enter a password: ");
        String password = scanner.nextLine();

        boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> "!@#$%^&*".indexOf(c) >= 0);

        if (password.length() < 8 || !hasUpper || !hasDigit || !hasSpecial) {
            System.out.println("Password is not correctly formatted. " +
                "Please ensure that the password contains at least 8 characters, " +
                "a capital letter, a number, and a special character.");
            return;
        }

        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine().trim();

        try (FileWriter fw = new FileWriter("login.txt", true)) {
            fw.write(username + "\n" + password + "\n" + firstName + " " + lastName + "\n");
            System.out.println("Account successfully created. " +
                "Welcome " + firstName + " " + lastName + "!");
        } catch (IOException e) {
            System.out.println("Error saving user: " + e.getMessage());
        }
    }

    // ── Login an existing user ────────────────────────────────────────────────
    public static boolean loginUser() {
        ensureFileExists();

        System.out.print("Enter your Username: ");
        String username = scanner.nextLine().trim();

        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader("login.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }

            for (int i = 0; i + 1 < lines.size(); i += 3) {
                String storedUser = lines.get(i);
                String storedPass = lines.get(i + 1);
                String storedName = lines.get(i + 2);

                if (username.equals(storedUser) && password.equals(storedPass)) {
                    System.out.println("Welcome " + storedName + ", it is great to see you again.");
                    return true;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        System.out.println("Username or password incorrect, please try again.");
        return false;
    }

    // ── Validation helpers (used in unit tests) ───────────────────────────────
    public static String checkUserName(String username) {
        if (username.contains("_") && username.length() <= 5) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted, please ensure that your " +
               "username contains an underscore and is no more than 5 characters in length.";
    }

    public static String checkPasswordComplexity(String password) {
        boolean hasUpper   = password.chars().anyMatch(Character::isUpperCase);
        boolean hasDigit   = password.chars().anyMatch(Character::isDigit);
        boolean hasSpecial = password.chars().anyMatch(c -> "!@#$%^&*".indexOf(c) >= 0);

        if (password.length() >= 8 && hasUpper && hasDigit && hasSpecial) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted, please ensure that the password " +
               "contains at least 8 characters, a capital letter, a number, and a special character.";
    }
}
