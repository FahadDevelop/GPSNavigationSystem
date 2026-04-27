import java.util.*;

// Represents the road network using an adjacency list
public class Graph {
    private final Map<String, Node> nodesById;
    private final Map<Node, List<Edge>> adjacencyList;
    private final Map<Integer, Edge> edgeById; // Map edge IDs for fast lookup

    public Graph() {
        nodesById = new HashMap<>();
        adjacencyList = new HashMap<>();
        edgeById = new HashMap<>();
    }

    // Add a node to the graph
    public void addNode(Node node) {
        nodesById.put(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Add a directed edge between two nodes
    public void addEdge(String sourceId, String targetId, double baseDistance) {
        addEdge(sourceId, targetId, baseDistance, 1.0); // Default: trafficFactor = 1.0 (clear)
    }

    // Overload to allow initial trafficFactor
    public void addEdge(String sourceId, String targetId, double baseDistance, double trafficFactor) {
        Node source = nodesById.get(sourceId);
        Node target = nodesById.get(targetId);
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target node not found.");
        }
        Edge edge = new Edge(source, target, baseDistance, trafficFactor);
        adjacencyList.get(source).add(edge);
        edgeById.put(edge.getId(), edge);
    }

    // Update the traffic factor of an edge by edge ID
    public void updateTraffic(int edgeId, double newTrafficFactor) {
        Edge edge = edgeById.get(edgeId);
        if (edge == null) {
            throw new IllegalArgumentException("Edge with ID " + edgeId + " not found.");
        }
        edge.updateTraffic(newTrafficFactor);
    }

    // Get a node by its ID
    public Node getNode(String id) {
        return nodesById.get(id);
    }

    // Get outgoing edges from a node
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    // Get all nodes in the graph
    public Collection<Node> getAllNodes() {
        return nodesById.values();
    }

    // Get all edges in the graph
    public Collection<Edge> getAllEdges() {
        return edgeById.values();
    }

    // Optionally, get edge by ID
    public Edge getEdge(int edgeId) {
        return edgeById.get(edgeId);
    }
}