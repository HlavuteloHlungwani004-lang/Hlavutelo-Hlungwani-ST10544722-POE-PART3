/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog_poe_part3;

/**
 *
 * @author Student
 */


/**
 *
 * @author Student
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Message {

    private static int totalMessages = 0;
    public static ArrayList<Message> sentMessages = new ArrayList<>();
    public static ArrayList<Message> storedMessages = new ArrayList<>();
    public static ArrayList<Message> disregardedMessages = new ArrayList<>();
    public static ArrayList<String> messageHashes = new ArrayList<>();
    public static ArrayList<String> messageIDs = new ArrayList<>();

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    // Part 2 constructor: creates a random message ID
    public Message(int messageNumber, String recipient, String message) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // NEW (Part 3): lets us give the message a known ID
    // (used when reading from the JSON file and in the unit tests)
    public Message(String messageID, int messageNumber, String recipient, String message) {
        this.messageID = messageID;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // NEW (Part 3): getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    private String generateMessageID() {
        Random random = new Random();
        long number = 1000000000L + (long)(random.nextDouble() * 9000000000L);
        return String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient.startsWith("+27") && recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        if (message.length() <= 250) {
            return "Message ready to send.";
        } else {
            int difference = message.length() - 250;
            return "Message exceeds 250 characters by " + difference + " characters. Please reduce the size.";
        }
    }

    public String createMessageHash() {
        String[] words = message.trim().split("\\s+");
        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return messageID.substring(0, 2) + ":" + messageNumber + ":" + (firstWord + lastWord).toUpperCase();
    }

    public String sentMessage() {
        Scanner input = new Scanner(System.in);

        System.out.println("\nChoose an option:");
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message");
        System.out.print("Enter choice: ");

        int choice = Integer.parseInt(input.nextLine());

        switch (choice) {
            case 1:
                totalMessages++;
                sentMessages.add(this);
                messageIDs.add(messageID);
                messageHashes.add(messageHash);
                return "Message successfully sent.";

            case 2:
                disregardedMessages.add(this);
                return "Message disregarded.";

            case 3:
                storedMessages.add(this);
                messageIDs.add(messageID);
                messageHashes.add(messageHash);
                storeMessage();
                return "Message successfully stored.";

            default:
                return "No valid option selected.";
        }
    }

    // CHANGED (Part 3): now uses the report() method below
    public static String printMessages() {
        return report(sentMessages);
    }

    public static int returnTotalMessages() {
        return totalMessages;
    }

    // NEW (Part 3): builds the JSON text for one message
    private String toJson() {
        return "{\n" +
                "  \"MessageID\": \"" + this.messageID + "\",\n" +
                "  \"MessageNumber\": " + this.messageNumber + ",\n" +
                "  \"Recipient\": \"" + this.recipient + "\",\n" +
                "  \"Message\": \"" + this.message + "\",\n" +
                "  \"MessageHash\": \"" + this.messageHash + "\"\n" +
                "}";
    }

    public void storeMessage() {
        try {
            FileWriter myWriter = new FileWriter("stored_messages.json", true);
            myWriter.write(toJson() + ",\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing to file.");
        }
    }

    // ===================== PART 3 =====================

    // Reads stored_messages.json into the storedMessages array.
    // Call this ONCE when the program starts.
    public static void loadStoredMessages() {
        storedMessages.clear();

        File file = new File("stored_messages.json");
        if (!file.exists()) {
            return;
        }

        String id = "";
        int number = 0;
        String recipient = "";
        String text = "";

        try {
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine().trim();

                if (line.startsWith("\"MessageID\"")) {
                    id = getJsonValue(line);
                } else if (line.startsWith("\"MessageNumber\"")) {
                    number = Integer.parseInt(getJsonValue(line));
                } else if (line.startsWith("\"Recipient\"")) {
                    recipient = getJsonValue(line);
                } else if (line.startsWith("\"Message\"")) {
                    text = getJsonValue(line);
                } else if (line.startsWith("\"MessageHash\"")) {
                    // last line of one message: build it and add it to the arrays
                    Message loaded = new Message(id, number, recipient, text);
                    storedMessages.add(loaded);
                    messageIDs.add(loaded.messageID);
                    messageHashes.add(loaded.messageHash);
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Could not read stored_messages.json.");
        }
    }

    // Takes a line like:   "Recipient": "+27838884567",
    // and returns:         +27838884567
    private static String getJsonValue(String line) {
        int colon = line.indexOf(":");
        String value = line.substring(colon + 1).trim();

        if (value.endsWith(",")) {
            value = value.substring(0, value.length() - 1);
        }
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    
    public static void saveAllToFile() {
        try {
            FileWriter myWriter = new FileWriter("stored_messages.json", false);
            for (int i = 0; i < storedMessages.size(); i++) {
                myWriter.write(storedMessages.get(i).toJson() + ",\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing to file.");
        }
    }

    
    public static String displaySenderAndRecipient() {
        if (storedMessages.size() == 0) {
            return "No stored messages.";
        }

        String result = "";
        for (int i = 0; i < storedMessages.size(); i++) {
            Message current = storedMessages.get(i);
            result += "Sender: You | Recipient: " + current.recipient + "\n";
        }
        return result;
    }

    // 2b. Longest stored message
    public static String longestMessage() {
        if (storedMessages.size() == 0) {
            return "No stored messages.";
        }

        Message longest = storedMessages.get(0);
        for (int i = 1; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).message.length() > longest.message.length()) {
                longest = storedMessages.get(i);
            }
        }
        return longest.message;
    }

    // 2c. Search for a message ID (returns null if not found)
    public static Message searchByMessageID(String id) {
        if (!messageIDs.contains(id)) {
            return null;
        }

        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).messageID.equals(id)) {
                return sentMessages.get(i);
            }
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).messageID.equals(id)) {
                return storedMessages.get(i);
            }
        }
        return null;
    }

    // 2d. All messages (sent or stored) for one recipient
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> found = new ArrayList<>();

        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i).recipient.equals(recipient)) {
                found.add(sentMessages.get(i).message);
            }
        }
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).recipient.equals(recipient)) {
                found.add(storedMessages.get(i).message);
            }
        }
        return found;
    }

    // 2e. Delete a stored message using its hash
    public static String deleteByHash(String hash) {
        for (int i = 0; i < storedMessages.size(); i++) {
            Message current = storedMessages.get(i);

            if (current.messageHash.equals(hash)) {
                String text = current.message;
                storedMessages.remove(i);
                messageHashes.remove(hash);
                messageIDs.remove(current.messageID);
                return "Message: \"" + text + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    // 2f. Report with the full details of the messages in a list
    public static String report(ArrayList<Message> list) {
        if (list.size() == 0) {
            return "No messages to display.";
        }

        String allDetails = "";
        for (int i = 0; i < list.size(); i++) {
            Message current = list.get(i);
            allDetails += "Message ID: " + current.messageID + "\n" +
                          "Message Hash: " + current.messageHash + "\n" +
                          "Recipient: " + current.recipient + "\n" +
                          "Message: " + current.message + "\n" +
                          "-----------------------------------\n";
        }
        return allDetails;
    }
}