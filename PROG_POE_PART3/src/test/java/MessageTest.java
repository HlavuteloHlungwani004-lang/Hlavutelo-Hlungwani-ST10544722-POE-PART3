/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Student
 */

package com.mycompany.prog_poe_part3;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Student
 */
public class MessageTest {

    Message m1;
    Message m2;
    Message m3;
    Message m4;
    Message m5;

    // Helper methods: put a message into the right arrays
    private void addToSent(Message m) {
        Message.sentMessages.add(m);
        Message.messageIDs.add(m.getMessageID());
        Message.messageHashes.add(m.getMessageHash());
    }

    private void addToStored(Message m) {
        Message.storedMessages.add(m);
        Message.messageIDs.add(m.getMessageID());
        Message.messageHashes.add(m.getMessageHash());
    }

    // Runs before every test: empties the arrays and loads the test data
    @Before
    public void setUp() {
        Message.sentMessages.clear();
        Message.storedMessages.clear();
        Message.disregardedMessages.clear();
        Message.messageHashes.clear();
        Message.messageIDs.clear();

        m1 = new Message("1000000001", 1, "+27834557896", "Did you get the cake?");
        m2 = new Message("1000000002", 2, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m3 = new Message("1000000003", 3, "+27834484567", "Yohoooo, I am at your gate.");
        m4 = new Message("0838884567", 4, "0838884567", "It is dinner time!");
        m5 = new Message("1000000005", 5, "+27838884567", "Ok, I am leaving without you.");

        addToSent(m1);                           // Message 1 - Sent
        addToStored(m2);                         // Message 2 - Stored
        Message.disregardedMessages.add(m3);     // Message 3 - Disregard
        addToSent(m4);                           // Message 4 - Sent
        addToStored(m5);                         // Message 5 - Stored
    }

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        assertEquals(2, Message.sentMessages.size());
        assertEquals("Did you get the cake?", Message.sentMessages.get(0).getMessage());
        assertEquals("It is dinner time!", Message.sentMessages.get(1).getMessage());
    }

    @Test
    public void testDisplayLongestMessage() {
        String expected = "Where are you? You are late! I have asked you to be on time.";
        assertEquals(expected, Message.longestMessage());
    }

    @Test
    public void testSearchForMessageID() {
        Message found = Message.searchByMessageID("0838884567");
        assertEquals("It is dinner time!", found.getMessage());
    }

    @Test
    public void testSearchByRecipient() {
        ArrayList<String> results = Message.searchByRecipient("+27838884567");

        assertEquals(2, results.size());
        assertEquals("Where are you? You are late! I have asked you to be on time.", results.get(0));
        assertEquals("Ok, I am leaving without you.", results.get(1));
    }

    @Test
    public void testDeleteMessageUsingHash() {
        String expected = "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.";
        String result = Message.deleteByHash(m2.getMessageHash());

        assertEquals(expected, result);
        assertEquals(1, Message.storedMessages.size());
    }

    @Test
    public void testDisplayReport() {
        String report = Message.printMessages();

        assertEquals(true, report.contains(m1.getMessageHash()));
        assertEquals(true, report.contains("+27834557896"));
        assertEquals(true, report.contains("Did you get the cake?"));

        assertEquals(true, report.contains(m4.getMessageHash()));
        assertEquals(true, report.contains("0838884567"));
        assertEquals(true, report.contains("It is dinner time!"));
    }
}