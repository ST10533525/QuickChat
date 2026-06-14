package com.example;

import java.util.Scanner;
/**
 *
 * @author Student
 */
public class QuickChat {

    private static final Scanner scanner  = new Scanner(System.in);
    private static boolean loggedIn = false;

    public static void main(String[] args) {
        System.out.println("Welcome to QuickChat.");

        while (true) {
            System.out.println("\n1) Register");
            System.out.println("2) Login");
            System.out.println("3) Exit");
            System.out.print(": ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> Login.registerUser();
                case "2" -> {
                    loggedIn = Login.loginUser();
                    if (loggedIn) showMainMenu();
                }
                case "3" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // ── Main menu (shown after login) ─────────────────────────────────────────
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print(": ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": sendMessagesFlow(); break;
                case "2": System.out.println("Coming Soon."); break;
                case "3":
                    System.out.println("Goodbye!");
                    System.exit(0);
                case "4": storedMessagesMenu(); break;
                default:  System.out.println("Invalid option.");
            }
        }
    }

    // ── PART 2: Send Messages ─────────────────────────────────────────────────
    private static void sendMessagesFlow() {
        System.out.print("How many messages do you want to send? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number.");
            return;
        }

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " ---");

            // Recipient
            String recipient;
            while (true) {
                System.out.print("Enter recipient cell number (e.g. +27...): ");
                recipient = scanner.nextLine().trim();
                Message temp = new Message(recipient, "test", i);
                String check = temp.checkRecipientCell();
                System.out.println(check);
                if (check.equals("Cell phone number successfully captured.")) break;
            }

            // Message text
            String text;
            while (true) {
                System.out.print("Enter your message (max 250 characters): ");
                text = scanner.nextLine();
                String check = Message.validateMessageLength(text);
                if (check.equals("Message ready to send.")) {
                    System.out.println("Message sent");
                    break;
                } else {
                    System.out.println("Please enter a message of less than 250 characters.");
                }
            }

            // Build message object
            Message msg = new Message(recipient, text, i);
            System.out.println("Message ID generated: " + msg.getMessageID());
            System.out.println("Message Hash: " + msg.getMessageHash());

            // Action sub-menu
            System.out.println("\n1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print(": ");
            int action;
            try {
                action = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                action = 2;
            }

            System.out.println(msg.SentMessage(action));

            // Display full details
            System.out.println("\n--- Message Details ---");
            System.out.println("Message ID:   " + msg.getMessageID());
            System.out.println("Message Hash: " + msg.getMessageHash());
            System.out.println("Recipient:    " + msg.getRecipient());
            System.out.println("Message:      " + msg.getMessageText());
        }

        System.out.println("\nTotal messages sent this session: " + new Message("", "", 0).returnTotalMessagess());
    }

    // ── PART 3: Stored Messages Menu ──────────────────────────────────────────
    private static void storedMessagesMenu() {
        Message.loadStoredMessagesFromJSON();

        while (true) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("a) Display sender and recipient");
            System.out.println("b) Display longest message");
            System.out.println("c) Search by Message ID");
            System.out.println("d) Search by recipient");
            System.out.println("e) Delete by Message Hash");
            System.out.println("f) Display full report");
            System.out.println("x) Back");
            System.out.print(": ");
            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a" -> System.out.println(Message.displaySenderRecipient());
                case "b" -> System.out.println(Message.displayLongestMessage());
                case "c" -> {
                    System.out.print("Enter Message ID: ");
                    System.out.println(Message.searchByMessageID(scanner.nextLine().trim()));
                }
                case "d" -> {
                    System.out.print("Enter recipient number: ");
                    System.out.println(Message.searchByRecipient(scanner.nextLine().trim()));
                }
                case "e" -> {
                    System.out.print("Enter Message Hash: ");
                    System.out.println(Message.deleteByHash(scanner.nextLine().trim()));
                }
                case "f" -> System.out.println(Message.displayReport());
                case "x" -> {
                    return;
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }
}
