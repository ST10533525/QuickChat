package com.mycompany.QuickChat;

import java.io.*;
import java.util.*;

public class Login {

    private static Scanner scanner = new Scanner(System.in);

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

    public static boolean loginUser() {
        ensureFileExists();

        System.out.print("Enter your Username: ");
        String username = scanner.nextLine();

        System.out.print("Enter your password: ");
        String userpass = scanner.nextLine();

        try (BufferedReader br = new BufferedReader(new FileReader("login.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = br.readLine()) != null) {
                lines.add(line.trim());
            }

            for (int i = 0; i < lines.size(); i += 3) {
                if (i + 1 < lines.size()) {
                    String storedUser = lines.get(i);
                    String storedPass = lines.get(i + 1);

                    if (username.equals(storedUser) && userpass.equals(storedPass)) {
                        System.out.println("Welcome " + username + " it is great to see you again");
                        return true;
                    }
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file");
        }

        System.out.println("Invalid username or password");
        return false;
    }
}
