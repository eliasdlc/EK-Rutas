package project.visual;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Pair;
import project.logic.*;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class graphCreationController implements ViewWindow.OnSelectedGraph{

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
    @FXML
    private FlowPane deletePanel;
    @FXML
    private FlowPane deleteRoutePanel;
    @FXML
    private TableView<Route> routesTable;
    @FXML
    private TableColumn<Route, String> nameColumn;
    @FXML
    private TableColumn<Route, Double> distanceColumn;
    @FXML
    private TableColumn<Route, Duration> timeColumn;
    @FXML
    private TableColumn<Route, Double> costColumn;
    @FXML
    private TableColumn<Route, Integer> transpColumn;
    private ObservableList<Route> routeList;
    private Graph graph = null;
    private final int baseNumVertex = 10;
    private final int baseRadius = 30;
    private Button btn1 = null;
    private Button btn2 = null;
    private boolean dijkstra = false;
    private boolean prim = false;
    private boolean deletingMode = false;
    private Criteria priority = null;
    private final Map<Button, TextField> buttonTextFieldMap = new HashMap<>();
    private final Map<Route, Line> routeLineMap = new HashMap<>();

    @Override
    public void getSelectedGraph(Graph graph) {
        this.graph = graph;
    }

    @FXML
    private void initialize() {
        hideAllPanels();
        showMainButtonPanel();

        initializeTable();

        if(graph == null){
            this.graph = new Graph(baseNumVertex, IdGenerator.generateId(), "Nuevo Grafo");
        }

        routesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showDeleteConfirmationDialog(newSelection);
            }
        });
    }

    void loadGraph(Graph graph) {
        this.graph = graph;
       map.getChildren().clear();

       for(StopNode node : graph.getNodes()){
           addNodeToMap(node);
       }

       for(StopNode node : graph.getListRoutes().keySet()){
           List<Route> routesFromNode = graph.getListRoutes().get(node);
           for(Route route : routesFromNode){
               if(!routeLineMap.containsKey(route)){
                   addRouteToMap(route);
               }
           }
       }


    }

    private void addNodeToMap(StopNode node) {
        // Verificar si el nodo ya está presente en el mapa
        if (buttonTextFieldMap.containsKey(node)) {
            return; // Si el nodo ya está en el mapa, no agregarlo
        }

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

        TextField txtFld = new TextField(node.getNombre());
        txtFld.setEditable(false);
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

        btn.setViewOrder(0);

        System.out.println("Node: " + node.getNombre() + " X: " + x + " Y: " + y);


        buttonTextFieldMap.put(btn, txtFld);
        map.getChildren().add(btn);
        map.getChildren().add(txtFld);
    }


    private void addRouteToMap(Route route) {
        if (routeLineMap.containsKey(route)) {
            return; // Si la ruta ya está en el mapa, no agregarla
        }

        StopNode origin = route.getOrigin();
        StopNode dest = route.getDestination();

        Line line = new Line();
        line.setStartX(origin.getPosX() + 60.0 / 2);
        line.setStartY(origin.getPosY() + 60.0 / 2);
        line.setEndX(dest.getPosX() + 60.0 / 2);
        line.setEndY(dest.getPosY() + 60.0 / 2);

        line.setStroke(Color.WHITE);
        line.setStrokeWidth(5);

        line.setViewOrder(-1);

        map.getChildren().add(line);
        routeLineMap.put(route,line);

        System.out.println("Route: " + route.getName() + " Origin: " + origin.getNombre() + " Destination: " + dest.getNombre());
    }

    @FXML
    private void exitTripSelection(ActionEvent event) {
        resetBtnStyle();
        resetAllLineColors();
        hideAllPanels();
        showMainButtonPanel();
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
        deletePanel.setVisible(false);
        deletePanel.setDisable(true);
        deleteRoutePanel.setVisible(false);
        deleteRoutePanel.setDisable(true);
    }

    private void showNodeHandlerPanel(){
        buttons.setVisible(false);
        buttons.setDisable(true);
        nodeHandlerPanel.setVisible(true);
        nodeHandlerPanel.setDisable(false);
    }

    private void showMainButtonPanel(){
        hideAllPanels();
        buttons.setVisible(true);
        buttons.setDisable(false);
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

            buttonTextFieldMap.put(nodeBtn, txtFld);
            map.getChildren().add(nodeBtn);
            map.getChildren().add(txtFld);

            txtFld.requestFocus();
            txtFld.setOnAction(textEvent -> {
                String name = txtFld.getText();
                if(graph.getNodes() != null) {
                    graph.getNodes().add(new StopNode(idNode, name, x, y));
                    txtFld.setEditable(false);
                }
            });
        });
    }

    private void ifGraphNull(Graph graph){
        if(graph == null){
            String idGraph = IdGenerator.generateId();
            this.graph = new Graph(baseNumVertex, idGraph, "Nuevo Grafo");
        }
    }

    private void handleNodeSelection(Button clickedButton){
        if(btn1 == null){
            btn1 = clickedButton;
            btn1.setStyle("-fx-background-radius: 5em; " +
                    "-fx-min-width: 60px; " +
                    "-fx-min-height: 60px; " +
                    "-fx-max-width: 60px; " +
                    "-fx-max-height: 60px;" +
                    "-fx-background-color: BLUE;");

            if(deletingMode && btn1 != null){
                hideAllPanels();
                deletePanel.setDisable(false);
                deletePanel.setVisible(true);
            }
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
                if ((Math.abs(startX - (node1.getPosX()+ 60.0 / 2)) < 1e-2 && Math.abs(startY - (node1.getPosY() + 60.0 / 2)) < 1e-2) ||
                        (Math.abs(endX - (node1.getPosX() + 60.0 / 2)) < 1e-2 && Math.abs(endY - (node1.getPosY() + 60.0 / 2)) < 1e-2)) {
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

    private void drawLine(Button btn1, Button btn2){
        Line line = new Line();
        line.setStartX(btn1.getLayoutX() + btn1.getWidth() / 2);
        line.setStartY(btn1.getLayoutY() + btn1.getHeight() / 2);
        line.setEndX(btn2.getLayoutX() + btn2.getWidth() / 2);
        line.setEndY(btn2.getLayoutY() + btn2.getHeight() / 2);

        // styling
        line.setStroke(javafx.scene.paint.Color.WHITE);
        line.setStrokeWidth(5);

        line.setViewOrder(-1);

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
        showMainButtonPanel();
    }

    private void resetBtnStyle(){
        if(btn1 != null){
            btn1.setStyle("-fx-background-radius: 5em; " +
                    "-fx-min-width: 60px; " +
                    "-fx-min-height: 60px; " +
                    "-fx-max-width: 60px; " +
                    "-fx-max-height: 60px;" +
                    "-fx-background-color: WHITE;");
        }
        if(btn2 != null){
            btn2.setStyle("-fx-background-radius: 5em; " +
                    "-fx-min-width: 60px; " +
                    "-fx-min-height: 60px; " +
                    "-fx-max-width: 60px; " +
                    "-fx-max-height: 60px;" +
                    "-fx-background-color: WHITE;");
        }
        btn1 = null;
        btn2 = null;
    }

    private void resetAllLineColors() {

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
    private void enableDeleteMode(ActionEvent actionEvent) {
        deletingMode = true;
    }

    @FXML
    private void findWay(ActionEvent actionEvent) {
        if(!dijkstra && !prim) return;
        StopNode node1 = findStopNodeForButton(btn1);
        StopNode node2 = findStopNodeForButton(btn2);
        if(dijkstra){
            System.out.println("pan");
            List<StopNode> shortestPath = graph.dijkstraShortestPath(node1,node2,priority);

            if(shortestPath != null && !shortestPath.isEmpty()){
                highlightPath(shortestPath);
                System.out.println("queso");
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

            Route routeToHighlight = graph.findRouteBetweenNodes(origin,destination);
            if(routeToHighlight != null){
                Line routeLine = routeLineMap.get(routeToHighlight);
                if(routeLine != null){
                    routeLine.setStroke(javafx.scene.paint.Color.ORANGE);
                    routeLine.setStrokeWidth(5);
                }
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
                Route route = graph.findRouteBetweenNodes(currentNode,nextNode);
                if(route != null){
                    Line routeLine = routeLineMap.get(route);
                    if(routeLine != null){
                        routeLine.setStroke(Color.GREEN);
                        routeLine.setStrokeWidth(5.0);
                    }
                }
            }
        }
    }

    @FXML
    private void removeNode(ActionEvent actionEvent) {
        hideAllPanels();
        StopNode nodeToRemove = findStopNodeForButton(btn1);

        if(nodeToRemove != null){
            removeConnectedRoutes(nodeToRemove);
            graph.removeNode(nodeToRemove);

            TextField txtFldToRemove = buttonTextFieldMap.get(btn1);
            if(txtFldToRemove != null){
                map.getChildren().remove(txtFldToRemove);
            }
            map.getChildren().remove(btn1);
            buttonTextFieldMap.remove(btn1);
        }
        deletingMode = false;
        showMainButtonPanel();
    }

    @FXML
    private void removeRoute(ActionEvent actionEvent) {
        hideAllPanels();
        deleteRoutePanel.setVisible(true);
        deleteRoutePanel.setDisable(false);

        StopNode node = findStopNodeForButton(btn1);
        displayRoutes(node);

        if(!deletingMode) {
            showMainButtonPanel();
        }
    }

    private void initializeTable(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));
        costColumn.setCellValueFactory(new PropertyValueFactory<>("cost"));
        transpColumn.setCellValueFactory(new PropertyValueFactory<>("trasbordo"));

        routeList = FXCollections.observableArrayList();
        routesTable.setItems(routeList);
    }

    private void displayRoutes(StopNode node){
        if(node == null){ return; }
        routeList.clear();
        List<Route> adjacentRoutes = graph.getListRoutes().get(node);
        if(adjacentRoutes != null && !adjacentRoutes.isEmpty()){
            routeList.addAll(adjacentRoutes);
        }
    }

    private void removeConnectedRoutes(StopNode nodeToRemove){
        List<Node> linesToRemove = new ArrayList<>();

        for(Node child : map.getChildren()){
            if(child instanceof Line){
                Line line = (Line) child;

                double startX = line.getStartX();
                double startY = line.getStartY();
                double endX = line.getEndX();
                double endY = line.getEndY();

                if ((Math.abs(startX - (nodeToRemove.getPosX() + 60.0 / 2)) < 1e-2 && Math.abs(startY - (nodeToRemove.getPosY() + 60.0 / 2)) < 1e-2) ||
                        (Math.abs(endX - (nodeToRemove.getPosX() + 60.0 / 2)) < 1e-2 && Math.abs(endY - (nodeToRemove.getPosY() + 60.0 / 2)) < 1e-2)) {
                    linesToRemove.add(line);  // Add the line to the removal list
                }
            }
        }
        map.getChildren().removeAll(linesToRemove);
    }

    private void showDeleteConfirmationDialog(Route route){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar");
        alert.setHeaderText("¿Está seguro de que quiere eliminar esta ruta?");
        alert.setContentText("Route: " + route.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            deleteRoute(route);
            resetBtnStyle();
            showMainButtonPanel();
        }
    }

    private void deleteRoute(Route route){
        graph.removeRoute(route);
        Line routeLine = routeLineMap.get(route);
        if(routeLine != null){
            map.getChildren().remove(routeLine);
            routeLineMap.remove(route);
        }
        deletingMode = false;
    }

    @FXML
    private void saveGraph(ActionEvent actionEvent) {
        Graph graphToSave = MapController.getInstance().searchGraphById(graph.getGraphId());
        if(graphToSave != null){
            MapController.getInstance().updateGraph(graphToSave);
        } else {
            MapController.getInstance().addGraph(graph);
        }
    }
}
