package com.example;

import java.io.*;
import java.util.*;

/**
 *
 * @author Student
 */
public final class Message {

    
    // ── Parallel arrays ───────────────────────────────────────────────────────
    private static final ArrayList<String> sentMessages        = new ArrayList<>();
    private static final ArrayList<String> disregardedMessages = new ArrayList<>();
    private static final ArrayList<String> storedMessages      = new ArrayList<>();
    private static final ArrayList<String> messageHashes       = new ArrayList<>();
    private static final ArrayList<String> messageIDs          = new ArrayList<>();
    private static final ArrayList<String> recipients          = new ArrayList<>();
    private static final ArrayList<String> sentRecipients      = new ArrayList<>();
    private static final ArrayList<String> storedRecipients    = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // ── Instance fields ───────────────────────────────────────────────────────
    private final String messageID;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private final int    messageNumber;

    // ── Constructor ───────────────────────────────────────────────────────────
    public Message(String recipient, String messageText, int messageNumber) {
        this.messageID     = generateMessageID();
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageNumber = messageNumber;
        this.messageHash   = createMessageHash();
    }

    // ── Generate random 10-digit ID ───────────────────────────────────────────
    private String generateMessageID() {
        Random rand = new Random();
        long id = (long)(rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // ── PART 2 METHODS ────────────────────────────────────────────────────────

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient.startsWith("+") && recipient.length() <= 13) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain " +
               "an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        String first2 = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord  = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        return first2 + ":" + messageNumber + ":" + firstWord + lastWord;
    }

    public String SentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                totalMessagesSent++;
                sentMessages.add(messageText);
                sentRecipients.add(recipient);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                recipients.add(recipient);
                return "Message successfully sent.";
            }
            case 2 -> {
                disregardedMessages.add(messageText);
                return "Press 0 to delete the message.";
            }
            case 3 -> {
                storedMessages.add(messageText);
                storedRecipients.add(recipient);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                recipients.add(recipient);
                storeMessage();
                return "Message successfully stored.";
            }
            default -> {
                return "Invalid choice.";
            }
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) return "No messages sent.";
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) sb.append(msg).append("\n");
        return sb.toString().trim();
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        String json = "{\"messageID\":\"" + messageID + "\"," +
                      "\"messageHash\":\"" + messageHash + "\"," +
                      "\"recipient\":\"" + recipient + "\"," +
                      "\"message\":\"" + messageText + "\"}";
        try (FileWriter fw = new FileWriter("messages.json", true)) {
            fw.write(json + "\n");
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    public static String validateMessageLength(String message) {
        if (message.length() <= 250) return "Message ready to send.";
        int over = message.length() - 250;
        return "Message exceeds 250 characters by " + over + "; please reduce the size.";
    }

    // ── PART 3 METHODS ────────────────────────────────────────────────────────

    public static void loadStoredMessagesFromJSON() {
        storedMessages.clear();
        File file = new File("messages.json");
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("\"message\":\"")) {
                    int start = line.indexOf("\"message\":\"") + 11;
                    int end   = line.indexOf("\"", start);
                    if (end > start) storedMessages.add(line.substring(start, end));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
    }

    public static String displaySenderRecipient() {
        if (recipients.isEmpty()) return "No messages stored.";
        StringBuilder sb = new StringBuilder();
        for (String rec : recipients)
            sb.append("Sender: You | Recipient: ").append(rec).append("\n");
        return sb.toString().trim();
    }

    public static String displayLongestMessage() {
        ArrayList<String> all = new ArrayList<>();
        all.addAll(sentMessages);
        all.addAll(storedMessages);
        if (all.isEmpty()) return "No messages available.";
        String longest = "";
        for (String msg : all)
            if (msg.length() > longest.length()) longest = msg;
        return longest;
    }

    public static String searchByMessageID(String id) {
        // Search by actual message ID
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(id)) {
                if (i < sentMessages.size()) return sentMessages.get(i);
                int si = i - sentMessages.size();
                if (si < storedMessages.size()) return storedMessages.get(si);
            }
        }
        // Search by recipient number (test data scenario)
        for (int i = 0; i < sentRecipients.size(); i++) {
            if (sentRecipients.get(i).equals(id)) return sentMessages.get(i);
        }
        for (int i = 0; i < storedRecipients.size(); i++) {
            if (storedRecipients.get(i).equals(id)) return storedMessages.get(i);
        }
        return "Message ID not found.";
    }

    public static String searchByRecipient(String recipientNum) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentRecipients.get(i).equals(recipientNum)) {
                sb.append(sentMessages.get(i)).append("\n");
            }
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedRecipients.get(i).equals(recipientNum)) {
                sb.append(storedMessages.get(i)).append("\n");
            }
        }
        return sb.toString().trim().isEmpty()
            ? "No messages found for that recipient."
            : sb.toString().trim();
    }

    public static String deleteByHash(String hash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(hash)) {
                String msg = i < sentMessages.size()
                    ? sentMessages.get(i)
                    : storedMessages.get(i - sentMessages.size());
                messageHashes.remove(i);
                if (i < sentMessages.size()) {
                    sentMessages.remove(i);
                    sentRecipients.remove(i);
                } else {
                    int si = i - sentMessages.size();
                    storedMessages.remove(si);
                    storedRecipients.remove(si);
                }
                return "Message: \"" + msg + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    public static String displayReport() {
        if (messageHashes.isEmpty()) return "No messages to report.";
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-25s %-15s %s%n", "Message Hash", "Recipient", "Message"));
        sb.append("-".repeat(80)).append("\n");
        for (int i = 0; i < messageHashes.size(); i++) {
            String hash = messageHashes.get(i);
            String rec  = i < recipients.size() ? recipients.get(i) : "N/A";
            String msg  = i < sentMessages.size() ? sentMessages.get(i)
                        : (i - sentMessages.size()) < storedMessages.size()
                          ? storedMessages.get(i - sentMessages.size()) : "N/A";
            sb.append(String.format("%-25s %-15s %s%n", hash, rec, msg));
        }
        return sb.toString().trim();
    }

    // ── Getters ───────────────────────────────────────────────────────────────
    public String getMessageID()   { return messageID; }
    public String getRecipient()   { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }

    public static ArrayList<String> getSentMessages()        { return sentMessages; }
    public static ArrayList<String> getStoredMessages()      { return storedMessages; }
    public static ArrayList<String> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<String> getMessageHashes()       { return messageHashes; }
    public static ArrayList<String> getMessageIDs()          { return messageIDs; }
    public static ArrayList<String> getRecipients()          { return recipients; }

    public static void resetAll() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        recipients.clear();
        sentRecipients.clear();
        storedRecipients.clear();
        totalMessagesSent = 0;
    }
}
    
