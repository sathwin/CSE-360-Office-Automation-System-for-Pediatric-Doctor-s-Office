package application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
    private String senderId;
    private String receiverId;
    private String content;
    private LocalDateTime timestamp;

    // Original constructor
    public Message(String senderId, String receiverId, String content) {
        this(senderId, receiverId, content, LocalDateTime.now()); // Delegating to the main constructor
    }

    // New constructor that also accepts timestamp
    public Message(String senderId, String receiverId, String content, LocalDateTime timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }

    public String formatForSaving() {

        return " " + content;
    }

    public static Message parseFromSavedString(String savedString) {
        String[] parts = savedString.split("\\|");
        if (parts.length < 4) return null;
        String senderId = parts[0];
        String receiverId = parts[1];
        LocalDateTime timestamp;
        try {
            timestamp = LocalDateTime.parse(parts[2], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return null; // Or handle the error appropriately
        }
        String content = parts[3];
        return new Message(senderId, receiverId, content, timestamp);
    }
}
