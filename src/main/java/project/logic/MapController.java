package project.logic;

import java.util.ArrayList;
import java.util.List;

// Clase Controlador de Mapa
public class MapController {

    private List<StopNode> stops;
    private static MapController myMap = null;

    public MapController() {
        super();
        stops = new ArrayList<>();
    }

    public static MapController getInstance() {
        if(myMap == null) {
            myMap = new MapController();
        }return myMap;
    }

    public List<StopNode> getStops() {
        return stops;
    }

    public void setStops(List<StopNode> stops) {
        this.stops = stops;
    }

}
