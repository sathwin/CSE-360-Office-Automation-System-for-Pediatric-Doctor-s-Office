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
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Class for displaying and editing patient information in a nurse's portal.
public class NurseView {
    private Stage stage;
    private String patientId;
    // Base directory for patient files.
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/";
    // Holds dynamically created controls for patient info.
    private Map<String, Control> controls = new HashMap<>();
    // Checkbox for patient age group.
    private CheckBox isChildOver12Checkbox;

    // Initializes the view with necessary controls.
    public NurseView(Stage stage, String patientId) {
        this.stage = stage;
        this.patientId = patientId;
        initializeControls();
    }

    // Prepares input fields and checkboxes for patient info.
    private void initializeControls() {
        String[] textFieldKeys = {
            "PATIENTID", "FIRSTNAME", "LASTNAME", "DOB", "WEIGHT", "HEIGHT", "BODYTEMPERATURE", "BLOODPRESSURE", "KNOWNALLERGIES", "HEALTHCONCERNS"
        };
        for (String key : textFieldKeys) {
            controls.put(key, new TextField());
        }

        String[] textAreaKeys = {
            "PATIENTHISTORY", "PRESCRIBEDMEDICATIONS", "IMMUNIZATIONRECORDS", "CONTACTINFORMATION"
        };
        for (String key : textAreaKeys) {
            TextArea textArea = new TextArea();
            textArea.setPrefHeight(100);
            controls.put(key, textArea);
        }

        isChildOver12Checkbox = new CheckBox("Child over 12");
        controls.put("CHILDOVER12", isChildOver12Checkbox);
    }

    // Displays the main UI for nurse to view/edit patient info.
    public void displayPatientInfo() {
        BorderPane root = new BorderPane();
        root.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        root.setTop(createHeader());
        root.setCenter(createCenterGrid());

        Scene scene = new Scene(root, 1500, 800);
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.setTitle("Nurse Portal - Patient Information");
        stage.show();

        preloadPatientData();
    }

    // Header section with navigation and utility buttons.
    private HBox createHeader() {
        HBox header = new HBox();
        header.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        header.setPadding(new Insets(15));
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        ImageView logo = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        header.getChildren().addAll(logo, new Label("Nurse Portal"), new Button("Log Out"));

        return header;
    }

    // Grid for patient info fields and actions.
    private GridPane createCenterGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.TOP_CENTER);

        // Dynamic fields for patient information.
        addLabelAndControl(grid, "Patient ID", "PATIENTID", 0, 0, true);
        // More fields added here...

        // Save button to store patient info changes.
        Button saveButton = new Button("Save");
        grid.add(saveButton, 1, 16);
        saveButton.setOnAction(event -> savePatientInfo());

        return grid;
    }

    // Adds a label and control to the grid layout.
    private void addLabelAndControl(GridPane grid, String labelText, String controlKey, int colIndex, int rowIndex, boolean isReadOnly) {
        Label label = new Label(labelText + ":");
        Control control = controls.get(controlKey);
        grid.add(label, colIndex, rowIndex);
        grid.add(control, colIndex, rowIndex + 1);
    }

    // Saves the patient info to a file.
    private void savePatientInfo() {
        // Implementation to save patient info.
    }

    // Loads patient data from file into the UI.
    private void preloadPatientData() {
        // Implementation to load patient data.
    }

    // Shows an information alert.
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

