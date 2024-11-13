package project.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import project.logic.Graph;
import project.logic.IdGenerator;
import project.logic.MapController;
import project.logic.StopNode;

import java.beans.EventHandler;

public class graphCreationController {

    @FXML
    private Pane map;
    private Graph graph = null;
    private final int baseNumVertex = 10;
    private final int baseRadius = 30;

    @FXML
    public void addNode(ActionEvent e){
        map.setOnMouseClicked(event -> {

            map.setOnMouseClicked(null);

            double x = event.getX();
            double y = event.getY();
            String idNode = IdGenerator.generateId();

            Circle node = new Circle(x, y, baseRadius);
            node.setFill(javafx.scene.paint.Color.WHITE);
            node.setStroke(javafx.scene.paint.Color.BLACK);

            TextField txtFld = new TextField(null);
            txtFld.setLayoutX(x - 50);
            txtFld.setLayoutY(y - baseRadius - 30);
            txtFld.setPrefWidth(100);
            txtFld.setPrefHeight(20);

            map.getChildren().add(node);
            map.getChildren().add(txtFld);

            txtFld.requestFocus();
            txtFld.setOnAction(textEvent -> {
                String name = txtFld.getText();
                if(graph == null){
                    String idGraph = IdGenerator.generateId();
                    graph = new Graph(baseNumVertex, idGraph);
                }
                if(graph.getNodes() != null) {
                    graph.getNodes().add(new StopNode(idNode, name, x, y));
                    MapController.getInstance().addGraph(graph);
                    txtFld.setEditable(false);
                    //map.setOnMouseClicked(null);
                }
            });
        });
    }

}
