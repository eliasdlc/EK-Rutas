package project.visual;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class graphCreation extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("graph-creation.fxml"));

        double screenWidth = Screen.getPrimary().getBounds().getWidth() - 50;
        double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;
        
        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        
        String css = this.getClass().getResource("project/visual/MainWindow.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        primaryStage.setTitle("Graph Creation");
        primaryStage.centerOnScreen();
        //stage.setResizable(false);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
