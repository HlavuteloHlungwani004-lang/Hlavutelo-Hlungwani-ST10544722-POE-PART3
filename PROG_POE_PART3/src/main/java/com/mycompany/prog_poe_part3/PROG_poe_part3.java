/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog_poe_part3;

/**
 *
 * @author Student
 */


import java.util.ArrayList;
import java.util.Scanner;

public class PROG_poe_part3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // NEW (Part 3): read the stored messages from the JSON file into the array
        Message.loadStoredMessages();

        System.out.println("Welcome to QuickChat.");
        boolean keepRunning = true;

        while (keepRunning) {
            System.out.println("\n--- Main Menu ---");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");

            int menuChoice = Integer.parseInt(scanner.nextLine());

            if (menuChoice == 1) {
                System.out.print("How many messages do you wish to enter? ");
                int totalLoops = Integer.parseInt(scanner.nextLine());

                for (int i = 0; i < totalLoops; i++) {
                    int sequenceNum = i + 1;
                    System.out.println("\n--- Message #" + sequenceNum + " ---");

                    System.out.print("Enter cell number: ");
                    String cellNum = scanner.nextLine();

                    System.out.print("Enter text message: ");
                    String textBody = scanner.nextLine();

                    Message messageObject = new Message(sequenceNum, cellNum, textBody);

                    String validationTextResult = messageObject.checkMessageLength();
                    System.out.println(validationTextResult);

                    if (validationTextResult.equals("Message ready to send.")) {
                        String cellFormatResult = messageObject.checkRecipientCell();
                        System.out.println(cellFormatResult);

                        if (cellFormatResult.equals("Cell phone number successfully captured.")) {
                            String promptResult = messageObject.sentMessage();
                            System.out.println(promptResult);
                        }
                    }
                }

                System.out.println("\nTotal number of messages successfully accumulated: " + Message.returnTotalMessages());

            } else if (menuChoice == 2) {
                System.out.println("Coming Soon.");

            } else if (menuChoice == 3) {
                System.out.println("Exiting Application. Goodbye!");
                keepRunning = false;

            } else if (menuChoice == 4) {
                storedMessagesMenu(scanner);

            } else {
                System.out.println("Invalid choice selected.");
            }
        }
        scanner.close();
    }

    // NEW (Part 3): the "Stored Messages" sub-menu
    public static void storedMessagesMenu(Scanner scanner) {
        boolean goBack = false;

        while (!goBack) {
            System.out.println("\n--- Stored Messages ---");
            System.out.println("1) Display sender and recipient of all stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search for all messages to a recipient");
            System.out.println("5) Delete a message using its hash");
            System.out.println("6) Display report of all stored messages");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");

            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.println(Message.displaySenderAndRecipient());

            } else if (choice == 2) {
                System.out.println("Longest message: " + Message.longestMessage());

            } else if (choice == 3) {
                System.out.print("Enter the message ID: ");
                String id = scanner.nextLine();

                Message found = Message.searchByMessageID(id);
                if (found == null) {
                    System.out.println("Message ID not found.");
                } else {
                    System.out.println("Recipient: " + found.getRecipient());
                    System.out.println("Message: " + found.getMessage());
                }

            } else if (choice == 4) {
                System.out.print("Enter the recipient's cell number: ");
                String recipient = scanner.nextLine();

                ArrayList<String> results = Message.searchByRecipient(recipient);
                if (results.size() == 0) {
                    System.out.println("No messages found for that recipient.");
                } else {
                    for (int i = 0; i < results.size(); i++) {
                        System.out.println((i + 1) + ") " + results.get(i));
                    }
                }

            } else if (choice == 5) {
                System.out.print("Enter the message hash: ");
                String hash = scanner.nextLine();

                String deleteResult = Message.deleteByHash(hash);
                System.out.println(deleteResult);

                // keep the JSON file in step with the array
                Message.saveAllToFile();

            } else if (choice == 6) {
                System.out.println(Message.report(Message.storedMessages));

            } else if (choice == 7) {
                goBack = true;

            } else {
                System.out.println("Invalid choice selected.");
            }
        }
    }
}