package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MainGUI {

    private Stage primaryStage;
    String directoryPath = System.getProperty("user.dir");
    private final String imagesDirectoryPath = directoryPath + "/"; // Provided images directory path

    public MainGUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // This method should not return anything, hence 'void' is used
    public void showMainScreen() {
        BorderPane mainLayout = createMainLayout();
        Scene scene = new Scene(mainLayout, 800, 600); // Create a new scene with the layout
        primaryStage.setTitle("Little Star Pediatric Care System");
        primaryStage.setScene(scene); // Set the created scene to the primary stage
        primaryStage.show(); // Show the primary stage
    }

    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane();
        mainLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null)));

        ImageView logoImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        logoImageView.setFitHeight(150);
        logoImageView.setPreserveRatio(true);
        StackPane logoPane = new StackPane(logoImageView);
        logoPane.setAlignment(Pos.TOP_CENTER);
        mainLayout.setTop(logoPane);

        Button patientBtn = createButtonWithIcon("Patient", "patient.png");
        patientBtn.setOnAction(event -> new PatientPortal(primaryStage).showPatientPortal());

        Button nurseBtn = createButtonWithIcon("Nurse", "nurse.png");
        nurseBtn.setOnAction(event -> new NursePortal(primaryStage).showNursePortal());

        Button doctorBtn = createButtonWithIcon("Doctor", "doctor.png");
        doctorBtn.setOnAction(event -> new DoctorPortal(primaryStage).showDoctorPortal());

        HBox buttonBox = new HBox(50, patientBtn, nurseBtn, doctorBtn);
        buttonBox.setAlignment(Pos.CENTER);
        mainLayout.setCenter(buttonBox);

        return mainLayout;
    }

    private Button createButtonWithIcon(String role, String iconName) {
        Button button = new Button(role);
        ImageView iconImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "/" + iconName));
        iconImageView.setFitHeight(90);
        iconImageView.setPreserveRatio(true);
        button.setGraphic(iconImageView);
        button.setStyle("-fx-background-color: transparent;");
        button.setContentDisplay(ContentDisplay.TOP);
        return button;
    }
}
