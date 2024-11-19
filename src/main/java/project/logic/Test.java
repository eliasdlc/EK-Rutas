package project.logic;

import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

public class Test {

    public static void main (String[] args) {
        Graph graph = new Graph(10, "1");
        StopNode node1 = new StopNode("1", "A", 0, 0);
        StopNode node2 = new StopNode("2", "B", 0, 0);
        StopNode node3 = new StopNode("3", "C", 0, 0);
        StopNode node4 = new StopNode("4", "D", 0, 0);
        StopNode node5 = new StopNode("5", "E", 0, 0);
        StopNode node6 = new StopNode("6", "F", 0, 0);
        StopNode node7 = new StopNode("7", "G", 0, 0);

        Route route1 = new Route(node1, node2, "tal1", "1", 10, LocalTime.of(0, 15, 0), 10, 1);  // 15 minutos
        Route route2 = new Route(node2, node3, "tal2", "2", 1, LocalTime.of(0, 10, 0), 14, 2);  // 10 minutos
        Route route3 = new Route(node3, node4, "tal3", "3", 20, LocalTime.of(0, 25, 0), 70, 0);  // 25 minutos
        Route route4 = new Route(node4, node5, "tal4", "4", 10, LocalTime.of(0, 20, 0), 1000, 3); // 20 minutos
        Route route5 = new Route(node5, node6, "tal5", "5", 3, LocalTime.of(0, 12, 0), 45, 0);   // 12 minutos
        Route route6 = new Route(node6, node7, "tal6", "6", 3, LocalTime.of(0, 4, 0), 35, 10);  // 18 minutos
        Route route7 = new Route(node1, node7, "tal7", "7", 5, LocalTime.of(0, 5, 0), 5, 1);    // 30 minutos
        Route route8 = new Route(node3, node6, "tal8", "8", 7, LocalTime.of(0, 22, 0), 10, 5);   // 22 minutos
        Route route9 = new Route(node4, node7, "tal9", "9", 30, LocalTime.of(0, 5, 0), 30, 1); // 5 minutos

        Route route1reverse = new Route(node2, node1, "tal1", "1", 10, LocalTime.of(0, 15, 0), 10, 1);  // 15 minutos
        Route route2reverse = new Route(node3, node2, "tal2", "2", 1, LocalTime.of(0, 10, 0), 14, 2);  // 10 minutos
        Route route3reverse = new Route(node4, node3, "tal3", "3", 20, LocalTime.of(0, 25, 0), 70, 0);  // 25 minutos
        Route route4reverse = new Route(node5, node4, "tal4", "4", 10, LocalTime.of(0, 20, 0), 1000, 3); // 20 minutos
        Route route5reverse = new Route(node6, node5, "tal5", "5", 3, LocalTime.of(0, 12, 0), 45, 0);   // 12 minutos
        Route route6reverse = new Route(node7, node6, "tal6", "6", 13, LocalTime.of(0, 4, 0), 35, 10); // 18 minutos
        Route route7reverse = new Route(node7, node1, "tal7", "7", 5, LocalTime.of(0, 5, 0), 5, 1);    // 30 minutos
        Route route8reverse = new Route(node6, node3, "tal8", "8", 7, LocalTime.of(0, 22, 0), 10, 5);   // 22 minutos
        Route route9reverse = new Route(node7, node4, "tal9", "9", 30, LocalTime.of(0, 5, 0), 30, 1); // 5 minutos

        node1.getRoutes().add(route1);
        node2.getRoutes().add(route2);
        node3.getRoutes().add(route3);
        node4.getRoutes().add(route4);
        node5.getRoutes().add(route5);
        node6.getRoutes().add(route6);
        node7.getRoutes().add(route7);
        node1.getRoutes().add(route7);
        node3.getRoutes().add(route8);
        node4.getRoutes().add(route9);

        node2.getRoutes().add(route1reverse);
        node3.getRoutes().add(route2reverse);
        node4.getRoutes().add(route3reverse);
        node5.getRoutes().add(route4reverse);
        node6.getRoutes().add(route5reverse);
        node7.getRoutes().add(route6reverse);
        node1.getRoutes().add(route7reverse);
        node6.getRoutes().add(route8reverse);
        node7.getRoutes().add(route9reverse);

        graph.getNodes().add(node1);
        graph.getNodes().add(node2);
        graph.getNodes().add(node3);
        graph.getNodes().add(node4);
        graph.getNodes().add(node5);
        graph.getNodes().add(node6);
        graph.getNodes().add(node7);

        graph.getListRoutes().put(node1, node1.getRoutes());
        graph.getListRoutes().put(node2, node2.getRoutes());
        graph.getListRoutes().put(node3, node3.getRoutes());
        graph.getListRoutes().put(node4, node4.getRoutes());
        graph.getListRoutes().put(node5, node5.getRoutes());
        graph.getListRoutes().put(node6, node6.getRoutes());
        graph.getListRoutes().put(node7, node7.getRoutes());
    /*
        // Single Scanner instance for the entire program
        Scanner scanner = new Scanner(System.in);

        System.out.println("Dijkstra");
        System.out.println("Inserte el id del nodo origen: ");
        String origenId = scanner.nextLine();

        System.out.println("Inserte el id del destino: ");
        String destinoId = scanner.nextLine();

        StopNode origen = graph.searchNodeById(origenId);
        StopNode destino = graph.searchNodeById(destinoId);
        List<StopNode> shortestPath = graph.dijkstraShortestPath(origen, destino, Criteria.TIME);
        System.out.print("shortestPath: ");
        for (StopNode stopNode : shortestPath) {
            System.out.print(stopNode.getIdNodo()+" ");
        }
        System.out.println(" ");
        System.out.println("PRIM MST");
        System.out.println("Elija el criterio para realizar el algoritmo (distancia/costo/tiempo)");
        String criterio = scanner.nextLine();
        graph.printMST(criterio);

        // Close the scanner only at the very end
        scanner.close();

    */
    }
}
