package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.nio.file.*;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MessagingPortal {
    private TextArea messageDisplayArea;
    private TextField patientIdInput, messageInputField;
    private Button loadButton, sendButton, callButton;
    private String dataDirectoryPath = "/Users/gunduabhi1/eclipse-workspace2/CSE360Phase2"; // Default path, consider using a configuration mechanism
    private final String doctorId = "DOCTOR";
    private boolean isDoctor;


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

    private Object initiateCall() {
    	String patientId = patientIdInput.getText().trim();
    	
    	if (patientId.isEmpty()) {
        	showAlert("Patient ID cannot be empty");
        } else {
        	showAlert("CALLING...");
        }
    	
		return null;
	}

	private ScrollPane createMessageDisplayArea() {
        messageDisplayArea = new TextArea();
        messageDisplayArea.setEditable(false);
        return new ScrollPane(messageDisplayArea);
    }

    private HBox createBottomPanel() {
        HBox bottomPanel = new HBox(10);
        messageInputField = new TextField();
        messageInputField.setPromptText("Enter your message...");
        sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        bottomPanel.getChildren().addAll(messageInputField, sendButton);
        return bottomPanel;
    }

    private void sendMessage() {
    	String patientId = patientIdInput.getText().trim();
        String messageContent = messageInputField.getText().trim();

        if (patientId.isEmpty()) {
            showAlert("Patient ID cannot be empty");
            return;
        }
        
        if (messageContent.isEmpty()) {
            showAlert("Message content cannot be empty.");
            return;
        }

        try {
            // Check if a patient file exists for the given patient ID
            boolean patientFileExists = Files.list(Paths.get(dataDirectoryPath))
            		.anyMatch(path -> path.getFileName().toString().startsWith(patientId) && path.getFileName().toString().endsWith(".txt"));

            if (!patientFileExists) {
                showAlert("Patient file does not exist.");
                return;
            }
            
            Path path = Paths.get(dataDirectoryPath, patientId + "_" + doctorId + "_messages.txt");
            String senderPrefix = isDoctor ? "Doctor" : "Patient";
            Message message = new Message(patientId, senderPrefix, messageContent);

            Files.writeString(path, senderPrefix + ": " + message.formatForSaving() + System.lineSeparator(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            messageInputField.clear(); // Clear input field after sending
            loadMessages(); // Refresh message display area
        } catch (IOException e) {
            showAlert("Failed to send message: " + e.getMessage());
        }
    }

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
            	.map(line -> {
                        // Assuming each line starts with "Doctor:" or "Patient:"
                        String[] parts = line.split(":", 2);
                        if (parts.length < 2) return "Invalid message format";
                        // Trim necessary if there are leading spaces after split
                        return parts[0].trim() + ": " + parts[1].trim();
                    })
                 .collect(Collectors.joining("\n"));
            messageDisplayArea.setText(messages);
        } catch (IOException e) {
            showAlert("Failed to load messages: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



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
