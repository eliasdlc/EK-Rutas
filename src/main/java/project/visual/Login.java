package project.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Login extends Application {

    double xOffset = 0;
    double yOffset = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Scene scene = new Scene(loader.load());

        // Apply CSS styles
        String css = getClass().getResource("login.css").toExternalForm();
        scene.getStylesheets().add(css);

        // Create a rectangle for rounded corners
        Rectangle rect = new Rectangle(500, 700);
        rect.setArcHeight(30.0);
        rect.setArcWidth(30.0);

        // Set transparent background
        scene.setFill(Color.TRANSPARENT);

        // Apply the clip to root node to round the corners
        Pane root = (Pane) scene.getRoot();
        root.setClip(rect);

        // Make the window transparent and draggable
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        scene.setOnMousePressed((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });

        scene.setOnMouseDragged((MouseEvent event) -> {
            primaryStage.setX(event.getScreenX() - xOffset);
            primaryStage.setY(event.getScreenY() - yOffset);
        });

        primaryStage.setTitle("Login");
        primaryStage.centerOnScreen();
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
