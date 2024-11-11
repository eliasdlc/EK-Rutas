package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// Clase Nodo
public class StopNode {

    private String idNodo;
    private String nombre;
    private int posX;
    private int posY;
    private HashMap<StopNode, List<Route>> listRoutes;

    public StopNode(String idNodo, String nombre, int posX, int posY) {
        super();
        this.idNodo = idNodo;
        this.nombre = nombre;
        this.posX = posX;
        this.posY = posY;

        //we might wanna fix the resizing issue here later, if necessary
        this.listRoutes = new HashMap<>();
    }

    public void addRoute(StopNode dest, Route route) {
        listRoutes.putIfAbsent(dest, new ArrayList<>());
        listRoutes.get(dest).add(route);
    }

    public void removeRoute(StopNode dest, Route route) {
        List<Route> routes = listRoutes.get(dest);

        if ( routes != null ) {
            routes.remove(route);

            if ( routes.isEmpty() ){ //if there's nothing left in the list then the destination gets removed.
                listRoutes.remove(dest);
            }
        }
    }

    public void removeStopNode(StopNode node) {
        node.getAdjacentDestinations().forEach(destination -> node.removeRoute(destination, null));

        // Remove all the references of this node to other nodes
        for ( StopNode otherNode : listRoutes.keySet() ) {
            otherNode.getAdjacentDestinations().forEach(destination -> {
                if (destination.equals(node)) {
                    otherNode.removeRoute(node, null);
                }
            });
        }
    }

    public List<StopNode> getAdjacentDestinations(){
        return new ArrayList<>(listRoutes.keySet());
    }

    //si no hay Routes devuelve una lista vac�a
    public List<Route> getRoutesToDestination(StopNode dest){
        return listRoutes.getOrDefault(dest, new ArrayList<>());
    }

    public String getIdNodo() {
        return idNodo;
    }

    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public int getPosX() {
        return posX;
    }


    public void setPosX(int posX) {
        this.posX = posX;
    }


    public int getPosY() {
        return posY;
    }


    public void setPosY(int posY) {
        this.posY = posY;
    }

}
