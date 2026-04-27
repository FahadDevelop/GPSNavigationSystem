import java.util.Scanner;

// Main application for GPS Navigation
public class Main {

    public static void main(String[] args) {
        Graph graph = new Graph();

        // Add sample nodes and edges
        graph.addNode(new Node("A", "Alpha", 12.9611, 77.6387));
        graph.addNode(new Node("B", "Bravo", 12.9722, 77.5958));
        graph.addNode(new Node("C", "Charlie", 12.9279, 77.6271));
        graph.addNode(new Node("D", "Delta", 12.9716, 77.5946));

        graph.addEdge("A", "B", 5.4);
        graph.addEdge("B", "C", 6.2);
        graph.addEdge("A", "C", 12.3);
        graph.addEdge("C", "D", 7.0);
        graph.addEdge("B", "D", 3.4);

        Scanner scanner = new Scanner(System.in);
        System.out.println("GPS Navigation System");
        System.out.print("Enter Start Node ID: ");
        String startId = scanner.nextLine().trim();
        System.out.print("Enter End Node ID: ");
        String endId = scanner.nextLine().trim();

        System.out.println("\n| Search Algorithm         | Execution Time (ns) |\n|-------------------------|--------------------|");

        // Dijkstra benchmark
        PerformanceBenchmark.benchmark("Dijkstra", () -> {
            try {
                double dijkstraDist = Pathfinder.findShortestDistanceDijkstra(graph, startId, endId);
                System.out.println("Dijkstra Shortest Distance: " + dijkstraDist);
            } catch (RuntimeException e) {
                System.out.println("Not reachable using Dijkstra.");
            }
        });

        // A* benchmark
        PerformanceBenchmark.benchmark("A-Star", () -> {
            try {
                double aStarDist = Pathfinder.findShortestDistanceAStar(graph, startId, endId);
                System.out.println("A* Shortest Distance: " + aStarDist);
            } catch (RuntimeException e) {
                System.out.println("Not reachable using A*.");
            }
        });

        scanner.close();
    }
}