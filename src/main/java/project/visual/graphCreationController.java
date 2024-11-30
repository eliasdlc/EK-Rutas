package project.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import project.logic.*;

import java.io.IOException;
import java.util.*;

public class graphCreationController {

    @FXML
    private Pane map;
    @FXML
    private FlowPane buttons;
    @FXML
    private FlowPane tripPanel;
    @FXML
    private FlowPane algorithmPanel;
    @FXML
    private FlowPane priorityPanel;
    @FXML
    private FlowPane nodeHandlerPanel;
    private Graph graph = null;
    private final int baseNumVertex = 10;
    private final int baseRadius = 30;
    private Button btn1 = null;
    private Button btn2 = null;
    private boolean dijkstra = false;
    private boolean prim = false;
    private boolean deletingMode = false;
    private Criteria priority = null;
    Set<Pair<StopNode, StopNode>> drawnRoutes = new HashSet<>();

    @FXML
    private void initialize() {
        // MUST CALL loadGraph()
        // IF THE PROGRAM IS RUN FOR THE FIRST TIME (WITH PREVIOUS DATA) LOAD THE FIRST GRAPH
        // ELSE, LOAD A NEW GRAPH, CHANGE addNode()
        // Hide panels by default
        hideAllPanels();
        buttons.setDisable(false);
        buttons.setVisible(true);

        List<Graph> graphs = MapController.getInstance().getGraphs();
        if (!graphs.isEmpty()) {
            this.graph = graphs.getFirst();
        } else {
            this.graph = new Graph(baseNumVertex, IdGenerator.generateId());
        }
        loadGraph(graph);
    }

    private void enableTripSelection(){
        selectTrip();
    }

    @FXML
    private void loadGraph(Graph graph) {
       map.getChildren().clear();

       for(StopNode node : graph.getNodes()){
           addNodeToMap(node);
       }

       for(StopNode node : graph.getListRoutes().keySet()){
           List<Route> routesFromNode = graph.getRoutesToDestination(node);
           for(Route route : routesFromNode){
               StopNode destination = route.getDestination();
               Pair<StopNode,StopNode> routePair = new Pair<>(node,destination);
               if(!drawnRoutes.contains(routePair) && !drawnRoutes.contains(new Pair<>(destination, node))){
                   addRouteToMap(route);
                   drawnRoutes.add(routePair);
               }
           }
       }
    }

    private void addNodeToMap(StopNode node) {
        Button btn = new Button();
        btn.setLayoutX(node.getPosX());
        btn.setLayoutY(node.getPosY());
        btn.setStyle("-fx-background-radius: 5em; " +
                "-fx-min-width: 60px; " +
                "-fx-min-height: 60px; " +
                "-fx-max-width: 60px; " +
                "-fx-max-height: 60px;" +
                "-fx-background-color: WHITE;");
        btn.setOnAction(event -> handleNodeSelection(btn));

        double x = node.getPosX();
        double y = node.getPosY();
//txtFld.setLayoutX(x - txtFld.getPrefWidth()/2 + baseRadius);
//            txtFld.setLayoutY(y - (txtFld.getPrefHeight()/2) - 35);
        TextField txtFld = new TextField(node.getNombre());
        txtFld.setPrefWidth(160);
        txtFld.setPrefHeight(20);
        txtFld.setLayoutX(x - txtFld.getPrefWidth()/2 + baseRadius);
        txtFld.setLayoutY(y - (txtFld.getPrefHeight()/2) - 35);
        txtFld.setBackground(new Background(new BackgroundFill(
                Color.rgb(42, 42, 42, 0),
                CornerRadii.EMPTY,      // Sin bordes redondeados
                Insets.EMPTY)));        // Sin margen interno
        txtFld.setFont( Font.font("Inter", FontWeight.BOLD, 18) );
        txtFld.setStyle("-fx-text-fill: white; -fx-alignment: center;");

        map.getChildren().add(btn);
        map.getChildren().add(txtFld);
    }

    private void addRouteToMap(Route route) {
        StopNode origin = route.getOrigin();
        StopNode dest = route.getDestination();

        Line line = new Line();
        line.setStartX(origin.getPosX() + 60.0 / 2);
        line.setStartY(origin.getPosY() + 60.0 / 2);
        line.setEndX(dest.getPosX() + 60.0 / 2);
        line.setEndY(dest.getPosY() + 60.0 / 2);

        line.setStroke(Color.WHITE);
        line.setStrokeWidth(5);

        map.getChildren().add(line);
    }

    @FXML
    private void selectTrip() {
        // METHOD FOR SELECTING THE TRIP.
        // WHEN THE MOUSE HOVERS OVER A NODE, IT CHANGES COLOR. THEN THE USER CLICKS TO SELECT. USER MUST SELECT TWO NODES
        // THEN BUTTON PANEL MUST CHANGE TO A DIFFERENT PANEL WITH BUTTONS: CHOOSE ALGORITHM, ACCEPT (CHOICE OF PRIORITY),
        // EXIT (THIS MUST UNSELECT THE NODES)
    }

    @FXML
    private void exitTripSelection(ActionEvent event) {
        resetBtnStyle();
        resetAllLineColors();
        hideAllPanels();
        buttons.setVisible(true);
        buttons.setDisable(false);
    }

    private void hideAllPanels() {
        buttons.setVisible(false);
        buttons.setDisable(true);
        tripPanel.setVisible(false);
        tripPanel.setDisable(true);
        algorithmPanel.setVisible(false);
        algorithmPanel.setDisable(true);
        priorityPanel.setVisible(false);
        priorityPanel.setDisable(true);
        nodeHandlerPanel.setVisible(false);
        nodeHandlerPanel.setDisable(true);
    }

    private void showNodeHandlerPanel(){
        buttons.setVisible(false);
        buttons.setDisable(true);
        nodeHandlerPanel.setVisible(true);
        nodeHandlerPanel.setDisable(false);
    }

    @FXML
    private void showAlgorithmPanel(ActionEvent event) {
        hideAllPanels();
        algorithmPanel.setVisible(true);
        algorithmPanel.setDisable(false);
    }

    @FXML
    private void showPriorityPanel(ActionEvent event) {
        hideAllPanels();
        priorityPanel.setDisable(false);
        priorityPanel.setVisible(true);
    }

    private void hidePanel(FlowPane pane){
        pane.setVisible(false);
        pane.setDisable(true);
        showTripPanel();
    }

    @FXML
    private void chooseDijkstra(ActionEvent event) {
        dijkstra = true;
        prim = false;
        hidePanel(algorithmPanel);
        showTripPanel();
    }
    @FXML
    private void choosePrim(ActionEvent event) {
        prim = true;
        dijkstra = false;
        hidePanel(algorithmPanel);
        showTripPanel();
    }

    @FXML
    private void chooseGeneral(ActionEvent event) {
        hidePanel(priorityPanel);
        priority = null;
        showTripPanel();
    }
    @FXML
    private void chooseDistancia(ActionEvent event) {
        hidePanel(priorityPanel);
        priority = Criteria.DISTANCIA;
        showTripPanel();
    }
    @FXML
    private void chooseTiempo(ActionEvent event) {
        hidePanel(priorityPanel);
        priority = Criteria.TIEMPO;
        showTripPanel();
    }
    @FXML
    private void chooseCosto(ActionEvent event) {
        hidePanel(priorityPanel);
        priority = Criteria.COSTO;
        showTripPanel();
    }
    @FXML
    private void chooseTransbordo(ActionEvent event) {
        hidePanel(priorityPanel);
        priority = Criteria.TRANSBORDO;
        showTripPanel();
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-window.fxml"));

            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight() - 50;
            Scene scene = new Scene(loader.load(),screenWidth,screenHeight);

            Stage stage = new Stage();
            stage.setScene(scene);
            String css = Objects.requireNonNull(this.getClass().getResource("MainWindow.css")).toExternalForm();
            scene.getStylesheets().add(css);
            stage.setTitle("EK Routes");
            stage.centerOnScreen();
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNode(ActionEvent e){

        map.setOnMouseClicked(event -> {
            map.setOnMouseClicked(null);
            double x = event.getX();
            double y = event.getY();
            String idNode = IdGenerator.generateId();

            Button nodeBtn = new Button();
            nodeBtn.setStyle(
                    "-fx-background-radius: 5em; " +
                            "-fx-min-width: 60px; " +
                            "-fx-min-height: 60px; " +
                            "-fx-max-width: 60px; " +
                            "-fx-max-height: 60px;" +
                            "-fx-background-color: WHITE;"
            );
            nodeBtn.setLayoutX(x);
            nodeBtn.setLayoutY(y);
            nodeBtn.setOnAction(btnEvent -> handleNodeSelection(nodeBtn));

            TextField txtFld = new TextField(null);
            txtFld.setPrefWidth(160);
            txtFld.setPrefHeight(20);
            txtFld.setLayoutX(x - txtFld.getPrefWidth()/2 + baseRadius);
            txtFld.setLayoutY(y - (txtFld.getPrefHeight()/2) - 35);
            txtFld.setBackground(new Background(new BackgroundFill(
                    Color.rgb(42, 42, 42, 0),
                    CornerRadii.EMPTY,      // Sin bordes redondeados
                    Insets.EMPTY)));        // Sin margen interno
            txtFld.setFont( Font.font("Inter", FontWeight.BOLD, 18) );
            txtFld.setStyle("-fx-text-fill: white; -fx-alignment: center;");

            map.getChildren().add(nodeBtn);
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
        if(graph == null){
            String idGraph = IdGenerator.generateId();
            this.graph = new Graph(baseNumVertex, idGraph);
        }
    }

    private void handleNodeSelection(Button clickedButton){
        if(btn1 == null){
            btn1 = clickedButton;
            if(deletingMode && btn1 != null){
                StopNode nodeToDelete = findStopNodeForButton(btn1);
                graph.removeNode(nodeToDelete);
                // METODO PARA ELIMINAR: panel con botones para eliminar nodo o eliminar ruta (el de ruta debe abrir
                // a popup window con una lista de las aristas conectadas a ese nodo
            }
            btn1.setStyle("-fx-background-radius: 5em; " +
                    "-fx-min-width: 60px; " +
                    "-fx-min-height: 60px; " +
                    "-fx-max-width: 60px; " +
                    "-fx-max-height: 60px;" +
                    "-fx-background-color: BLUE;");

        } else if(btn2 == null && clickedButton != null){
            btn2 = clickedButton;
            btn2.setStyle("-fx-background-radius: 5em; " +
                    "-fx-min-width: 60px; " +
                    "-fx-min-height: 60px; " +
                    "-fx-max-width: 60px; " +
                    "-fx-max-height: 60px;" +
                    "-fx-background-color: BLUE;");

            if(btn1 != null && btn2 != null){
                StopNode node1 = findStopNodeForButton(btn1);
                StopNode node2 = findStopNodeForButton(btn2);

                if(node1 != null && node2 != null){
                    showNodeHandlerPanel();
                }
            }
        }
    }

    private StopNode findStopNodeForButton(Button button){
        ifGraphNull(graph);
        for(StopNode node : graph.getNodes()){
            double tolerance = 1e-2;
            if(Math.abs(node.getPosX()- button.getLayoutX()) < tolerance &&
            Math.abs(node.getPosY()- button.getLayoutY()) < tolerance){
                return node;
            }
        }
        return null;
    }

    private Button findButtonForStopNode(StopNode node) {
        for (Node child : map.getChildren()) {
            if (child instanceof Button) {
                Button button = (Button) child;
                double tolerance = 1e-2;
                if (Math.abs(button.getLayoutX() - node.getPosX()) < tolerance &&
                        Math.abs(button.getLayoutY() - node.getPosY()) < tolerance) {
                    return button;
                }
            }
        }
        return null;
    }

    private Line findLineBetweenNodes(StopNode node1, StopNode node2) {
        for (Node child : map.getChildren()) {
            if (child instanceof Line) {
                Line line = (Line) child;
                double startX = line.getStartX();
                double startY = line.getStartY();
                double endX = line.getEndX();
                double endY = line.getEndY();

                // Check if the line connects the two nodes
                if ((Math.abs(startX - node1.getPosX()) < 1e-2 && Math.abs(endX - node2.getPosX()) < 1e-2) ||
                        (Math.abs(startX - node2.getPosX()) < 1e-2 && Math.abs(endX - node1.getPosX()) < 1e-2)) {
                    return line;
                }
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

    //IM GETTING A NullPointerException HERE, FIX LATER
    private void drawLine(Button btn1, Button btn2){
        Line line = new Line();
        line.setStartX(btn1.getLayoutX() + btn1.getWidth() / 2);
        line.setStartY(btn1.getLayoutY() + btn1.getHeight() / 2);
        line.setEndX(btn2.getLayoutX() + btn2.getWidth() / 2);
        line.setEndY(btn2.getLayoutY() + btn2.getHeight() / 2);

        // styling
        line.setStroke(javafx.scene.paint.Color.WHITE);
        line.setStrokeWidth(5);

        map.getChildren().add(line);
    }

    @FXML
    public void addRoute(ActionEvent actionEvent) {
        StopNode node1 = findStopNodeForButton(btn1);
        StopNode node2 = findStopNodeForButton(btn2);
        showRouteInfoPopup(node1,node2);
        drawLine(btn1,btn2);
        resetBtnStyle();
        hideAllPanels();
        buttons.setVisible(true);
        buttons.setDisable(false);
    }

    private void resetBtnStyle(){
        btn1.setStyle("-fx-background-radius: 5em; " +
                "-fx-min-width: 60px; " +
                "-fx-min-height: 60px; " +
                "-fx-max-width: 60px; " +
                "-fx-max-height: 60px;" +
                "-fx-background-color: WHITE;");
        btn2.setStyle("-fx-background-radius: 5em; " +
                "-fx-min-width: 60px; " +
                "-fx-min-height: 60px; " +
                "-fx-max-width: 60px; " +
                "-fx-max-height: 60px;" +
                "-fx-background-color: WHITE;");
        btn1 = null;
        btn2 = null;
    }

    private void resetAllLineColors() {
        // Iterate through all the children of the map pane
        for (Node node : map.getChildren()) {
            // Check if the node is a Line object
            if (node instanceof Line) {
                // Cast the node to Line and set its color back to white
                Line line = (Line) node;
                line.setStroke(Color.WHITE);
            } else if (node instanceof Button){
                Button button = (Button) node;
                button.setStyle("-fx-background-radius: 5em; " +
                        "-fx-min-width: 60px; " +
                        "-fx-min-height: 60px; " +
                        "-fx-max-width: 60px; " +
                        "-fx-max-height: 60px;" +
                        "-fx-background-color: WHITE;");
            }
        }
    }

    @FXML
    private void showTripPanel(){
        hideAllPanels();
        tripPanel.setVisible(true);
        tripPanel.setDisable(false);
    }

    @FXML
    private void deleteNode(ActionEvent actionEvent) {
        deletingMode = true;
    }

    @FXML
    private void findWay(ActionEvent actionEvent) {
        if(!dijkstra && !prim) return;
        StopNode node1 = findStopNodeForButton(btn1);
        StopNode node2 = findStopNodeForButton(btn2);
        if(dijkstra){
            List<StopNode> shortestPath = graph.dijkstraShortestPath(node1,node2,priority);
            if(shortestPath != null && !shortestPath.isEmpty()){
                highlightPath(shortestPath);
            }
        } else if(prim){
            List<Route> mstRoutes = graph.primMST(node1,priority);
            if(mstRoutes != null && !mstRoutes.isEmpty()){
                highlightMST(mstRoutes);
            }
        }
    }

    private void highlightMST(List<Route> mstRoutes) {
        for(Route route : mstRoutes){
            StopNode origin = route.getOrigin();
            StopNode destination = route.getDestination();
            Button ogButton = findButtonForStopNode(origin);
            Button destButton = findButtonForStopNode(destination);

            if(ogButton != null){
                ogButton.setStyle("-fx-background-radius: 5em; " +
                        "-fx-min-width: 60px; " +
                        "-fx-min-height: 60px; " +
                        "-fx-max-width: 60px; " +
                        "-fx-max-height: 60px;" +
                        "-fx-background-color: ORANGE;");
            }
            if(destButton != null){
                destButton.setStyle("-fx-background-radius: 5em; " +
                        "-fx-min-width: 60px; " +
                        "-fx-min-height: 60px; " +
                        "-fx-max-width: 60px; " +
                        "-fx-max-height: 60px;" +
                        "-fx-background-color: ORANGE;");
            }

            Line routeLine = findLineBetweenNodes(origin,destination);
            if(routeLine != null){
                routeLine.setStroke(javafx.scene.paint.Color.ORANGE);
                routeLine.setStrokeWidth(5);
            }
        }
    }

    private void highlightPath(List<StopNode> path) {
        for(int i = 0; i < path.size(); i++){
            StopNode currentNode = path.get(i);

            //find corresponding button for the current node
            Button currentButton = findButtonForStopNode(currentNode);
            if(currentButton != null){
                currentButton.setStyle("-fx-background-radius: 5em; " +
                        "-fx-min-width: 60px; " +
                        "-fx-min-height: 60px; " +
                        "-fx-max-width: 60px; " +
                        "-fx-max-height: 60px;" +
                        "-fx-background-color: GREEN;");
            }

            if(i < path.size() - 1){
                StopNode nextNode = path.get(i + 1);
                Line routeLine = findLineBetweenNodes(currentNode, nextNode);
                if(routeLine != null){
                    routeLine.setStroke(javafx.scene.paint.Color.GREEN);
                    routeLine.setStrokeWidth(5);
                }
            }
        }
    }
}
