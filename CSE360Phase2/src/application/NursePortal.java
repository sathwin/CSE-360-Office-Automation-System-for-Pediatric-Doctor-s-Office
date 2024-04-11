package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

// Class for the nurse's portal in the application.
public class NursePortal {

    private Stage primaryStage;
    // Directory path for saving files.
    String directoryPath = System.getProperty("user.dir");
    // Path to save new patient files.
    private final String imagesDirectoryPath = directoryPath + "/";

    // Constructor initializing the stage.
    public NursePortal(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Displays the nurse portal UI.
    public void showNursePortal() {
        // Creating the header and content sections.
        VBox headerBox = createHeaderBox();
        VBox createPatientBox = createPatientSection();
        VBox searchPatientBox = searchPatientSection();

        // Combining content sections in a horizontal layout.
        HBox contentBox = new HBox(20, createPatientBox, searchPatientBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        // Setting the main layout and scene.
        BorderPane nurseLayout = new BorderPane();
        nurseLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        nurseLayout.setTop(headerBox);
        nurseLayout.setCenter(contentBox);

        Scene nurseScene = new Scene(nurseLayout, 800, 600);
        primaryStage.setScene(nurseScene);
        primaryStage.setTitle("Nurse Portal");
        primaryStage.show();
    }

    // Creates the header section.
    private VBox createHeaderBox() {
        // Setup header with title and logout button.
        Label nursePortalLabel = new Label("Nurse Portal");
        nursePortalLabel.setTextFill(Color.WHITE);
        nursePortalLabel.setStyle("-fx-font-size: 24px;");

        Button backButton = new Button("Log Out");
        backButton.setOnAction(event -> {
            MainGUI mainGUI = new MainGUI(primaryStage);
            mainGUI.showMainScreen();
        });

        VBox headerBox = new VBox(20, nursePortalLabel, backButton);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10));
        headerBox.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        return headerBox;
    }

    // Creates the section for creating a new patient.
    private VBox createPatientSection() {
        // Setup input fields for new patient details and create button.
        Label createPatientLabel = new Label("Create New Patient");
        createPatientLabel.setTextFill(Color.WHITE);

        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("First Name");

        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("Last Name");

        TextField dobInput = new TextField();
        dobInput.setPromptText("Date of Birth (DOB)");

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            createNewPatient(firstNameInput.getText(), lastNameInput.getText(), dobInput.getText());
        });

        VBox createPatientBox = new VBox(10, createPatientLabel, firstNameInput, lastNameInput, dobInput, createButton);
        createPatientBox.setAlignment(Pos.CENTER);
        createPatientBox.setPadding(new Insets(10));
        return createPatientBox;
    }

    // Creates the section for searching a patient by ID.
    private VBox searchPatientSection() {
        // Setup search field and button.
        Label searchPatientLabel = new Label("Search by Patient ID");
        searchPatientLabel.setTextFill(Color.WHITE);

        TextField searchPatientIdInput = new TextField();
        searchPatientIdInput.setPromptText("Enter Patient ID");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchPatient(searchPatientIdInput.getText()));

        VBox searchPatientBox = new VBox(10, searchPatientLabel, searchPatientIdInput, searchButton);
        searchPatientBox.setAlignment(Pos.CENTER);
        searchPatientBox.setPadding(new Insets(10));
        return searchPatientBox;
    }

    // Handles creating a new patient file with provided details.
    private void createNewPatient(String firstName, String lastName, String dob) {
        // Generate a unique patient ID and save patient details to a file.
    }

    // Generates a random patient ID.
    private String generatePatientID() {
        // Randomly generates a new patient ID.
    }

    // Searches for a patient by ID and displays their information.
    private void searchPatient(String patientId) {
        // Search for a patient file by ID and handle the result.
    }

    // Displays an alert with a given message.
    private void showAlert(String message) {
        // Show an information dialog.
    }
}

