package project.visual;

import javafx.event.ActionEvent;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainWindowController {
	private Stage stage;
	private Scene scene;
	
	@FXML
	public void changeToGraphCreation(ActionEvent e) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graph-creation.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		
        double screenWidth = Screen.getPrimary().getBounds().getWidth() - 50;
        double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;

        
        scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        
        String css = this.getClass().getResource("graphCreation.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setTitle("Graph Creation");
        stage.centerOnScreen();
        //stage.setResizable(false);
        //stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
	}
}

