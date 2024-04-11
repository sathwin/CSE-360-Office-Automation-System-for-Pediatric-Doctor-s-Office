package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

// Other imports as necessary

public class PatientPortal {
    private Stage primaryStage;
    // Ensure this path is correctly pointing to your project's resources or appropriate directory
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/";

    public PatientPortal(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showPatientPortal() {
        BorderPane patientLayout = new BorderPane();
        patientLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        ImageView logoImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        logoImageView.setFitHeight(60);
        logoImageView.setPreserveRatio(true);
        Label patientPortalLabel = new Label("Patient Portal");
        patientPortalLabel.setTextFill(Color.WHITE);
        patientPortalLabel.setStyle("-fx-font-size: 24px;");
        HBox patientHeader = new HBox(20, logoImageView, patientPortalLabel);
        patientHeader.setAlignment(Pos.CENTER);
        patientHeader.setPadding(new Insets(10));
        patientHeader.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));
        patientLayout.setTop(patientHeader);

        VBox createAccountSection = setupCreateAccountSection();
        VBox searchSection = setupSearchSection();

        HBox centerLayout = new HBox(100, createAccountSection, searchSection);
        centerLayout.setAlignment(Pos.CENTER);
        patientLayout.setCenter(centerLayout);

        Scene patientPortalScene = new Scene(patientLayout, 800, 600);
        primaryStage.setScene(patientPortalScene);
        primaryStage.setTitle("Patient Portal");
        primaryStage.show();
    }

    private VBox setupCreateAccountSection() {
        VBox createAccountSection = new VBox(10);
        createAccountSection.setAlignment(Pos.CENTER_LEFT);
        createAccountSection.setPadding(new Insets(50));
        Label createAccountLabel = new Label("Create New Patient");
        TextField firstNameInput = new TextField();
        firstNameInput.setPromptText("First Name");
        TextField lastNameInput = new TextField();
        lastNameInput.setPromptText("Last Name");
        TextField dobInput = new TextField();
        dobInput.setPromptText("Date of Birth (MM-DD-YYYY)");
        Button createButton = new Button("Create");

        createButton.setOnAction(e -> {
            String firstName = firstNameInput.getText().trim();
            String lastName = lastNameInput.getText().trim();
            String dob = dobInput.getText().trim().replace("/", "-");
            if (firstName.isEmpty() || lastName.isEmpty() || dob.isEmpty()) {
                showAlert("Please enter all fields.");
                return;
            }

            String patientID = generatePatientID();
            String patientFileName = String.format("%s_%s_%s_%s.txt", patientID, firstName, lastName, dob);
            Path patientFilePath = Paths.get(imagesDirectoryPath, patientFileName);

            if (Files.exists(patientFilePath)) {
                showAlert("An account for this patient ID already exists.");
                return;
            }

            try {
                ArrayList<String> content = new ArrayList<>();
                content.add("Patient ID: " + patientID);
                content.add("First Name: " + firstName);
                content.add("Last Name: " + lastName);
                content.add("DOB: " + dob);
                Files.write(patientFilePath, content, StandardOpenOption.CREATE_NEW);
                showAlert("Patient account created successfully. Patient ID: " + patientID);
            } catch (IOException ex) {
                showAlert("Error creating patient account: " + ex.getMessage());
            }
        });
        
        Button backButton = new Button("Log Out");
        backButton.setOnAction(event -> {
            MainGUI mainGUI = new MainGUI(primaryStage);
            mainGUI.showMainScreen(); // Show the main screen when Back button is pressed
        });

        createAccountSection.getChildren().addAll(createAccountLabel, firstNameInput, lastNameInput, dobInput, createButton, backButton);
        return createAccountSection;
    }

    private VBox setupSearchSection() {
        VBox searchSection = new VBox(10);
        searchSection.setAlignment(Pos.CENTER_RIGHT);
        searchSection.setPadding(new Insets(50));
        Label searchLabel = new Label("Search Patient by ID");
        TextField searchInput = new TextField();
        searchInput.setPromptText("Enter Patient ID");
        Button searchButton = new Button("Search");

        searchButton.setOnAction(e -> {
            String patientId = searchInput.getText().trim();
            if (patientId.isEmpty()) {
                showAlert("Please enter a Patient ID.");
                return;
            }
            File dir = new File(imagesDirectoryPath);
            File[] foundFiles = dir.listFiles((d, name) -> name.startsWith(patientId) && name.endsWith(".txt"));

            if (foundFiles != null && foundFiles.length > 0) {
                // Here you could open the PatientView
                new PatientView(primaryStage, patientId); // This now matches the constructor in PatientView
            } else {
                showAlert("No record found for Patient ID: " + patientId);
            }
        });
        searchSection.getChildren().addAll(searchLabel, searchInput, searchButton);
        return searchSection;
    }

    private String generatePatientID() {
        return String.valueOf((int) (Math.random() * 90000) + 10000);
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
