package application;

// MessageThread.java
import java.util.ArrayList;
import java.util.List;

public class MessageThread {
    private List<Message> messages;

    public MessageThread() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return messages;
    }
}
