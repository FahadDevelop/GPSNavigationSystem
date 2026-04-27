import java.util.*;

// Finds shortest paths in the graph
public class Pathfinder {

    // Finds shortest distance from startId to endId using Dijkstra's Algorithm
    public static double findShortestDistance(Graph graph, String startId, String endId) {
        Node start = graph.getNode(startId);
        Node end = graph.getNode(endId);

        // Check if nodes exist
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end node not found.");
        }

        // Distance from start to each node
        Map<Node, Double> distance = new HashMap<>();
        // Track visited nodes
        Set<Node> visited = new HashSet<>();
        // Min-heap priority queue: node and distance
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.dist));

        // Set initial distances to infinity, except start node
        for (Node node : graph.getAllNodes()) {
            distance.put(node, Double.POSITIVE_INFINITY);
        }
        distance.put(start, 0.0);

        // Add start node to queue
        queue.add(new NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            Node currNode = current.node;

            // Skip if already visited
            if (!visited.add(currNode)) continue;

            // Stop if end node is reached
            if (currNode.equals(end)) break;

            // Check all neighbors
            for (Edge edge : graph.getEdges(currNode)) {
                Node neighbor = edge.getTarget();
                if (visited.contains(neighbor)) continue;

                double newDist = distance.get(currNode) + edge.getWeight();

                // Update distance if shorter
                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        double result = distance.get(end);
        // If unreachable, return -1
        if (Double.isInfinite(result)) {
            throw new RuntimeException("End node is unreachable from start node.");
        }
        return result;
    }

    // Helper class for PriorityQueue
    private static class NodeDistance {
        Node node;
        double dist;
        NodeDistance(Node node, double dist) {
            this.node = node;
            this.dist = dist;
        }
    }
}