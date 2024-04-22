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

public class NursePortal {
	
	TextField firstNameInput;
	TextField lastNameInput;
	TextField dobInput;

    private Stage primaryStage;
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/"; // Replace with the actual path

    public NursePortal(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showNursePortal() {
        VBox headerBox = createHeaderBox();

        VBox createPatientBox = createPatientSection();
        VBox searchPatientBox = searchPatientSection();

        HBox contentBox = new HBox(20, createPatientBox, searchPatientBox);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(20));

        BorderPane nurseLayout = new BorderPane();
        nurseLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        nurseLayout.setTop(headerBox);
        nurseLayout.setCenter(contentBox);

        Scene nurseScene = new Scene(nurseLayout, 800, 600);
        primaryStage.setScene(nurseScene);
        primaryStage.setTitle("Nurse Portal");
        primaryStage.show();
    }

    private VBox createHeaderBox() {
        Label nursePortalLabel = new Label("Nurse Portal");
        nursePortalLabel.setTextFill(Color.WHITE);
        nursePortalLabel.setStyle("-fx-font-size: 24px;");

        Button backButton = new Button("Log Out");
        backButton.setOnAction(event -> {
            MainGUI mainGUI = new MainGUI(primaryStage);
            mainGUI.showMainScreen(); // Show the main screen when Back button is pressed
        });

        VBox headerBox = new VBox(20, nursePortalLabel, backButton);
        headerBox.setAlignment(Pos.CENTER);
        headerBox.setPadding(new Insets(10));
        headerBox.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), CornerRadii.EMPTY, Insets.EMPTY)));
        return headerBox;
    }

    private VBox createPatientSection() {
        Label createPatientLabel = new Label("Create New Patient");
        createPatientLabel.setTextFill(Color.WHITE);

        firstNameInput = new TextField();
        firstNameInput.setPromptText("First Name");

        lastNameInput = new TextField();
        lastNameInput.setPromptText("Last Name");

        dobInput = new TextField();
        dobInput.setPromptText("Date of Birth (DOB)");

        Button createButton = new Button("Create");
        createButton.setOnAction(e -> {
            String firstName = firstNameInput.getText();
            String lastName = lastNameInput.getText();
            String dob = dobInput.getText();
            
            if(isValid()) {
            createNewPatient(firstName, lastName, dob);
            }
        });

        VBox createPatientBox = new VBox(10, createPatientLabel, firstNameInput, lastNameInput, dobInput, createButton);
        createPatientBox.setAlignment(Pos.CENTER);
        createPatientBox.setPadding(new Insets(10));
        return createPatientBox;
    }

    private VBox searchPatientSection() {
        Label searchPatientLabel = new Label("Search by Patient ID");
        searchPatientLabel.setTextFill(Color.WHITE);

        TextField searchPatientIdInput = new TextField();
        searchPatientIdInput.setPromptText("Enter Patient ID");

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String patientId = searchPatientIdInput.getText();
            if(patientId.isEmpty()) {
            	showAlert("Please enter a Patient ID");
            }
            else {
            searchPatient(patientId);
            }
        });

        VBox searchPatientBox = new VBox(10, searchPatientLabel, searchPatientIdInput, searchButton);
        searchPatientBox.setAlignment(Pos.CENTER);
        searchPatientBox.setPadding(new Insets(10));
        return searchPatientBox;
    }

    private void createNewPatient(String firstName, String lastName, String dob) {
        String patientID = generatePatientID();
        String formattedDOB = dob.replace("/", "-");
        String filePath = imagesDirectoryPath + File.separator + patientID + "_" + firstName + "_" + lastName + "_" + formattedDOB + ".txt";

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write("Patient ID: " + patientID + "\n");
            fileWriter.write("First Name: " + firstName + "\n");
            fileWriter.write("Last Name: " + lastName + "\n");
            fileWriter.write("DOB: " + dob + "\n");
            showAlert("New patient created with ID: " + patientID);
        } catch (IOException e) {
            showAlert("Error: Unable to create patient  file.");
        }
    }

    private String generatePatientID() {
        Random random = new Random();
        return String.valueOf(10000 + random.nextInt(90000));
    }

    private void searchPatient(String patientId) {
        File dir = new File(imagesDirectoryPath); 
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(patientId + "_") && name.endsWith(".txt"));

        if (matchingFiles != null && matchingFiles.length > 0) {
            // Extracting the patient ID from the file name.
            String fileName = matchingFiles[0].getName();
            String patientFileId = fileName.substring(0, fileName.lastIndexOf('.'));
            // Pass the patient ID to NurseView to display the patient information.
            new NurseView(primaryStage, patientFileId).displayPatientInfo();
        } else {
            showAlert("No record found for Patient ID: " + patientId);
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
public boolean isValid() {
    	
    	try {
    		if(firstNameInput.getText().isEmpty()) {
    			showAlert("Please Enter Patient First name");
    			return false;
    		}
    		Integer.parseInt(firstNameInput.getText());
    		showAlert("Please enter a Valid First Name");
    		return false;
    		
    	}catch(NumberFormatException e) {}
    	
    	try {
    		if(lastNameInput.getText().isEmpty()) {
    			showAlert("Please Enter Patient Last name");
    			return false;
    		}
    		Integer.parseInt(lastNameInput.getText());
    		showAlert("Please enter a Valid Last Name");
    		return false;
    		
    	}catch(NumberFormatException e) {}
    	
    	try {
    		if(dobInput.getText().isEmpty()) {
    			showAlert("Please Enter Patient Date of Birth");
    			return false;
    		}
    		String str = dobInput.getText();
    		if ((str.contains(String.valueOf("/")))||(str.contains(String.valueOf("-")))) {}
    		else {
    		  showAlert("Please enter Date of Birth in the proper format");
    		  return false;
    		}
    		str = str.replace("/", "");
    		str = str.replace("-", "");
    		
    		Integer.parseInt(str);

    	}catch(NumberFormatException e) {
    		showAlert("Please enter valid Date of Birth");
    		return false;
    	}
    	
    	return true;
    }
}
