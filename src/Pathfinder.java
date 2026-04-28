import java.util.*;

/**
 * Provides shortest path and alternative route finding algorithms for the graph.
 */
public class Pathfinder {

    /**
     * Holds the path and total distance.
     */
    public static class PathResult {
        public final List<Edge> path;
        public final double totalWeight;

        public PathResult(List<Edge> path, double totalWeight) {
            this.path = path;
            this.totalWeight = totalWeight;
        }
    }

    /**
     * Holds both the primary and alternative routes.
     */
    public static class AlternativeRoutesResult {
        public final PathResult primary;
        public final PathResult alternative;

        public AlternativeRoutesResult(PathResult primary, PathResult alternative) {
            this.primary = primary;
            this.alternative = alternative;
        }
    }

    /**
     * Finds the shortest path using Dijkstra's algorithm.
     * @param graph The graph
     * @param startId Start node ID
     * @param endId End node ID
     * @return PathResult with path and total weight
     */
    public static PathResult findShortestPathDijkstra(Graph graph, String startId, String endId) {
        Node start = graph.getNode(startId);
        Node end = graph.getNode(endId);

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end node not found.");
        }

        Map<Node, Double> distance = new HashMap<>();
        Map<Node, Edge> previousEdge = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.dist));

        // Initialize distances
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

            // Check all neighbors
            for (Edge edge : graph.getEdges(currNode)) {
                Node neighbor = edge.getTarget();
                if (visited.contains(neighbor)) continue;

                double newDist = distance.get(currNode) + edge.getWeight();

                if (newDist < distance.get(neighbor)) {
                    distance.put(neighbor, newDist);
                    previousEdge.put(neighbor, edge);
                    queue.add(new NodeDistance(neighbor, newDist));
                }
            }
        }

        // Build path by walking predecessors
        if (distance.get(end) == Double.POSITIVE_INFINITY) {
            throw new RuntimeException("End node is unreachable from start node.");
        }
        List<Edge> path = new ArrayList<>();
        Node curr = end;
        while (!curr.equals(start)) {
            Edge edge = previousEdge.get(curr);
            if (edge == null) break;
            path.add(0, edge);
            curr = edge.getSource();
        }
        return new PathResult(path, distance.get(end));
    }

    /**
     * Finds the shortest path using A* algorithm.
     * @param graph The graph
     * @param startId Start node ID
     * @param endId End node ID
     * @return PathResult with path and total weight
     */
    public static PathResult findShortestPathAStar(Graph graph, String startId, String endId) {
        Node start = graph.getNode(startId);
        Node end = graph.getNode(endId);

        if (start == null || end == null) {
            throw new IllegalArgumentException("Start or end node not found.");
        }

        Map<Node, Double> gScore = new HashMap<>();
        Map<Node, Double> fScore = new HashMap<>();
        Map<Node, Edge> previousEdge = new HashMap<>();
        Set<Node> visited = new HashSet<>();
        PriorityQueue<NodeDistance> queue = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.dist));

        // Initialize scores
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

            // Check all neighbors
            for (Edge edge : graph.getEdges(currNode)) {
                Node neighbor = edge.getTarget();
                if (visited.contains(neighbor)) continue;

                double tentativeG = gScore.get(currNode) + edge.getWeight();

                if (tentativeG < gScore.get(neighbor)) {
                    gScore.put(neighbor, tentativeG);
                    double heuristic = haversine(neighbor, end);
                    fScore.put(neighbor, tentativeG + heuristic);
                    previousEdge.put(neighbor, edge);
                    queue.add(new NodeDistance(neighbor, fScore.get(neighbor)));
                }
            }
        }

        if (gScore.get(end) == Double.POSITIVE_INFINITY) {
            throw new RuntimeException("End node is unreachable from start node.");
        }
        List<Edge> path = new ArrayList<>();
        Node curr = end;
        while (!curr.equals(start)) {
            Edge edge = previousEdge.get(curr);
            if (edge == null) break;
            path.add(0, edge);
            curr = edge.getSource();
        }
        return new PathResult(path, gScore.get(end));
    }

    /**
     * Finds both the primary and an alternative route between two nodes.
     * The alternative avoids the edge with the highest traffic factor in the primary path.
     * @param graph The graph
     * @param startId Start node ID
     * @param endId End node ID
     * @param method "dijkstra" or "astar"
     * @return AlternativeRoutesResult with both routes
     */
    public static AlternativeRoutesResult findAlternativeRoutes(
            Graph graph, String startId, String endId, String method) {

        PathResult primary;
        if ("astar".equalsIgnoreCase(method)) {
            primary = findShortestPathAStar(graph, startId, endId);
        } else {
            primary = findShortestPathDijkstra(graph, startId, endId);
        }

        if (primary.path == null || primary.path.isEmpty()) {
            return new AlternativeRoutesResult(primary, null);
        }

        // Find the edge with the highest traffic factor
        Edge criticalEdge = null;
        double maxTraffic = -1;
        for (Edge edge : primary.path) {
            if (edge.getTrafficFactor() > maxTraffic) {
                maxTraffic = edge.getTrafficFactor();
                criticalEdge = edge;
            }
        }

        if (criticalEdge == null) {
            return new AlternativeRoutesResult(primary, null);
        }

        // Temporarily set the critical edge's traffic very high
        double originalTraffic = criticalEdge.getTrafficFactor();
        criticalEdge.updateTraffic(1e6);

        PathResult alternative = null;
        try {
            if ("astar".equalsIgnoreCase(method)) {
                alternative = findShortestPathAStar(graph, startId, endId);
            } else {
                alternative = findShortestPathDijkstra(graph, startId, endId);
            }
            // If still uses the critical edge, no alternative
            if (alternative.path != null && alternative.path.contains(criticalEdge)) {
                alternative = null;
            }
        } catch (RuntimeException e) {
            alternative = null;
        } finally {
            // Restore original traffic factor
            criticalEdge.updateTraffic(originalTraffic);
        }

        return new AlternativeRoutesResult(primary, alternative);
    }

    /**
     * Calculates the Haversine distance between two nodes.
     * @param a First node
     * @param b Second node
     * @return Distance in kilometers
     */
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