package application;

// Importing necessary JavaFX classes for UI components, layout management, and file handling
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.File;
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

// Class definition for DoctorView
public class DoctorView {
    private Stage stage;
    private String patientId;
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/";
    private Map<String, Control> controls = new HashMap<>();
    private CheckBox isChildOver12Checkbox;

    // Constructor to initialize the DoctorView with a Stage and patient ID
    public DoctorView(Stage stage, String patientId) {
        this.stage = stage;
        this.patientId = patientId;
        initializeControls();
    }

    // Initializes UI controls to be used in the form
    private void initializeControls() {
        // Initialize text fields for various patient information
        String[] textFieldKeys = {
            "PATIENTID", "FIRSTNAME", "LASTNAME", "DOB", "WEIGHT", "HEIGHT", "BODYTEMPERATURE", "BLOODPRESSURE", "KNOWNALLERGIES", "HEALTHCONCERNS"
        };
        for (String key : textFieldKeys) {
            controls.put(key, new TextField());
        }

        // Initialize text areas for larger text inputs
        String[] textAreaKeys = {
            "PATIENTHISTORY", "PRESCRIBEDMEDICATIONS", "IMMUNIZATIONRECORDS", "CONTACTINFORMATION", "DOCTORFINDINGS", "DOCTORREMARKS"
        };
        for (String key : textAreaKeys) {
            TextArea textArea = new TextArea();
            textArea.setPrefHeight(100);
            controls.put(key, textArea);
        }

        // Initialize checkbox for specific patient conditions
        isChildOver12Checkbox = new CheckBox("Child over 12");
        controls.put("CHILDOVER12", isChildOver12Checkbox);
    }

    // Displays patient information on the UI
    public void displayPatientInfo() {
        // Setting up the main layout pane
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        // Creating the header
        HBox header = createHeader();
        root.setTop(header);

        // Creating the center grid for patient data inputs
        GridPane centerGrid = createCenterGrid();
        root.setCenter(centerGrid);

        // Creating the scene and setting it to the stage
        Scene scene = new Scene(root, 1800, 800);
        stage.setScene(scene);
	stage.setFullScreen(true); // Optional: Make the application full-screen
        stage.setTitle("Doctor Portal - Patient Information");
        
        // Preloading patient data from a file
        preloadPatientData();
        
        // Displaying the stage
        stage.show();
    }

    // Creates the header including the logo, title, and buttons
    private HBox createHeader() {
        HBox header = new HBox();
        header.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        header.setPadding(new Insets(15));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        // Setting up the logo
        ImageView logo = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        logo.setFitHeight(50);
        logo.setPreserveRatio(true);
        header.getChildren().add(logo);

        // Setting up the title
        Label title = new Label("Doctor Portal");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        header.getChildren().add(title);
        
        // Adding back and pharmacy buttons
        Button backButton = new Button("Log Out");
        backButton.setOnAction(event -> new MainGUI(stage).showMainScreen());
        header.getChildren().add(backButton);
        
        Button pharmacyButton = new Button("Send to Pharmacy");
        pharmacyButton.setOnAction(event -> sendToPharmacy());
        header.getChildren().add(pharmacyButton);
        
        // Adding right-aligned header components (e.g., messages)
        HBox rightHeader = new HBox();
        rightHeader.setAlignment(Pos.CENTER_RIGHT);
        ImageView bellIcon = new ImageView(new Image("file:" + imagesDirectoryPath + "/bell.png"));
        bellIcon.setFitHeight(30);
        bellIcon.setPreserveRatio(true);
        
        Button viewMessagesButton = new Button("", bellIcon);
        viewMessagesButton.setStyle("-fx-background-color: transparent;");
        viewMessagesButton.setOnAction(event -> {/* Action to show messages */});

        Label viewMessagesLabel = new Label("Messages");
        viewMessagesLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
        rightHeader.getChildren().addAll(viewMessagesLabel, viewMessagesButton);
        HBox.setHgrow(rightHeader, Priority.ALWAYS);
        
        header.getChildren().add(rightHeader);

        return header;
    }

    // Action to send patient information to pharmacy
    private void sendToPharmacy() {
        showAlert("Sent to Pharmacy.");
    }

    // Creates the center grid for input fields and labels
    private GridPane createCenterGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.TOP_CENTER);

        // Setting column width distribution
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(50);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col1, col2);

        // Adding controls for patient data inputs
        // The method 'addLabelAndControl' is used to add UI components to the grid
        // For brevity, not all calls are commented. Adjust positions as necessary.

        // Save button configuration and action binding
        Button saveButton = new Button("Save");
        saveButton.setMaxWidth(100);
        GridPane.setHalignment(saveButton, HPos.RIGHT);
        GridPane.setValignment(saveButton, VPos.BOTTOM);
        grid.add(saveButton, 1, 18); // Adjust position as necessary
        saveButton.setOnAction(event -> savePatientInfo());

        return grid;
    }

    // Method to add a label and its corresponding control to the grid
    private void addLabelAndControl(GridPane grid, String labelText, String controlKey, int colIndex, int rowIndex, boolean isReadOnly) {
        Label label = new Label(labelText + ":");
        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        Control control = controls.get(controlKey);

        // Setting control properties based on type
        if (control instanceof TextField) {
            ((TextField) control).setEditable(!isReadOnly);
            ((TextField) control).setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(control, Priority.ALWAYS);
        } else if (control instanceof TextArea) {
            ((TextArea) control).setEditable(!isReadOnly);
            ((TextArea) control).setMaxWidth(Double.MAX_VALUE);
            GridPane.setHgrow(control, Priority.ALWAYS);
            GridPane.setValignment(label, VPos.TOP);
            GridPane.setRowSpan(control, 2);
        } else if (control instanceof CheckBox) {
            ((CheckBox) control).setAllowIndeterminate(false);
        }

        // Adding label and control to the grid
        grid.add(label, colIndex, rowIndex);
        grid.add(control, colIndex, rowIndex + 1);
        GridPane.setMargin(label, new Insets(0, 0, 0, 3));
    }

    // Saves patient information to a file
    private void savePatientInfo() {
        // Implementation of file saving logic goes here
    }

    // Preloads patient data from a file
    private void preloadPatientData() {
        // Implementation of preloading logic goes here
    }

    // Shows an alert dialog with a given message
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

