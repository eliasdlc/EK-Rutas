package project.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainWindow extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window.fxml"));


        double screenWidth = Screen.getPrimary().getBounds().getWidth() - 50;
        double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;

        
        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        
        String css = Objects.requireNonNull(this.getClass().getResource("MainWindow.css")).toExternalForm();
        scene.getStylesheets().add(css);

        /*Image icon = null;
        try {
            icon = new Image(Objects.requireNonNull(MainWindow.class.getResourceAsStream("project/visual/icons/gps_icon.png")));
            stage.getIcons().add(icon);
        } catch (NullPointerException e) {
            System.out.println("Icon resource not found. Please check the path and filename.");
        }

        stage.getIcons().add(icon);*/

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