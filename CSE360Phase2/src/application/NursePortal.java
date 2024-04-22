package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.nio.file.StandardOpenOption;
import javafx.geometry.HPos;
import javafx.geometry.VPos;

public class NurseView {
    private Stage stage;
    private String patientId;
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/";
    private Map<String, Control> controls = new HashMap<>();
    private CheckBox isChildOver12Checkbox;

    public NurseView(Stage stage, String patientId) {
        this.stage = stage;
        this.patientId = patientId;
        initializeControls();
    }

    private void initializeControls() {
        // Initialize text fields with keys
        String[] textFieldKeys = {
            "PATIENTID", "FIRSTNAME", "LASTNAME", "DOB", "WEIGHT", "HEIGHT", "BODYTEMPERATURE", "BLOODPRESSURE", "KNOWNALLERGIES", "HEALTHCONCERNS"
        };
        for (String key : textFieldKeys) {
            controls.put(key, new TextField());
        }

        // Initialize text areas with keys
        String[] textAreaKeys = {
            "PATIENTHISTORY", "PRESCRIBEDMEDICATIONS", "IMMUNIZATIONRECORDS", "CONTACTINFORMATION"
        };
        for (String key : textAreaKeys) {
            TextArea textArea = new TextArea();
            textArea.setPrefHeight(100);
            controls.put(key, textArea);
        }

        // Initialize checkbox
        isChildOver12Checkbox = new CheckBox("Child over 12");
        controls.put("CHILDOVER12", isChildOver12Checkbox);
    }

    public void displayPatientInfo() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        HBox header = createHeader();
        root.setTop(header);

        GridPane centerGrid = createCenterGrid();
        root.setCenter(centerGrid);

        Scene scene = new Scene(root, 1500, 800);
        stage.setScene(scene);
        stage.setTitle("Nurse Portal - Patient Information");
        stage.show();

        preloadPatientData();
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        header.setPadding(new Insets(15));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView logo = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        logo.setFitHeight(50);
        logo.setPreserveRatio(true);
        header.getChildren().add(logo);

        Label title = new Label("Nurse Portal");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        header.getChildren().add(title);
        
        Button backButton = new Button("Log Out");
        backButton.setOnAction(event -> {
            MainGUI mainGUI = new MainGUI(stage);
            mainGUI.showMainScreen(); // Show the main screen when Back button is pressed
        });
        header.getChildren().add(backButton);

        // Right side of the header for notifications and messages
        HBox rightHeader = new HBox();
        rightHeader.setAlignment(Pos.CENTER_RIGHT);
        ImageView bellIcon = new ImageView(new Image("file:" + imagesDirectoryPath + "/bell.png"));
        bellIcon.setFitHeight(30);
        bellIcon.setPreserveRatio(true);

        Button viewMessagesButton = new Button("", bellIcon);
        viewMessagesButton.setStyle("-fx-background-color: transparent;");
        viewMessagesButton.setOnAction(event -> {
            // Now this matches the constructor and method in MessagingPortal
            MessagingPortal messagingPortal = new MessagingPortal();
            messagingPortal.showMessagingPortal(true);
        });

        Label viewMessagesLabel = new Label("Messages");
        viewMessagesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        rightHeader.getChildren().addAll(viewMessagesLabel, viewMessagesButton);
        HBox.setHgrow(rightHeader, Priority.ALWAYS);

        header.getChildren().add(rightHeader);

        return header;
    }

    private GridPane createCenterGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.TOP_CENTER);

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);

        addLabelAndControl(grid, "Patient ID", "PATIENTID", 0, 0, true);
        addLabelAndControl(grid, "First Name", "FIRSTNAME", 0, 2, false);
        addLabelAndControl(grid, "Last Name", "LASTNAME", 0, 4, false);
        addLabelAndControl(grid, "DOB", "DOB", 0, 6, false);
        addLabelAndControl(grid, "Child over 12", "CHILDOVER12", 0, 8, false);
        addLabelAndControl(grid, "Weight", "WEIGHT", 0, 10, false);
        addLabelAndControl(grid, "Height", "HEIGHT", 0, 12, false);
        addLabelAndControl(grid, "Body Temperature", "BODYTEMPERATURE", 0, 14, false);
        addLabelAndControl(grid, "Blood Pressure", "BLOODPRESSURE", 0, 16, false);

        // Add right side elements with more width for text areas
        addLabelAndControl(grid, "Known Allergies", "KNOWNALLERGIES", 1, 0, false);
        addLabelAndControl(grid, "Health Concerns", "HEALTHCONCERNS", 1, 2, false);
        addLabelAndControl(grid, "Patient History", "PATIENTHISTORY", 1, 4, false);
        addLabelAndControl(grid, "Prescribed Medications", "PRESCRIBEDMEDICATIONS", 1, 7, false);
        addLabelAndControl(grid, "Immunization Records", "IMMUNIZATIONRECORDS", 1, 10, false);
        addLabelAndControl(grid, "Contact Information", "CONTACTINFORMATION", 1, 13, false);

        // Add Save button
        Button saveButton = new Button("Save");
        saveButton.setMaxWidth(100);
        GridPane.setHalignment(saveButton, HPos.RIGHT);
        GridPane.setValignment(saveButton, VPos.BOTTOM);
        grid.add(saveButton, 1, 16);
        saveButton.setOnAction(event -> {
        	
        	if(isValid()) {
        		savePatientInfo();
        	}
        	
        	
        });

        return grid;
    }
    private void addLabelAndControl(GridPane grid, String labelText, String controlKey, int colIndex, int rowIndex, boolean isReadOnly) {
        Label label = new Label(labelText + ":");
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        Control control = controls.get(controlKey);
        
        if (control instanceof TextField) {
            ((TextField) control).setEditable(!isReadOnly);
            ((TextField) control).setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(control, Priority.ALWAYS); // Allow TextField to grow
        }
        if (control instanceof TextArea) {
            ((TextArea) control).setEditable(!isReadOnly);
            ((TextArea) control).setPrefHeight(100);
            ((TextArea) control).setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(control, Priority.ALWAYS); // Allow TextArea to grow
            GridPane.setValignment(label, VPos.TOP); // Align label to top
            GridPane.setRowSpan(control, 2); // TextArea takes 2 rows in height
        }
        if (control instanceof CheckBox) {
            ((CheckBox) control).setAllowIndeterminate(false);
        }

        grid.add(label, colIndex, rowIndex);
        grid.add(control, colIndex, rowIndex + 1); // Add control below the label
        GridPane.setMargin(label, new Insets(0, 0, 0, 3)); // Add some spacing between label and control
    }

    private void savePatientInfo() {
        Path filePath = Paths.get(imagesDirectoryPath, patientId + ".txt");
        List<String> lines = new ArrayList<>();

        controls.forEach((key, value) -> {
            if (value instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) value;
                lines.add(key + ": " + (checkBox.isSelected() ? "Yes" : "No"));
            } else if (value instanceof TextInputControl) {
                TextInputControl textInput = (TextInputControl) value;
                lines.add(key + ": " + textInput.getText());
            }
        });

        try {
            Files.write(filePath, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            showAlert("Patient information saved successfully.");
        } catch (IOException e) {
            showAlert("Error saving patient data: " + e.getMessage());
        }
    }

    private void preloadPatientData() {
        Path filePath = Paths.get(imagesDirectoryPath, patientId + ".txt");
        if (Files.exists(filePath)) {
            try {
                List<String> lines = Files.readAllLines(filePath);
                lines.forEach(line -> {
                    String[] parts = line.split(": ", 2);
                    if (parts.length == 2) {
                        String key = parts[0].replaceAll("\\s+", "").toUpperCase();
                        String value = parts[1];
                        Control control = controls.get(key);
                        if (control instanceof CheckBox) {
                            ((CheckBox) control).setSelected("Yes".equals(value));
                        } else if (control instanceof TextInputControl) {
                            ((TextInputControl) control).setText(value);
                        }
                    }
                });
            } catch (IOException e) {
                showAlert("Error loading patient data: " + e.getMessage());
            }
        }
    }
    
    private boolean isValid() {
    	TextField heightTextArea = (TextField) controls.get("HEIGHT");
	    String height = heightTextArea.getText();
	    TextField weightTextArea = (TextField) controls.get("WEIGHT");
	    String weight = weightTextArea.getText();
	    TextField tempTextArea = (TextField) controls.get("BODYTEMPERATURE");
	    String temp = tempTextArea.getText();
	    TextField bpTextArea = (TextField) controls.get("BLOODPRESSURE");
	    String bloodpres = bpTextArea.getText();
	    
	    if(weight.isEmpty()) {
	    	showAlert("Please enter the Weight of the patient");
	    	return false;
	    }
	    if(height.isEmpty()) {
	    	showAlert("Please enter the Height of patient");
	    	return false;
	    }
	    if(temp.isEmpty()) {
	    	showAlert("Please enter the Body Temperatur of the patient");
	    	return false;
	    }
	    if(bloodpres.isEmpty()) {
	    	showAlert("Please enter the Blood Pressure of the patient");
	    	return false;
	    }
	    return true;
	    
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
