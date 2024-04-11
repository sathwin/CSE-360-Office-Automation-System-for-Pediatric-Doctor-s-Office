package application;

// Import JavaFX classes needed.
import javafx.application.Application;
import javafx.stage.Stage;

// Main class to start the app.
public class Main extends Application {

    @Override
    // Start method sets up the main window.
    public void start(Stage primaryStage) {
        // Create the main GUI and show it.
        MainGUI mainGUI = new MainGUI(primaryStage);
        mainGUI.showMainScreen();
    }

    // Main method to run the app.
    public static void main(String[] args) {
        // Start the JavaFX app.
        launch(args);
    }
}

