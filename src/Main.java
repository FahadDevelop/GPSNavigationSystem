import java.util.Scanner;

// Main application for GPS Navigation
public class Main {
    public static int intInput(Scanner scanner, String output) {
        while (true) {
            System.out.print(output);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid integer input! Please enter a valid integer.");
            }
        }
    }

    public static double doubleInput(Scanner scanner, String output) {
        while (true) {
            System.out.print(output);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid float input! Please enter a valid float number.");
            }
        }
    }

    public static void returnMenu(Scanner scanner) {
        System.out.print("\nPress Enter to return...");
        scanner.nextLine();
    }

    private static void getEdges(Graph graph) {
        System.out.println("ID   From   To     BaseDist   TrafficFactor   Weight");
        for (Edge edge : graph.getAllEdges()) {
            System.out.printf("%-4d %-6s %-6s %-10.2f %-15.2f %-8.2f\n",
                edge.getId(),
                edge.getSource().getId(),
                edge.getTarget().getId(),
                edge.getBaseDistance(),
                edge.getTrafficFactor(),
                edge.getWeight());
        }
    }

    // Prints each segment (edge) in the route: from, to, baseDistance, trafficFactor, and weight
    private static void printRoute(java.util.List<Edge> route) {
        if (route == null || route.isEmpty()) {
            System.out.println("No route found.");
            return;
        }
        System.out.println();
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

    private static double calcTotalDistance(java.util.List<Edge> route) {
        double total = 0.0;
        if (route != null) {
            for (Edge edge : route) {
                total += edge.getBaseDistance();
            }
        }
        return total;
    }

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

        while (true) {
            System.out.println("""

GPS Navigation System

[1] List All Nodes
[2] List All Edges
[3] Find Shortest Route (Dijkstra)
[4] Find Shortest Route (A-Star)
[5] Update Edge Traffic
[0] Exit Program\n""");
            int choice = intInput(scanner, "Input: ");
            String startId, endId;
            System.out.println();
            switch (choice) {
                case 1: // List All Nodes
                    System.out.println("List All Nodes:\n");
                    System.out.println("ID   Name       Latitude     Longitude");
                    for (Node node : graph.getAllNodes()) {
                        System.out.printf("%-4s %-10s %-12.4f %-12.4f\n",
                            node.getId(),
                            node.getName(),
                            node.getLatitude(),
                            node.getLongitude());
                    }
                    break;
                case 2: // List All Edges
                    System.out.println("List All Edges:\n");
                    getEdges(graph);
                    break;
                case 3: // Find Shortest Route (Dijkstra)
                    System.out.println("Find Shortest Route (Dijkstra):\n");
                    System.out.print("Enter Start Node ID: ");
                    startId = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Enter End Node ID: ");
                    endId = scanner.nextLine().trim().toUpperCase();
                    System.out.println();

                    // Dijkstra benchmark and path print, including alternative route
                    PerformanceBenchmark.benchmark("Dijkstra", () -> {
                        try {
                            Pathfinder.AlternativeRoutesResult result = Pathfinder.findAlternativeRoutes(graph, startId, endId, "dijkstra");
                            System.out.println("Primary Route:");
                            System.out.println("Total Weighted Cost: " + String.format("%.2f", result.primary.totalWeight));
                            System.out.println("Total Distance: " + String.format("%.2f", calcTotalDistance(result.primary.path)));
                            printRoute(result.primary.path);

                            if (result.alternative != null) {
                                System.out.println("\nAlternative Route:");
                                System.out.println("Total Weighted Cost: " + String.format("%.2f", result.alternative.totalWeight));
                                System.out.println("Total Distance: " + String.format("%.2f", calcTotalDistance(result.alternative.path)));
                                printRoute(result.alternative.path);
                            } else {
                                System.out.println("\nNo alternative route found (all routes use the most congested segment).");
                            }
                        } catch (RuntimeException e) {
                            System.out.println("Not reachable using Dijkstra.");
                        } finally {
                            System.out.println();
                        }
                    });
                    break;
                case 4: // Find Shortest Route (A-Star)
                    System.out.println("Find Shortest Route (A-Star):\n");
                    System.out.print("Enter Start Node ID: ");
                    startId = scanner.nextLine().trim().toUpperCase();
                    System.out.print("Enter End Node ID: ");
                    endId = scanner.nextLine().trim().toUpperCase();
                    System.out.println();

                    // A* benchmark and path print, including alternative route
                    PerformanceBenchmark.benchmark("A-Star", () -> {
                        try {
                            Pathfinder.AlternativeRoutesResult result = Pathfinder.findAlternativeRoutes(graph, startId, endId, "astar");
                            System.out.println("Primary Route:");
                            System.out.println("Total Weighted Cost: " + String.format("%.2f", result.primary.totalWeight));
                            System.out.println("Total Distance: " + String.format("%.2f", calcTotalDistance(result.primary.path)));
                            printRoute(result.primary.path);

                            if (result.alternative != null) {
                                System.out.println("\nAlternative Route:");
                                System.out.println("Total Weighted Cost: " + String.format("%.2f", result.alternative.totalWeight));
                                System.out.println("Total Distance: " + String.format("%.2f", calcTotalDistance(result.alternative.path)));
                                printRoute(result.alternative.path);
                            } else {
                                System.out.println("\nNo alternative route found (all routes use the most congested segment).");
                            }
                        } catch (RuntimeException e) {
                            System.out.println("Not reachable using A*.");
                        } finally {
                            System.out.println();
                        }
                    });
                    break;
                case 5: // Update Edge Traffic
                    System.out.println("Update Edge Traffic:\n");
                    getEdges(graph);
                    System.out.println();
                    int edgeId = intInput(scanner, "Enter Edge ID to update: ");
                    double tf = doubleInput(scanner, "Enter new traffic factor (e.g., 1.0, 1.5, 2.5): ");
                    graph.updateTraffic(edgeId, tf);
                    break;
                case 0: // Exit Program
                    scanner.close();
                    return;
            }
            returnMenu(scanner);
        }
    }
}