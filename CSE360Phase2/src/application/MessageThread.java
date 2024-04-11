package application;

// Importing necessary classes for handling collections.
import java.util.ArrayList;
import java.util.List;

// Class to represent a thread of messages, like in a chat or email thread.
public class MessageThread {
    // A list to hold Message objects. This list represents the messages in the thread.
    private List<Message> messages;

    // Constructor for MessageThread. Initializes the list of messages.
    public MessageThread() {
        this.messages = new ArrayList<>();
    }

    // Method to add a new message to the thread.
    public void addMessage(Message message) {
        messages.add(message);
    }

    // Getter method to retrieve all messages in the thread.
    public List<Message> getMessages() {
        return messages;
    }
}

