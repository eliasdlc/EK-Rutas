package project.visual;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import project.logic.MapController;

public class MainWindowController {
    private Stage stage;
	private Scene scene;


    @FXML
    private Button createButton, listButton, stadisticsButton, closeButton;

    @FXML
    private Button homeButton, optionsButton;

	
	@FXML
	public void changeToGraphCreation(ActionEvent e) throws IOException {
        

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graph-creation.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        
        scene = new Scene(fxmlLoader.load());
        
        String css = Objects.requireNonNull(this.getClass().getResource("graphCreation.css")).toExternalForm();
        addStyling(css);

        stage.setTitle("Graph Creation");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
	}

    @FXML
    public void changeToViewGraphs(ActionEvent e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("view-window.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();


        scene = new Scene(fxmlLoader.load());
        String css = Objects.requireNonNull(this.getClass().getResource("view-window.css")).toExternalForm();
        addStyling(css);

        //stage.setMaximized(true);
        stage.setTitle("Options");
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void changeToOptions(ActionEvent e) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("option-window.fxml"));
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();


        scene = new Scene(fxmlLoader.load());
        String css = Objects.requireNonNull(this.getClass().getResource("optionsWindow.css")).toExternalForm();
        addStyling(css);

        //stage.setMaximized(true);
        stage.setTitle("Options");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void closeApp(ActionEvent e){
        MapController.saveData();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void addStyling(String css) {
        scene.getStylesheets().add(css);

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
    }

    public void initialize(){

        List<Button> buttons = new ArrayList<>(List.of(createButton, listButton, stadisticsButton));

        for (Button button : buttons) {
            setupButtonAnimations(button);
        }

        List<Button> sidebarButtons = new ArrayList<>(List.of(homeButton, optionsButton, closeButton));

        for (Button button : sidebarButtons) {
            setupSidebarButtonAnimation(button);
        }
    }


    private void setupSidebarButtonAnimation(Button bt){
        setupSidebarBttnAnimation(bt);
    }

    static void setupSidebarBttnAnimation(Button bt) {
        DropShadow shadow = new DropShadow(16, Color.rgb(0, 0, 0, 0.2));
        shadow.setOffsetY(4);
        bt.setEffect(shadow);

        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2), // Duration of animation
                        new KeyValue(shadow.spreadProperty(), 0.8), // Increase shadow spread
                        new KeyValue(bt.scaleXProperty(), 1.05), // Slightly increase button scale
                        new KeyValue(bt.scaleYProperty(), 1.05)) // Slightly increase button scale
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(shadow.spreadProperty(), 0.52),
                        new KeyValue(bt.scaleXProperty(), 1),
                        new KeyValue(bt.scaleYProperty(), 1))
        );

        bt.setOnMouseEntered(e -> {
            hoverAnimation.playFromStart();
        });

        bt.setOnMouseExited(e -> {
            exitAnimation.playFromStart();
        });
    }


    private void setupButtonAnimations(Button bt){
        DropShadow shadow = new DropShadow(16, Color.rgb(0, 0, 0, 0.2));
        shadow.setOffsetY(4);
        bt.setEffect(shadow);

        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2), // Duration of animation
                        new KeyValue(shadow.radiusProperty(), 20), // Increase shadow radius
                        new KeyValue(shadow.spreadProperty(), 0.4), // Increase shadow spread
                        new KeyValue(bt.scaleXProperty(), 1.05), // Slightly increase button scale
                        new KeyValue(bt.scaleYProperty(), 1.05)) // Slightly increase button scale
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(shadow.radiusProperty(), 16),
                        new KeyValue(shadow.spreadProperty(), 0.26),
                        new KeyValue(bt.scaleXProperty(), 1),
                        new KeyValue(bt.scaleYProperty(), 1))
        );

        bt.setOnMouseEntered(e -> {
            hoverAnimation.playFromStart();

            if (bt == createButton)
                bt.setStyle("""
                            -fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #ecd6d6 29.76%, #eac1c1 32.19%, #e6a6a6 34.29%, #e38787 36.53%, #FF4D4D 36.62%, #E22B2B 96.47%);
                            -fx-effect: dropshadow(gaussian, rgba(50, 9, 9, 0.2), 16, 0.26, 0, 4);
                        """);
            else if (bt ==  listButton)
                bt.setStyle("""
                        	-fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #d7ddee 29.76%, #c0ccee 32.19%, #aebeed 34.29%, #87a0ea 36.53%, #627EFA 36.62%, #2355EA  96.47%);
                        	-fx-effect: dropshadow(gaussian, rgba(9, 23, 50, 0.2), 16, 0.26, 0, 4);
                        """);
            else if (bt ==  stadisticsButton)
                bt.setStyle("""
                        	-fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #dff9df 29.76%, #cef3ce 32.19%, #b3eab3 34.29%, #91e991 36.53%, #45D845 36.62%, #17CB17 96.47%);
                        	-fx-effect: dropshadow(gaussian, rgba(18, 50, 9, 0.2), 16, 0.26, 0, 4);
                        """);

        });

        bt.setOnMouseExited(e -> {
            exitAnimation.playFromStart();
            if (bt == createButton)
                bt.setStyle("""
                        -fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #ecd6d6 29.76%, #eac1c1 32.19%, #e6a6a6 34.29%, #e38787 36.53%, #E22B2B 36.62%, #FF4D4D 96.47%);                    """);
            else if (bt ==  listButton)
                bt.setStyle("""
                        	-fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #d7ddee 29.76%, #c0ccee 32.19%, #aebeed 34.29%, #87a0ea 36.53%, #2355EA 36.62%, #627EFA  96.47%);
                        """);
            else if (bt ==  stadisticsButton)
                bt.setStyle("""
                        	-fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #dff9df 29.76%, #cef3ce 32.19%, #b3eab3 34.29%, #91e991 36.53%, #17CB17 36.62%, #45D845 96.47%);
                        """);
        });
    }



}

