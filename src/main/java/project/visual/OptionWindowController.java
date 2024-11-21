package project.visual;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OptionWindowController {

    @FXML
    private Button homeButton, optionsButton;

    @FXML
    public void changeToMainScreen(ActionEvent e) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main-window.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());

        Scene scene = new Scene(fxmlLoader.load());
        String css = Objects.requireNonNull(this.getClass().getResource("MainWindow.css")).toExternalForm();
        scene.getStylesheets().add(css);

        stage.setMaximized(true);
        stage.setTitle("EK Routes");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(){

        List<Button> sidebarButtons = new ArrayList<>(List.of(homeButton, optionsButton));

        for (Button button : sidebarButtons) {
            setupSidebarButtonAnimation(button);
        }
    }

    private void setupSidebarButtonAnimation(Button bt){
        MainWindowController.setupSidebarBttnAnimation(bt);
    }
}
