package project.visual;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.logic.Graph;

import java.io.IOException;
import java.util.Objects;

public class ViewWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public interface OnSelectedGraph{
        void getSelectedGraph(Graph graph);
    }
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("view-window.fxml"));

        double screenWidth = Screen.getPrimary().getBounds().getWidth() - 50;
        double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;

        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        String css = Objects.requireNonNull(MainWindow.class.getResource("view-window.css")).toExternalForm();
        scene.getStylesheets().add(css);

        Stage stage = new Stage();
        stage.setTitle("EK Routes");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setMaximized(true);
        stage.show();

    }
}
