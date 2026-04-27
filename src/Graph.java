import java.util.*;

// Represents the road network using an adjacency list
public class Graph {
    private final Map<String, Node> nodesById;
    private final Map<Node, List<Edge>> adjacencyList;

    public Graph() {
        nodesById = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    // Add a node to the graph
    public void addNode(Node node) {
        nodesById.put(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    // Add a directed edge between two nodes
    public void addEdge(String sourceId, String targetId, double weight) {
        Node source = nodesById.get(sourceId);
        Node target = nodesById.get(targetId);
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target node not found.");
        }
        adjacencyList.get(source).add(new Edge(source, target, weight));
    }

    // Update road weight between two nodes
    public void updateRoadWeight(String sourceId, String targetId, double newWeight) {
        Node source = nodesById.get(sourceId);
        Node target = nodesById.get(targetId);
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source or target node not found.");
        }
        List<Edge> edges = adjacencyList.get(source);
        boolean edgeFound = false;
        for (Edge edge : edges) {
            if (edge.getTarget().equals(target)) {
                // Remove old edge and add updated one (Edge is immutable)
                edges.remove(edge);
                edges.add(new Edge(source, target, newWeight));
                edgeFound = true;
                break;
            }
        }
        if (!edgeFound) {
            throw new RuntimeException("Edge not found between nodes.");
        }
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
}