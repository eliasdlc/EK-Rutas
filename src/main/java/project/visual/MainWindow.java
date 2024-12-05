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

    public static void showMainWindow() throws IOException {
        // Cargar el archivo FXML
        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("main-window.fxml"));

        // Configurar dimensiones de pantalla
        double screenWidth = Screen.getPrimary().getBounds().getWidth() - 50;
        double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;

        // Crear la escena y aplicar el estilo CSS
        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        String css = Objects.requireNonNull(MainWindow.class.getResource("MainWindow.css")).toExternalForm();
        scene.getStylesheets().add(css);

        // Crear y configurar el Stage
        Stage stage = new Stage();
        stage.setTitle("EK Routes");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void start(Stage stage) throws IOException {
        showMainWindow();
    }


    public static void main(String[] args) {
        launch();
    }
}