package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Class for handling messages in the application.
public class Message {
    // Variables to store message details.
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime timestamp;

    // Constructor to create a message without specifying the timestamp.
    // It automatically sets the timestamp to the current time.
    public Message(String senderId, String receiverId, String content) {
        this(senderId, receiverId, content, LocalDateTime.now()); // Calls the full constructor.
    }

    // Full constructor that accepts all details including the timestamp.
    public Message(String senderId, String receiverId, String content, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getters for all the message details.
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // Method to format the message for saving, e.g., to a file or database.
    public String formatForSaving() {
        // Currently returns content only. Should be updated to include all details.
        return " " + content;
    }

    // Static method to parse a message from a saved string.
    // Assumes the string is formatted with parts separated by "|".
    public static Message parseFromSavedString(String savedString) {
        // Splits the string into parts.
        String[] parts = savedString.split("\\|");
        // Checks if the string has all required parts.
        if (parts.length < 4) return null;

        // Extracts individual parts.
        String senderId = parts[0];
        String receiverId = parts[1];
        LocalDateTime timestamp;
        // Parses the timestamp.
        try {
            timestamp = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            // Returns null if parsing fails.
            return null; 
        }
        String content = parts[3];

        // Creates a new Message object with the parsed details.
        return new Message(senderId, receiverId, content, timestamp);
    }
}

