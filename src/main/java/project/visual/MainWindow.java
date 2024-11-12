package project.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {
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
        //stage.setResizable(false);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}