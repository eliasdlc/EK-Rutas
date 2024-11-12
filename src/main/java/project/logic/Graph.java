package project.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
	private HashMap<StopNode, List<Route>> listRoutes;
	
	public Graph(int numVertex) {
		setListRoutes(new HashMap<>(numVertex));
	} 
	
	public List<StopNode> dijkstraShortestPath(StopNode origin, StopNode destination, String criterio) {
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
                        double peso = 0.0;
                        switch (criterio.toLowerCase()) {
                            case "distancia":
                                peso = ruta.getDistance();
                                break;
                            case "costo":
                                peso = ruta.getCost();
                                break;
                            case "tiempo":
                                peso = ruta.getTime().toSecondOfDay();
                                break;
                            default:
                                throw new IllegalArgumentException("Criterio no válido. Use 'distancia', 'costo' o 'tiempo'.");
                        }

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
	
	public void addNode(StopNode node) {
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

  	public void setListRoutes(HashMap<StopNode, List<Route>> listRoutes) {
  		this.listRoutes = listRoutes;
  	}
}
