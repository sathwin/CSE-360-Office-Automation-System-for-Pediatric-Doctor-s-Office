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

import java.io.File;

public class DoctorPortal {

    private Stage primaryStage;
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/";

    public DoctorPortal(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showDoctorPortal() {
        BorderPane doctorLayout = new BorderPane();
        doctorLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        HBox header = createHeader();
        doctorLayout.setTop(header);

        VBox center = createCenter();
        doctorLayout.setCenter(center);

        Scene doctorScene = new Scene(doctorLayout, 800, 600);
        primaryStage.setScene(doctorScene);
        primaryStage.setTitle("Doctor Portal");
        primaryStage.show();
    }

    private HBox createHeader() {
        HBox headerBox = new HBox();
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(10));
        headerBox.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        ImageView logoImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        logoImageView.setFitHeight(60);
        logoImageView.setPreserveRatio(true);

        Label titleLabel = new Label("Doctor Portal");
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setStyle("-fx-font-size: 24px;");

        headerBox.getChildren().addAll(logoImageView, titleLabel);

        return headerBox;
    }

    private VBox createCenter() {
        VBox centerBox = new VBox(10);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(15));
        centerBox.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        Label patientIDLabel = new Label("Search Patient by ID");
        patientIDLabel.setTextFill(Color.WHITE);
        patientIDLabel.setStyle("-fx-font-size: 18px;");

        TextField patientIDInput = new TextField();
        patientIDInput.setPromptText("Enter Patient ID");
        patientIDInput.setMaxWidth(200); // Set the max width to make the text box smaller

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchPatient(patientIDInput.getText()));

        Button backButton = new Button("Log Out");
        backButton.setOnAction(e -> new MainGUI(primaryStage).showMainScreen());

        centerBox.getChildren().addAll(patientIDLabel, patientIDInput, searchButton, backButton);

        return centerBox;
    }

    private void searchPatient(String patientId) {
        File dir = new File(imagesDirectoryPath); 
        File[] matchingFiles = dir.listFiles((dir1, name) -> name.startsWith(patientId + "_") && name.endsWith(".txt"));

        if (matchingFiles != null && matchingFiles.length > 0) {
            // Open DoctorView with the patient ID
            new DoctorView(primaryStage, patientId).displayPatientInfo();
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
}
