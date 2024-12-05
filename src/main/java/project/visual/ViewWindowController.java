package project.visual;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import project.logic.Graph;
import project.logic.MapController;
import project.logic.Route;
import project.logic.StopNode;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ViewWindowController {

    @FXML
    public Button homeButton, optionsButton, closeButton;

    private Stage stage;
    private Scene scene;

    @FXML
    private FlowPane grafoList;

    @FXML
    private void initialize() {
        List<Graph> graphs = MapController.getInstance().getGraphs(); // Aqui seria poner esto despues de terminar el test
                                             // MapController.getInstance().getGraphs();

        // Crear y configurar GridPane para cada grafo
        for (Graph graph : graphs) {
            GridPane graphPane = new GridPane();
            configureGraphPane(graphPane, graph);
            grafoList.getChildren().add(graphPane);
        }

        List<Button> sidebarButtons = new ArrayList<>(List.of(homeButton, optionsButton, closeButton));

        for (Button button : sidebarButtons) {
            setupSidebarButtonAnimation(button);
        }

        /*
        * TODO: Que cuando se le haga click al elemento te abra el grafo el la visualizacion
        * TODO: Hacer Hover effect
        * TODO: Opcion para borrar y visualizar (puede ser click derecho o un popup)*/
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

    @FXML
    public void closeApp(ActionEvent e){
        MapController.saveData();
        stage = (Stage)((Node)e.getSource()).getScene().getWindow();
        stage.close();
    }

    private void setupSidebarButtonAnimation(Button bt){
        setupSidebarBttnAnimation(bt);
    }

    static void setupSidebarBttnAnimation(Button bt) {
        DropShadow shadow = new DropShadow(16, Color.rgb(0, 0, 0, 0.2));
        shadow.setOffsetY(4);
        bt.setEffect(shadow);

        Timeline hoverAnimation = new Timeline(
                new KeyFrame(javafx.util.Duration.seconds(0.2), // Duration of animation
                        new KeyValue(shadow.spreadProperty(), 0.8), // Increase shadow spread
                        new KeyValue(bt.scaleXProperty(), 1.05), // Slightly increase button scale
                        new KeyValue(bt.scaleYProperty(), 1.05)) // Slightly increase button scale
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(javafx.util.Duration.seconds(0.2),
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


    private void configureGraphPane(GridPane graphPane, Graph graph) {
        graphPane.setPrefWidth(1300);
        graphPane.setPrefHeight(180);
        graphPane.setStyle("-fx-background-color: #D9D9D9; -fx-background-radius: 16");

        // Crear y configurar el contenedor para la imagen
        Pane imageView = new Pane();
        imageView.setPrefWidth(140);
        imageView.setPrefHeight(140);
        imageView.setStyle("-fx-background-color: linear-gradient(from 90% 100% to 10% 0%, #07bbff 15%, #22a4d3 75%); -fx-background-radius: 16");
        GridPane.setHalignment(imageView, HPos.CENTER);
        GridPane.setValignment(imageView, VPos.CENTER);
        GridPane.setMargin(imageView, new javafx.geometry.Insets(20, 20, 20, 20));
        graphPane.addColumn(0, imageView);

        // Crear y configurar la información del grafo
        FlowPane graphInfo = new FlowPane();
        graphInfo.setPrefWidth(1000);
        graphInfo.setPrefHeight(160);
        graphInfo.setStyle("-fx-background-color: transparent;");
        graphInfo.setHgap(20);
        graphInfo.setVgap(20);
        graphInfo.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));
        GridPane.setHalignment(graphInfo, HPos.CENTER);
        GridPane.setValignment(graphInfo, VPos.CENTER);
        GridPane.setMargin(graphInfo, new javafx.geometry.Insets(10, 10, 10, 10));
        graphPane.addColumn(1, graphInfo);

        // Título del grafo
        Label graphTitle = new Label(graph.getNombre());
        graphTitle.setPrefWidth(1000);
        graphTitle.setStyle("-fx-font-size: 24; -fx-font-weight: bold; -fx-text-fill: black");
        graphInfo.getChildren().add(graphTitle);

        // Información del grafo
        Label cantParadas = new Label("Cantidad de paradas: " + graph.getCantNodos());
        cantParadas.setStyle("-fx-font-size: 18; -fx-text-fill: black");
        graphInfo.getChildren().add(cantParadas);

        Label cantRutas = new Label("Cantidad de rutas: " + graph.getCantRutas());
        cantRutas.setStyle("-fx-font-size: 18; -fx-text-fill: black");
        graphInfo.getChildren().add(cantRutas);

        ContextMenu contextMenu = new ContextMenu();

        javafx.scene.control.MenuItem viewOption = new javafx.scene.control.MenuItem("Visualizar");
        viewOption.setOnAction(e -> {
            visualizeGraph(graph);
        });

        // Opción de eliminar
        javafx.scene.control.MenuItem deleteOption = new javafx.scene.control.MenuItem("Eliminar");
        deleteOption.setOnAction(e -> {
            grafoList.getChildren().remove(graphPane); // Eliminar del contenedor
            // Aquí podrías agregar lógica adicional para eliminar el grafo del modelo si es necesario.
        });

        contextMenu.getItems().addAll(viewOption, deleteOption);

        // Asociar el menú contextual al `GridPane`
        graphPane.setOnContextMenuRequested(event -> {
            contextMenu.show(graphPane, event.getScreenX(), event.getScreenY());
        });

    }

    private void visualizeGraph(Graph graph) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("graph-creation.fxml"));

            double screenWidth = Screen.getPrimary().getBounds().getWidth();
            double screenHeight = Screen.getPrimary().getBounds().getHeight();

            Scene newScene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);

            String css = Objects.requireNonNull(this.getClass().getResource("graphCreation.css")).toExternalForm();
            newScene.getStylesheets().add(css);

            Stage currentStage = (Stage) grafoList.getScene().getWindow();

            currentStage.setScene(newScene);
            currentStage.centerOnScreen();
            currentStage.setMaximized(true);

            graphCreationController controller = fxmlLoader.getController();
            controller.loadGraph(graph);

        } catch (IOException e) {
            e.printStackTrace();
        }
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


    public List<Graph> genTestGraphs() {
        Graph graph1 = new Graph(10, "1","graph1");
        Graph graph2 = new Graph(15, "1","graph2");
        Graph graph3 = new Graph(20, "1","graph3");

        // Configurar nodos y rutas para graph1
        List<StopNode> nodes1 = new ArrayList<>();
        nodes1.add(new StopNode("N1", "A", 1.0, 1.0));
        nodes1.add(new StopNode("N2", "B", 2.0, 1.5));
        nodes1.add(new StopNode("N3", "C", 3.0, 3.0));
        nodes1.add(new StopNode("N4", "D", 4.0, 2.0));
        nodes1.add(new StopNode("N5", "E", 5.0, 4.0));

        HashMap<StopNode, List<Route>> routes1 = new HashMap<>();
        routes1.put(nodes1.get(0), List.of(
                new Route(nodes1.get(0), nodes1.get(1), "Route1-AB", 1.5, Duration.ofMinutes(5), 1.0, 0),
                new Route(nodes1.get(0), nodes1.get(2), "Route1-AC", 2.0, Duration.ofMinutes(7), 1.5, 0)
        ));
        routes1.put(nodes1.get(1), List.of(
                new Route(nodes1.get(1), nodes1.get(3), "Route1-BD", 1.8, Duration.ofMinutes(6), 1.2, 1)
        ));
        routes1.put(nodes1.get(2), List.of(
                new Route(nodes1.get(2), nodes1.get(4), "Route1-CE", 2.5, Duration.ofMinutes(8), 2.0, 1)
        ));

        graph1.setNodes(nodes1);
        graph1.setListRoutes(routes1);

        // Configurar nodos y rutas para graph2
        List<StopNode> nodes2 = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            nodes2.add(new StopNode("N" + i, "Node " + i, i * 1.5, i * 1.2));
        }

        HashMap<StopNode, List<Route>> routes2 = new HashMap<>();
        for (int i = 0; i < nodes2.size() - 1; i++) {
            StopNode origin = nodes2.get(i);
            StopNode destination = nodes2.get(i + 1);
            routes2.putIfAbsent(origin, new ArrayList<>());
            routes2.get(origin).add(new Route(origin, destination, "Route2-" + origin.getNombre() + "-" + destination.getNombre(),
                    2.0 + i, Duration.ofMinutes(5 + i), 1.5 + i * 0.2, i % 2));
        }

        graph2.setNodes(nodes2);
        graph2.setListRoutes(routes2);

        // Configurar nodos y rutas para graph3
        List<StopNode> nodes3 = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            nodes3.add(new StopNode("N" + i, "Node " + i, i * 2.0, i * 2.5));
        }

        HashMap<StopNode, List<Route>> routes3 = new HashMap<>();
        for (int i = 0; i < nodes3.size() - 2; i++) {
            StopNode origin = nodes3.get(i);
            StopNode destination1 = nodes3.get(i + 1);
            StopNode destination2 = nodes3.get(i + 2);
            routes3.putIfAbsent(origin, new ArrayList<>());
            routes3.get(origin).add(new Route(origin, destination1, "Route3-" + origin.getNombre() + "-" + destination1.getNombre(),
                    3.0 + i, Duration.ofMinutes(6 + i), 2.0 + i * 0.3, i % 3));
            routes3.get(origin).add(new Route(origin, destination2, "Route3-" + origin.getNombre() + "-" + destination2.getNombre(),
                    4.0 + i, Duration.ofMinutes(7 + i), 2.5 + i * 0.3, (i + 1) % 3));
        }

        graph3.setNodes(nodes3);
        graph3.setListRoutes(routes3);

        return List.of(graph1, graph2, graph3);
    }

}
