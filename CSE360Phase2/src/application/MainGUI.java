package application;

// Import JavaFX classes.
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// Class for the main GUI.
public class MainGUI {

    private Stage primaryStage; // The main stage/window of the application.
    String directoryPath = System.getProperty("user.dir"); // Get the current working directory.
    private final String imagesDirectoryPath = directoryPath + "/"; // Path for images folder.

    // Constructor that takes the primary stage.
    public MainGUI(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    // Shows the main screen.
    public void showMainScreen() {
        BorderPane mainLayout = createMainLayout(); // Create layout for the main screen.
        Scene scene = new Scene(mainLayout, 800, 600); // Create scene with layout.
        primaryStage.setTitle("Little Star Pediatric Care System"); // Set window title.
        primaryStage.setScene(scene); // Set scene to stage.
        primaryStage.show(); // Display the stage.
    }

    // Creates the layout for the main screen.
    private BorderPane createMainLayout() {
        BorderPane mainLayout = new BorderPane(); // Main layout pane.
        mainLayout.setBackground(new Background(new BackgroundFill(Color.rgb(44, 94, 136), null, null))); // Set background color.

        // Create and set up the logo image.
        ImageView logoImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "/logo.png"));
        logoImageView.setFitHeight(150); // Set logo height.
        logoImageView.setPreserveRatio(true); // Keep logo aspect ratio.
        StackPane logoPane = new StackPane(logoImageView); // Pane to hold the logo.
        logoPane.setAlignment(Pos.TOP_CENTER); // Center logo at the top.
        mainLayout.setTop(logoPane); // Add logo pane to main layout.

        // Create buttons for Patient, Nurse, and Doctor roles.
        Button patientBtn = createButtonWithIcon("Patient", "patient.png");
        patientBtn.setOnAction(event -> new PatientPortal(primaryStage).showPatientPortal()); // Set action for button.

        Button nurseBtn = createButtonWithIcon("Nurse", "nurse.png");
        nurseBtn.setOnAction(event -> new NursePortal(primaryStage).showNursePortal()); // Set action for button.

        Button doctorBtn = createButtonWithIcon("Doctor", "doctor.png");
        doctorBtn.setOnAction(event -> new DoctorPortal(primaryStage).showDoctorPortal()); // Set action for button.

        // Group buttons in a horizontal box.
        HBox buttonBox = new HBox(50, patientBtn, nurseBtn, doctorBtn); // Space out buttons.
        buttonBox.setAlignment(Pos.CENTER); // Center buttons.
        mainLayout.setCenter(buttonBox); // Add buttons to main layout.

        return mainLayout; // Return the completed layout.
    }

    // Creates a button with an icon above the text.
    private Button createButtonWithIcon(String role, String iconName) {
        Button button = new Button(role); // Button with role text.
        ImageView iconImageView = new ImageView(new Image("file:" + imagesDirectoryPath + "/" + iconName));
        iconImageView.setFitHeight(90); // Set icon height.
        iconImageView.setPreserveRatio(true); // Keep icon aspect ratio.
        button.setGraphic(iconImageView); // Set the icon as the button's graphic.
        button.setStyle("-fx-background-color: transparent;"); // Make button background transparent.
        button.setContentDisplay(ContentDisplay.TOP); // Position text below the icon.
        return button; // Return the created button.
    }
}
