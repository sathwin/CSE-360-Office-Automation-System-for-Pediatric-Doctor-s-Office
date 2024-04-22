package application;

// Import statements to use JavaFX components and classes
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

// Class definition for the DoctorPortal
public class DoctorPortal {

    // Declaration of instance variables
    private Stage primaryStage;
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/";

    // Constructor to initialize the DoctorPortal with a Stage
    public DoctorPortal(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Method to show the Doctor Portal UI
    public void showDoctorPortal() {
        // Setting up the main layout pane
        BorderPane doctorLayout = new BorderPane();
        doctorLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        // Creating and setting the header
        HBox header = createHeader();
        doctorLayout.setTop(header);

        // Creating and setting the center content
        VBox center = createCenter();
        doctorLayout.setCenter(center);

        // Creating and setting the scene
        Scene doctorScene = new Scene(doctorLayout, 800, 600);
        primaryStage.setScene(doctorScene);
        primaryStage.setTitle("Doctor Portal");
        primaryStage.show();
    }

    // Method to create the header part of the UI
    private HBox createHeader() {
        // Setting up the HBox for the header
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10));
        headerBox.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        // Adding logo to the header
        ImageView logoImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "logo.png"));
        logoImageView.setFitHeight(60);
        logoImageView.setPreserveRatio(true);

        // Adding title label to the header
        Label titleLabel = new Label("Doctor Portal");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-size: 24px;");

        // Adding the logo and title to the header box
        headerBox.getChildren().addAll(logoImageView, titleLabel);

        return headerBox;
    }

    // Method to create the center part of the UI
    private VBox createCenter() {
        // Setting up the VBox for the center content
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(15));
        centerBox.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        // Adding patient ID label and input field
        Label patientIDLabel = new Label("Search Patient by ID");
        patientIDLabel.setTextFill(Color.WHITE);
        patientIDLabel.setStyle("-fx-font-size: 18px;");

        TextField patientIDInput = new TextField();
        patientIDInput.setPromptText("Enter Patient ID");
        patientIDInput.setMaxWidth(200); // Set the max width to make the text box smaller

        // Adding search and back buttons
        Button searchButton = new Button("Search");
        
        searchButton.setOnAction(e -> {
        
        
	        String patientId = patientIDInput.getText();
	        
	        if(patientId.isEmpty()) {
	        	showAlert("Please enter a Patient ID");
	        }
	        else {
	        searchPatient(patientId);
	        }
        
        });

        Button backButton = new Button("Log Out");
        backButton.setOnAction(e -> new MainGUI(primaryStage).showMainScreen());

        // Adding all components to the center box
        centerBox.getChildren().addAll(patientIDLabel, patientIDInput, searchButton, backButton);

        return centerBox;
    }

    // Method to search for a patient based on ID
    private void searchPatient(String patientId) {
        // Finding files that match the patient ID
        File dir = new File(imagesDirectoryPath); 
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(patientId + "_") && name.endsWith(".txt"));

        // If matching files are found, open DoctorView, else show an alert
        if (matchingFiles != null && matchingFiles.length > 0) {
            new DoctorView(primaryStage, patientId).displayPatientInfo();
        } else {
            showAlert("No record found for Patient ID: " + patientId);
        }
    }

    // Method to show an alert message
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
