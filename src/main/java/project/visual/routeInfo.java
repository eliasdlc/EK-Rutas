package project.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class routeInfo extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("routeInfo.fxml"));
        Scene scene = new Scene(loader.load());

        String css = this.getClass().getResource("routeInfo.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Agregar Ruta");
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
