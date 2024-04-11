package application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// Class to manage messaging between patients and doctors.
public class MessagingPortal {
    // Text area for displaying messages.
    private TextArea messageDisplayArea;
    // Input fields for patient ID and message text.
    private TextField patientIdInput, messageInputField;
    // Buttons for various actions.
    private Button loadButton, sendButton, callButton;
    // Directory path for data storage.
    String directoryPath = System.getProperty("user.dir");
    // Path where messages are stored.
    private final String dataDirectoryPath = directoryPath + "/";
    // Identifier for the doctor.
    private final String doctorId = "DOCTOR";
    // Flag to indicate if the user is a doctor.
    private boolean isDoctor;

    // Builds the top panel with patient ID input and action buttons.
    private VBox createTopPanel() {
        VBox topPanel = new VBox(10);
        patientIdInput = new TextField();
        patientIdInput.setPromptText("Enter Patient ID");
        loadButton = new Button("Load Messages");
        loadButton.setOnAction(e -> loadMessages());
        callButton = new Button("Initiate Call");
        callButton.setOnAction(e -> initiateCall());
        
        topPanel.getChildren().addAll(new Label("Patient ID:"), patientIdInput, loadButton, callButton);
        return topPanel;
    }

    // Initiates a call to the patient, showing an alert.
    private Object initiateCall() {
        String patientId = patientIdInput.getText().trim();
        
        if (patientId.isEmpty()) {
            showAlert("Patient ID cannot be empty");
        } else {
            showAlert("CALLING...");
        }
        
        return null;
    }

    // Creates a scroll pane for displaying messages.
    private ScrollPane createMessageDisplayArea() {
        messageDisplayArea = new TextArea();
        messageDisplayArea.setEditable(false);
        return new ScrollPane(messageDisplayArea);
    }

    // Creates the bottom panel for message input and the send button.
    private HBox createBottomPanel() {
        HBox bottomPanel = new HBox(10);
        messageInputField = new TextField();
        messageInputField.setPromptText("Enter your message...");
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());
        
        bottomPanel.getChildren().addAll(messageInputField, sendButton);
        return bottomPanel;
    }

    // Handles sending of a message.
    private void sendMessage() {
        String patientId = patientIdInput.getText().trim();
        String messageContent = messageInputField.getText().trim();

        if (patientId.isEmpty() || messageContent.isEmpty()) {
            showAlert("Patient ID and message content cannot be empty.");
            return;
        }

        try {
            Path path = Paths.get(dataDirectoryPath, patientId + "_" + doctorId + "_messages.txt");
            String senderPrefix = isDoctor ? "Doctor" : "Patient";
            // Construct message format.
            Files.writeString(path, senderPrefix + ": " + messageContent + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            messageInputField.clear(); // Clear the input field after sending.
            loadMessages(); // Refresh the message display.
        } catch (IOException e) {
            showAlert("Failed to send message: " + e.getMessage());
        }
    }

    // Loads and displays messages for a specific patient.
    private void loadMessages() {
        String patientId = patientIdInput.getText().trim();
        Path path = Paths.get(dataDirectoryPath, patientId + "_" + doctorId + "_messages.txt");

        if (!Files.exists(path)) {
            messageDisplayArea.setText("No messages found.");
            return;
        }

        try {
            List<String> lines = Files.readAllLines(path);
            String messages = lines.stream()
                                   .collect(Collectors.joining("\n"));
            messageDisplayArea.setText(messages);
        } catch (IOException e) {
            showAlert("Failed to load messages: " + e.getMessage());
        }
    }

    // Shows an alert with a specific message.
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Displays the messaging portal window.
    public void showMessagingPortal(boolean isDoctor) {
        this.isDoctor = isDoctor;
        
        Stage newStage = new Stage();
        newStage.setTitle("Messaging System");

        BorderPane root = new BorderPane();
        root.setTop(createTopPanel());
        root.setCenter(createMessageDisplayArea());
        root.setBottom(createBottomPanel());

        Scene scene = new Scene(root, 600, 400);
        newStage.setScene(scene);
        newStage.show();
    }
}

