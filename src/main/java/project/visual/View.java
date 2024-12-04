package project.visual;

import javafx.application.Application;
import javafx.stage.Stage;
import project.logic.Graph;

public class View extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    public interface OnSelectedGraph{
        void getSelectedGraph(Graph graph);
    }
    @Override
    public void start(Stage primaryStage) {

    }
}
