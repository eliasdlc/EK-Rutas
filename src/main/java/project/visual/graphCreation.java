package project.visual;

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.logic.Graph;

public class graphCreation extends Application {


	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(MainWindow.class.getResource("graph-creation.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load());
        
        String css = this.getClass().getResource("project/visual/MainWindow.css").toExternalForm();
        scene.getStylesheets().add(css);

        primaryStage.setTitle("Graph Creation");
        primaryStage.centerOnScreen();
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

    public static void main(String[] args) {
		launch(args);
	}
}
