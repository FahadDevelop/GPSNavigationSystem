import java.util.*;

/**
 * Represents the road network using an adjacency list.
 */
public class Graph {
    private final Map<String, Node> nodesById;      // Node lookup by ID
    private final Map<Node, List<Edge>> adjacencyList; // Outgoing edges for each node
    private final Map<Integer, Edge> edgeById;      // Edge lookup by ID

    /** Creates an empty graph. */
    public Graph() {
        nodesById = new HashMap<>();
        adjacencyList = new HashMap<>();
        edgeById = new HashMap<>();
    }

    /**
     * Adds a node to the graph.
     * @param node Node to add
     */
    public void addNode(Node node) {
        nodesById.put(node.getId(), node);
        adjacencyList.putIfAbsent(node, new ArrayList<>());
    }

    /**
     * Adds a directed edge with default traffic factor.
     * @param sourceId Source node ID
     * @param targetId Target node ID
     * @param baseDistance Distance
     */
    public void addEdge(String sourceId, String targetId, double baseDistance) {
        addEdge(sourceId, targetId, baseDistance, 1.0);
    }

    /**
     * Adds a directed edge with specified traffic factor.
     * @param sourceId Source node ID
     * @param targetId Target node ID
     * @param baseDistance Distance
     * @param trafficFactor Traffic multiplier
     */
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

    /**
     * Updates the traffic factor of an edge by edge ID.
     * @param edgeId Edge ID
     * @param newTrafficFactor New traffic multiplier
     */
    public void updateTraffic(int edgeId, double newTrafficFactor) {
        Edge edge = edgeById.get(edgeId);
        if (edge == null) {
            throw new IllegalArgumentException("Edge with ID " + edgeId + " not found.");
        }
        edge.updateTraffic(newTrafficFactor);
    }

    /**
     * Gets a node by its ID.
     * @param id Node ID
     * @return Node or null
     */
    public Node getNode(String id) {
        return nodesById.get(id);
    }

    /**
     * Gets outgoing edges from a node.
     * @param node Node
     * @return List of edges
     */
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, Collections.emptyList());
    }

    /** @return All nodes in the graph */
    public Collection<Node> getAllNodes() {
        return nodesById.values();
    }

    /** @return All edges in the graph */
    public Collection<Edge> getAllEdges() {
        return edgeById.values();
    }

    /**
     * Gets an edge by its ID.
     * @param edgeId Edge ID
     * @return Edge or null
     */
    public Edge getEdge(int edgeId) {
        return edgeById.get(edgeId);
    }
}