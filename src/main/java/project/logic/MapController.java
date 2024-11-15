package project.logic;

import java.util.ArrayList;
import java.util.List;

public class MapController {

    private List<Graph> graphs;
    private static MapController myMap = null;

    public MapController() {
        super();
        graphs = new ArrayList<>();
    }

    public static MapController getInstance() {
        if(myMap == null) {
            myMap = new MapController();
        }return myMap;
    }

    public List<Graph> getGraphs() {
        return graphs;
    }

    public void addGraph(Graph graph) {
        graphs.add(graph);
    }

    public Graph searchGraphById(String id){
        for(Graph aux : graphs) {
            if(aux.getGraphId().equals(id)) {
                return aux;
            }
        }
        return null;
    }

}
