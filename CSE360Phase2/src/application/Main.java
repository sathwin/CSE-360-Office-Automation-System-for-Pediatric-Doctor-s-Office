package application;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainGUI mainGUI = new MainGUI(primaryStage);
        mainGUI.showMainScreen();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
