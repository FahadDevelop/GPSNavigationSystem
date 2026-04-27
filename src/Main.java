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

        while (true) {
            System.out.print("Enter Start Node ID: ");
            String startId = scanner.nextLine().trim();
            System.out.print("Enter End Node ID: ");
            String endId = scanner.nextLine().trim();

            System.out.println("\n| Algorithm               | Exec Time (ns)      |");
            System.out.println("|-------------------------|---------------------|");

            // Dijkstra benchmark and path print
            PerformanceBenchmark.benchmark("Dijkstra", () -> {
                try {
                    Pathfinder.PathResult result = Pathfinder.findShortestPathDijkstra(graph, startId, endId);
                    System.out.println("Dijkstra Shortest Distance: " + result.totalWeight);
                    printRoute(result.path);
                } catch (RuntimeException e) {
                    System.out.println("Not reachable using Dijkstra.");
                }
            });

            // A* benchmark and path print
            PerformanceBenchmark.benchmark("A-Star", () -> {
                try {
                    Pathfinder.PathResult result = Pathfinder.findShortestPathAStar(graph, startId, endId);
                    System.out.println("A* Shortest Distance: " + result.totalWeight);
                    printRoute(result.path);
                } catch (RuntimeException e) {
                    System.out.println("Not reachable using A*.");
                }
            });

            // Optionally, allow live traffic update and show new route
            System.out.print("\nUpdate traffic? (y/n): ");
            if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
                System.out.println("Available edges:");
                for (Edge edge : graph.getAllEdges()) {
                    System.out.printf("Edge ID %d: %s -> %s | Distance: %.2f | Traffic factor: %.1f | Weight: %.2f\n",
                        edge.getId(), edge.getSource().getId(), edge.getTarget().getId(),
                        edge.getBaseDistance(), edge.getTrafficFactor(), edge.getWeight());
                }
                System.out.print("Enter Edge ID to update: ");
                int edgeId = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Enter new traffic factor (e.g., 1.0, 1.5, 2.5): ");
                double tf = Double.parseDouble(scanner.nextLine().trim());
                graph.updateTraffic(edgeId, tf);

                System.out.println("\nRe-running search after traffic update...");
                // Dijkstra after update
                try {
                    Pathfinder.PathResult result = Pathfinder.findShortestPathDijkstra(graph, startId, endId);
                    System.out.println("New Dijkstra Distance: " + result.totalWeight);
                    printRoute(result.path);
                } catch (RuntimeException e) {
                    System.out.println("Not reachable (Dijkstra) after update.");
                }
                try {
                    Pathfinder.PathResult result = Pathfinder.findShortestPathAStar(graph, startId, endId);
                    System.out.println("New A* Distance: " + result.totalWeight);
                    printRoute(result.path);
                } catch (RuntimeException e) {
                    System.out.println("Not reachable (A*) after update.");
                }
            }

            System.out.print("\nAnother query? (y/n): ");
            if (!scanner.nextLine().trim().equalsIgnoreCase("y")) break;
        }
        scanner.close();
    }

    // Prints each segment (edge) in the route: from, to, baseDistance, trafficFactor, and weight
    private static void printRoute(java.util.List<Edge> route) {
        if (route == null || route.isEmpty()) {
            System.out.println("No route found.");
            return;
        }
        System.out.println("Route segments:");
        System.out.printf("%-6s %-6s %-12s %-15s %-10s\n", "From", "To", "BaseDist", "TrafficFactor", "Weight");
        for (Edge edge : route) {
            System.out.printf("%-6s %-6s %-12.2f %-15.2f %-10.2f\n",
                edge.getSource().getId(),
                edge.getTarget().getId(),
                edge.getBaseDistance(),
                edge.getTrafficFactor(),
                edge.getWeight());
        }
    }
}