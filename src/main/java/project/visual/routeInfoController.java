package project.visual;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import project.logic.Graph;
import project.logic.MapController;
import project.logic.Route;
import project.logic.StopNode;

import java.time.Duration;

public class routeInfoController {
    @FXML
    private TextField txtOrigin;
    @FXML
    private TextField txtDest;
    @FXML
    private Spinner<Integer> spnHours;
    @FXML
    private Spinner<Integer> spnMinutes;
    @FXML
    private Button btnOk;
    @FXML
    private Spinner<Double> spnDistance;
    @FXML
    private Spinner<Integer> spnTransport;
    @FXML
    private TextField txtName;
    @FXML
    private Spinner<Double> spnCost;

    private StopNode originNode;
    private StopNode destNode;
    private Graph graph;

    public void setNodes(StopNode origin, StopNode destination) {
        this.originNode = origin;
        this.destNode = destination;
        populateFields();
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    @FXML
    public void initialize() {
        // Configure spnDistance (starter 0, max 1000, step 0.1)
        SpinnerValueFactory<Double> distanceValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 1000.0, 0.0, 0.1);
        spnDistance.setValueFactory(distanceValueFactory);
        spnDistance.setEditable(true);  // Allows the user to type a value as well

        // Configure spnTransport (starter 1, min 1, max 4)
        SpinnerValueFactory<Integer> transportValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4, 1);
        spnTransport.setValueFactory(transportValueFactory);
        spnTransport.setEditable(false);  // This can be true if you want to allow manual input

        // Configure spnCost (starter 0, max 5000, step 0.1)
        SpinnerValueFactory<Double> costValueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, 5000.0, 0.0, 0.1);
        spnCost.setValueFactory(costValueFactory);
        spnCost.setEditable(true);

        // Configure time spinners (spnHours and spnMinutes)
        SpinnerValueFactory<Integer> hoursValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0);
        spnHours.setValueFactory(hoursValueFactory);

        SpinnerValueFactory<Integer> minutesValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0);
        spnMinutes.setValueFactory(minutesValueFactory);

        // set texts
        if(originNode != null && destNode != null) {
            System.out.println("hi?");
            txtOrigin.setText(originNode.getNombre());
            txtDest.setText(destNode.getNombre());
        }

    }

    private void populateFields(){
        if(originNode != null && destNode != null) {
            txtOrigin.setText(originNode.getNombre());
            txtDest.setText(destNode.getNombre());
        }
    }

    public void setBtnOk(ActionEvent event) {
        double distance = spnDistance.getValue();
        double cost = spnCost.getValue();
        int hours = spnHours.getValue();
        int minutes = spnMinutes.getValue();
        int transports = spnTransport.getValue();
        String name = txtName.getText();
        Duration totalTime = Duration.ofHours(hours).plusMinutes(minutes);
        if(graph!=null && originNode!=null && destNode!=null && name!=null) {
            Route route = new Route(originNode,destNode,name,distance,totalTime,cost,transports);
            graph.addListAdy(route);
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
