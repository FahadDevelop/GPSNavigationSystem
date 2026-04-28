import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * Main application for the GPS Navigation System.
 * Provides a console-based user interface.
 */
public class Main {

    /**
     * Reads an integer from the user with prompt and validation.
     * @param scanner Scanner for input
     * @param output Prompt message
     * @return Valid integer input
     */
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

    /**
     * Reads a double from the user with prompt and validation.
     * @param scanner Scanner for input
     * @param output Prompt message
     * @return Valid double input
     */
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

    /**
     * Waits for the user to press Enter to return to the menu.
     * @param scanner Scanner for input
     */
    public static void returnMenu(Scanner scanner) {
        System.out.print("\nPress Enter to return...");
        scanner.nextLine();
    }

    /**
     * Prints all edges in the graph.
     * @param graph The graph
     */
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

    /**
     * Prints each segment (edge) in the route.
     * @param route List of edges in the route
     */
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

    /**
     * Prints the path as a sequence of node IDs.
     * @param route List of edges in the route
     */
    private static void printRoutePath(java.util.List<Edge> route) {
        if (route == null || route.isEmpty()) {
            System.out.println("No route found.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(route.get(0).getSource().getId());
        for (Edge edge : route) {
            sb.append(" -> ").append(edge.getTarget().getId());
        }
        System.out.println("Path: " + sb.toString());
    }

    /**
     * Calculates the total base distance of a route.
     * @param route List of edges in the route
     * @return Total base distance
     */
    private static double calcTotalDistance(java.util.List<Edge> route) {
        double total = 0.0;
        if (route != null) {
            for (Edge edge : route) {
                total += edge.getBaseDistance();
            }
        }
        return total;
    }

    /**
     * Loads graph data from a text file.
     * @param graph The graph to populate
     * @param filename File name to read from
     */
    public static void loadGraphFromFile(Graph graph, String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean readingNodes = false, readingEdges = false;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.equalsIgnoreCase("[NODES]")) {
                    readingNodes = true;
                    readingEdges = false;
                    continue;
                }
                if (line.equalsIgnoreCase("[EDGES]")) {
                    readingNodes = false;
                    readingEdges = true;
                    continue;
                }
                if (readingNodes) {
                    // Format: ID,Name,Latitude,Longitude
                    String[] parts = line.split(",");
                    if (parts.length != 4) continue;
                    String id = parts[0].trim();
                    String name = parts[1].trim();
                    double lat = Double.parseDouble(parts[2].trim());
                    double lon = Double.parseDouble(parts[3].trim());
                    graph.addNode(new Node(id, name, lat, lon));
                } else if (readingEdges) {
                    // Format: FromID,ToID,BaseDistance[,TrafficFactor]
                    String[] parts = line.split(",");
                    if (parts.length < 3) continue;
                    String from = parts[0].trim();
                    String to = parts[1].trim();
                    double dist = Double.parseDouble(parts[2].trim());
                    if (parts.length >= 4) {
                        double tf = Double.parseDouble(parts[3].trim());
                        graph.addEdge(from, to, dist, tf);
                    } else {
                        graph.addEdge(from, to, dist);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading map data: " + e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Saves the graph data to a text file.
     * @param graph The graph to save
     * @param filename File name to write to
     */
    public static void saveGraphToFile(Graph graph, String filename) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write("[NODES]\n");
            for (Node node : graph.getAllNodes()) {
                bw.write(String.format("%s,%s,%.4f,%.4f\n",
                    node.getId(), node.getName(), node.getLatitude(), node.getLongitude()));
            }
            bw.write("\n[EDGES]\n");
            for (Edge edge : graph.getAllEdges()) {
                bw.write(String.format("%s,%s,%.1f,%.1f\n",
                    edge.getSource().getId(), edge.getTarget().getId(),
                    edge.getBaseDistance(), edge.getTrafficFactor()));
            }
        } catch (IOException e) {
            System.out.println("Error saving map data: " + e.getMessage());
        }
    }

    /**
     * Main entry point for the GPS Navigation System.
     * @param args Command-line arguments
     */
    public static void main(String[] args) {
        Graph graph = new Graph();

        // Load graph from file
        loadGraphFromFile(graph, "map_data.txt");

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
                    System.out.printf("%-4s %-18s %-12s %-12s\n", "ID", "Name", "Latitude", "Longitude");
                    for (Node node : graph.getAllNodes()) {
                        System.out.printf("%-4s %-18s %-12.4f %-12.4f\n",
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
                            System.out.println();
                            printRoutePath(result.primary.path);

                            if (result.alternative != null) {
                                System.out.println("\nAlternative Route:");
                                System.out.println("Total Weighted Cost: " + String.format("%.2f", result.alternative.totalWeight));
                                System.out.println("Total Distance: " + String.format("%.2f", calcTotalDistance(result.alternative.path)));
                                printRoute(result.alternative.path);
                                System.out.println();
                                printRoutePath(result.alternative.path);
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
                            System.out.println();
                            printRoutePath(result.primary.path);

                            if (result.alternative != null) {
                                System.out.println("\nAlternative Route:");
                                System.out.println("Total Weighted Cost: " + String.format("%.2f", result.alternative.totalWeight));
                                System.out.println("Total Distance: " + String.format("%.2f", calcTotalDistance(result.alternative.path)));
                                printRoute(result.alternative.path);
                                System.out.println();
                                printRoutePath(result.alternative.path);
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
                    try {
                        graph.updateTraffic(edgeId, tf);
                        saveGraphToFile(graph, "map_data.txt");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage());
                        break;
                    }
                    break;
                case 0: // Exit Program
                    scanner.close();
                    return;
            }
            returnMenu(scanner);
        }
    }
}