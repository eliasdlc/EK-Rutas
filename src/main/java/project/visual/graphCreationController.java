package project.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import project.logic.Graph;
import project.logic.IdGenerator;
import project.logic.MapController;
import project.logic.StopNode;

import java.beans.EventHandler;
import java.io.IOException;
import java.util.Objects;

public class graphCreationController {

    @FXML
    private Pane map;
    private Graph graph = null;
    private final int baseNumVertex = 10;
    private final int baseRadius = 30;
    private boolean addingRoute = false;
    private Circle selCircle1 = null;
    private Circle selCircle2 = null;

    @FXML
    private void addNode(ActionEvent e){
        map.setOnMouseClicked(event -> {

            map.setOnMouseClicked(null);

            double x = event.getX();
            double y = event.getY();
            String idNode = IdGenerator.generateId();

            Circle node = new Circle(x, y, baseRadius);
            node.setFill(javafx.scene.paint.Color.WHITE);
            node.setStroke(javafx.scene.paint.Color.BLACK);

            TextField txtFld = new TextField(null);
            txtFld.setPrefWidth(160);
            txtFld.setPrefHeight(20);
            txtFld.setLayoutX(x - txtFld.getPrefWidth()/2);
            txtFld.setLayoutY(y - (txtFld.getPrefHeight()/2) - baseRadius - 35);
            txtFld.setBackground(new Background(new BackgroundFill(
                    Color.rgb(42, 42, 42, 0),
                    CornerRadii.EMPTY,      // Sin bordes redondeados
                    Insets.EMPTY)));        // Sin margen interno
            txtFld.setFont( Font.font("Inter", FontWeight.BOLD, 18) );
            txtFld.setStyle("-fx-text-fill: white; -fx-alignment: center;");

            map.getChildren().add(node);
            map.getChildren().add(txtFld);

            txtFld.requestFocus();
            txtFld.setOnAction(textEvent -> {
                String name = txtFld.getText();
                ifGraphNull(graph);
                if(graph.getNodes() != null) {
                    graph.getNodes().add(new StopNode(idNode, name, x, y));
                    MapController.getInstance().addGraph(graph);
                    txtFld.setEditable(false);
                }
            });
        });
    }

    private void ifGraphNull(Graph graph){
        this.graph = graph;
        if(graph == null){
            String idGraph = IdGenerator.generateId();
            this.graph = new Graph(baseNumVertex, idGraph);
        }
    }

    @FXML
    private void addRoute(ActionEvent e){
        startAddingRoute();
        //mouse click handler for selecting circles
        map.setOnMouseClicked(event -> {

            if(!addingRoute) return; //ignore clicks if not in adding route mode

            Circle clickedCircle = null;
            for(var node: map.getChildren()){
                if(node instanceof Circle){
                    Circle circle = (Circle) node;
                    if(circle.contains(circle.sceneToLocal(event.getSceneX(), event.getSceneY()))){
                        clickedCircle = circle;
                        break;
                    }
                }
            }
            if(clickedCircle == null) return;


            if(selCircle1 == null){
                selCircle1 = clickedCircle; // select first circle
                selCircle1.setFill(javafx.scene.paint.Color.BLUE);
            } else if(selCircle2 == null && clickedCircle != selCircle1){
                selCircle2 = clickedCircle; // select second circle
                selCircle2.setFill(javafx.scene.paint.Color.BLUE);

                if(selCircle1 != null && selCircle2 != null){
                    // find the nodes
                    StopNode node1 = findStopNodeForCircle(selCircle1);
                    StopNode node2 = findStopNodeForCircle(selCircle2);

                    if(node1 != null && node2 != null){
                        drawLine(selCircle1,selCircle2);
                        selCircle1.setFill(javafx.scene.paint.Color.WHITE);
                        selCircle2.setFill(javafx.scene.paint.Color.WHITE);
                        // add the route info
                        showRouteInfoPopup(node1,node2);
                    }
                    addingRoute = false;
                    selCircle1 = null;
                    selCircle2 = null;
                    map.setOnMouseClicked(null);
                }
            }
        });
    }

    private StopNode findStopNodeForCircle(Circle circle){
        ifGraphNull(graph);
        for(StopNode node : graph.getNodes()){
            double tolerance = 1e-2;
            if(Math.abs(node.getPosX()- circle.getCenterX()) < tolerance &&
            Math.abs(node.getPosY()- circle.getCenterY()) < tolerance){
                return node;
            }
        }
        return null;
    }

    private void showRouteInfoPopup(StopNode node1, StopNode node2){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("routeInfo.fxml"));
            Scene scene = new Scene(loader.load());
            String css = this.getClass().getResource("routeInfo.css").toExternalForm();
            routeInfoController controller = loader.getController();
            controller.setNodes(node1,node2);
            controller.setGraph(graph);
            Stage stage = new Stage();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.setTitle("Agregar Ruta");
            stage.show();

        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
    @FXML
    private void changeToRouteInfo(ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("routeInfo.fxml"));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(loader.load());
        String css = Objects.requireNonNull(this.getClass().getResource("routeInfo.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Info Ruta");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.centerOnScreen();
        stage.setScene(scene);
        stage.show();
    }

    private void drawLine(Circle circle1, Circle circle2){
        Line line = new Line();
        line.setStartX(circle1.getCenterX());
        line.setStartY(circle1.getCenterY());
        line.setEndX(circle2.getCenterX());
        line.setEndY(circle2.getCenterY());

        // styling
        line.setStroke(javafx.scene.paint.Color.WHITE);
        int lineWidth = 5;
        line.setStrokeWidth(lineWidth);

        map.getChildren().add(line);
    }

    private void startAddingRoute(){
        addingRoute = true;
        selCircle1 = null;
        selCircle2 = null;
    }

}
