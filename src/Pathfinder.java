import java.util.*;

// Finds shortest paths in the graph
public class Pathfinder {

    // Finds shortest distance from startId to endId using Dijkstra's Algorithm
    public static double findShortestDistanceDijkstra(Graph graph, String startId, String endId) {
        Node start = graph.getNode(startId);
        Node end = graph.getNode(endId);

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end node not found.");
        }

        Map<Node, Double> distance = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.dist));

        for (Node node : graph.getAllNodes()) {
            distance.put(node, Double.POSITIVE_INFINITY);
        }
        distance.put(start, 0.0);

        queue.add(new NodeDistance(start, 0.0));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            Node currNode = current.node;

            if (!visited.add(currNode)) continue;
            if (currNode.equals(end)) break;

            for (Edge edge : graph.getEdges(currNode)) {
                Node neighbor = edge.getTarget();
                if (visited.contains(neighbor)) continue;

                double newDist = distance.get(currNode) + edge.getWeight();

                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        double result = distance.get(end);
        if (Double.isInfinite(result)) {
            throw new RuntimeException("End node is unreachable from start node.");
        }
        return result;
    }

    // Finds shortest distance from startId to endId using A* Search Algorithm
    public static double findShortestDistanceAStar(Graph graph, String startId, String endId) {
        Node start = graph.getNode(startId);
        Node end = graph.getNode(endId);

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end node not found.");
        }

        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.dist));

        for (Node node : graph.getAllNodes()) {
            gScore.put(node, Double.POSITIVE_INFINITY);
            fScore.put(node, Double.POSITIVE_INFINITY);
        }
        gScore.put(start, 0.0);
        fScore.put(start, haversine(start, end));

        queue.add(new NodeDistance(start, fScore.get(start)));

        while (!queue.isEmpty()) {
            NodeDistance current = queue.poll();
            Node currNode = current.node;

            if (!visited.add(currNode)) continue;
            if (currNode.equals(end)) break;

            for (Edge edge : graph.getEdges(currNode)) {
                Node neighbor = edge.getTarget();
                if (visited.contains(neighbor)) continue;

                double tentativeG = gScore.get(currNode) + edge.getWeight();

                if (tentativeG < gScore.get(neighbor)) {
                    gScore.put(neighbor, tentativeG);
                    double heuristic = haversine(neighbor, end);
                    fScore.put(neighbor, tentativeG + heuristic);
                    queue.add(new NodeDistance(neighbor, fScore.get(neighbor)));
                }
            }
        }

        double result = gScore.get(end);
        if (Double.isInfinite(result)) {
            throw new RuntimeException("End node is unreachable from start node.");
        }
        return result;
    }

    // Haversine distance formula (in kilometers)
    private static double haversine(Node a, Node b) {
        final int R = 6371; // Earth radius in kilometers
        double lat1 = Math.toRadians(a.getLatitude());
        double lon1 = Math.toRadians(a.getLongitude());
        double lat2 = Math.toRadians(b.getLatitude());
        double lon2 = Math.toRadians(b.getLongitude());
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double h = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.pow(Math.sin(dLon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(h), Math.sqrt(1 - h));

        return R * c;
    }

    // Helper class for priority queue
    private static class NodeDistance {
        Node node;
        double dist;
        NodeDistance(Node node, double dist) {
            this.node = node;
            this.dist = dist;
        }
    }
}