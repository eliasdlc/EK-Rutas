package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

public class Graph {
	private HashMap<StopNode, List<Route>> listRoutes;
    private final String graphId;
    private List<StopNode> nodes;
    private int numVertex;

	public Graph(int numVertex, String graphId) {
		setListRoutes(new HashMap<>(numVertex));
        this.graphId = graphId;
        this.nodes = new ArrayList<>();
        this.numVertex = numVertex;
	}

    public int getNumVertex() {
        return numVertex;
    }

    public void setNumVertex(int nummVertex) {
        this.numVertex = nummVertex;
    }

    public List<StopNode> getNodes() {
        return nodes;
    }

    public void setNodes(List<StopNode> nodes) {
        this.nodes = nodes;
    }

    public List<StopNode> dijkstraShortestPath(StopNode origin, StopNode destination, Criteria criterio) {
        // Inicializar las estructuras
        Map<StopNode, Double> distancias = new HashMap<>();
        Map<StopNode, StopNode> predecesores = new HashMap<>();
        PriorityQueue<StopNode> pq = new PriorityQueue<>(Comparator.comparingDouble(distancias::get));
        Set<StopNode> visitados = new HashSet<>();

        // Inicializar todas las distancias a infinito, excepto el origen
        for (StopNode nodo : listRoutes.keySet()) {
            distancias.put(nodo, Double.POSITIVE_INFINITY);
        }
        distancias.put(origin, 0.0);

        // Agregar el nodo origen a la cola de prioridad
        pq.add(origin);

        // Algoritmo de Dijkstra
        while (!pq.isEmpty()) {
            StopNode nodoActual = pq.poll(); // Extraer el nodo con la distancia mínima

            if (nodoActual.equals(destination)) {
                break; // Hemos llegado al nodo destino
            }

            if (!visitados.contains(nodoActual)) {
                visitados.add(nodoActual);

                // Revisar las rutas adyacentes al nodo actual
                List<Route> adyacentes = listRoutes.get(nodoActual);
                if (adyacentes != null) {
                    for (Route ruta : adyacentes) {
                        StopNode vecino = ruta.getDestination();
                        if (visitados.contains(vecino)) continue;
                        // Obtener el peso según el criterio
                        double peso = getWeightByCriterion(ruta,criterio);
                        // Relajación de la arista
                        double nuevaDistancia = distancias.get(nodoActual) + peso;
                        if (nuevaDistancia < distancias.get(vecino)) {
                            distancias.put(vecino, nuevaDistancia);
                            predecesores.put(vecino, nodoActual); // Guardar el nodo anterior en la ruta más corta
                            pq.add(vecino);
                        }
                    }
                }
            }
        }

        // Reconstruir el camino más corto desde el destino al origen
        List<StopNode> camino = new ArrayList<>();
        for (StopNode nodo = destination; nodo != null; nodo = predecesores.get(nodo)) {
            camino.add(nodo);
        }
        Collections.reverse(camino); // Revertir la lista para obtener el camino desde el origen al destino

        // Verificar si el destino es alcanzable
        if (camino.isEmpty() || !camino.get(0).equals(origin)) {
            return new ArrayList<>(); // Retornar lista vacía si no hay camino
        }

        return camino; // Retornar la lista de nodos que conforman la ruta más corta
    }

    // ARBOL DE EXPANSION MINIMA (MST) CON PRIM
    public List<Route> primMST(Criteria criterio) {
        // Verificación inicial: si el grafo está vacío o no tiene rutas, no hay MST
        if (listRoutes.isEmpty()) return new ArrayList<>();

        List<Route> mst = new ArrayList<>(); // Almacena las aristas del MST
        Set<StopNode> visitados = new HashSet<>(); // Conjunto de nodos ya incluidos en el MST
        PriorityQueue<Route> pq = new PriorityQueue<>(Comparator.comparingDouble(route -> getWeightByCriterion(route, criterio)));

        // Inicializar el algoritmo desde un nodo arbitrario (primer nodo en la lista)
        StopNode nodoInicial = listRoutes.keySet().iterator().next();
        visitados.add(nodoInicial);

        // Añadir todas las rutas adyacentes al nodo inicial a la cola de prioridad
        for (Route ruta : listRoutes.get(nodoInicial)) {
            pq.add(ruta);
        }

        // Mientras no se hayan visitado todos los nodos
        while (!pq.isEmpty() && visitados.size() < listRoutes.size()) {
            // Seleccionar la ruta de menor peso
            Route rutaActual = pq.poll();
            StopNode origen = rutaActual.getOrigin();
            StopNode destino = rutaActual.getDestination();

            // Determinar el nodo aún no visitado
            StopNode siguienteNodo = visitados.contains(origen) ? destino : origen;

            // Si el nodo de destino ya está visitado, ignorar esta arista
            if (visitados.contains(siguienteNodo)) continue;

            // Añadir el nodo al conjunto de visitados
            visitados.add(siguienteNodo);

            // Añadir la ruta seleccionada al MST
            mst.add(rutaActual);

            // Añadir todas las rutas adyacentes al nuevo nodo a la cola de prioridad
            for (Route ruta : listRoutes.get(siguienteNodo)) {
                if (!visitados.contains(ruta.getDestination())) {
                    pq.add(ruta);
                }
            }
        }

        return mst; // Retornar el conjunto de aristas que forman el MST
    }

    // Metodo auxiliar para obtener el peso según el criterio especificado
    private double getWeightByCriterion(Route route, Criteria criterio) {
        if(criterio == null){
            double v = (3 * route.getCost() + 2 * route.getDistance() + route.getTime().getSecond() + (1 / 2) * (double) route.getTrasbordo()) /
                    (route.getCost() + route.getDistance() + route.getTime().getSecond() + route.getTrasbordo());
            return v;
        }
        return switch (criterio) {
            case DISTANCIA -> route.getDistance();
            case COSTO -> route.getCost();
            case TIEMPO -> route.getTime().toSecondOfDay(); // Convertir tiempo a segundos
            case TRANSBORDO -> route.getTrasbordo();
        };
    }

	public void addNodeAdyList(StopNode node) {
	    if (!listRoutes.containsKey(node)) {
	        listRoutes.put(node, new ArrayList<>());
	    }
	}
	
	public void addListAdy(Route route) {
        getListRoutes().get(route.getOrigin()).add(route);
        getListRoutes().get(route.getDestination()).add(route);
    }



	public void removeRoute(Route route) {
	    StopNode origin = route.getOrigin();
	    StopNode destination = route.getDestination();

	    // para nodo origen
	    List<Route> originRoutes = listRoutes.get(origin);
	    if (originRoutes != null) {
	        originRoutes.remove(route);
	    }

	    // para nodo destino
	    List<Route> destinationRoutes = listRoutes.get(destination);
	    if (destinationRoutes != null) {
	        destinationRoutes.remove(route);
	    }
	}
	
	public void removeNode(StopNode node) {
	    List<Route> routesToRemove = listRoutes.get(node);
	    
	    if (routesToRemove != null) {
	        // Eliminar las rutas desde las listas de sus nodos adyacentes
	        for (Route route : routesToRemove) {
	            StopNode otherNode = route.getOrigin().equals(node) ? route.getDestination() : route.getOrigin();
	            List<Route> otherRoutes = listRoutes.get(otherNode);
	            if (otherRoutes != null) {
	                otherRoutes.remove(route);
	            }
	        }
	    }
	    listRoutes.remove(node);
	}

    public StopNode searchNodeById(String id) {
        for(StopNode aux : listRoutes.keySet()) {
            if(aux.getIdNodo().equals(id)) {
                return aux;
            }
        }
        return null;
    }

    //                                                     DELETE LATER
    public void printMST(Criteria criterio) {
        // Get the MST list of routes
        List<Route> mst = primMST(criterio);

        // Check if the MST is empty (no valid spanning tree found)
        if (mst.isEmpty()) {
            System.out.println("No Minimum Spanning Tree (MST) found for the graph.");
            return;
        }

        // Print the MST routes with the corresponding weight based on the criterion
        System.out.println("Minimum Spanning Tree (MST) based on " + criterio + ":");
        for (Route route : mst) {
            StopNode origin = route.getOrigin();
            StopNode destination = route.getDestination();
            double weight = getWeightByCriterion(route, criterio);

            System.out.println("Route from " + origin.getNombre() + " to " + destination.getNombre() + " - "
                    + criterio + ": " + weight);
        }
    }

    public List<StopNode> getAdjacentDestinations(){
        return new ArrayList<>(listRoutes.keySet());
    }

    //si no hay Routes devuelve una lista vacia
    public List<Route> getRoutesToDestination(StopNode dest){
        return listRoutes.getOrDefault(dest, new ArrayList<>());
    }
    
    public HashMap<StopNode, List<Route>> getListRoutes() {
  		return listRoutes;
  	}

    public String getGraphId(){
        return graphId;
    }
  	public void setListRoutes(HashMap<StopNode, List<Route>> listRoutes) {
  		this.listRoutes = listRoutes;
  	}
}
