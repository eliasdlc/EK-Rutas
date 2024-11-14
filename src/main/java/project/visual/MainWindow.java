package project.visual;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {

    @FXML
    private Button createButton;
    private Button listBttn;
    private Button stadisticsBttn;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window.fxml"));


        double screenWidth = Screen.getPrimary().getBounds().getWidth() - 50;
        double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;

        
        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        
        String css = this.getClass().getResource("MainWindow.css").toExternalForm();
        scene.getStylesheets().add(css);

        stage.setTitle("EK Routes");
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}