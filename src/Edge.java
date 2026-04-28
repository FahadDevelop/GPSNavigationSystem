/**
 * Represents a road segment (edge) between two nodes.
 * Each edge has a base distance and a traffic factor.
 */
public class Edge {
    private static int nextId = 0; // Counter for edge IDs
    private final int id;
    private final Node source;
    private final Node target;
    private double baseDistance;
    private double trafficFactor;

    /**
     * Creates an edge with default traffic factor (1.0).
     * @param source Source node
     * @param target Target node
     * @param baseDistance Distance between nodes
     */
    public Edge(Node source, Node target, double baseDistance) {
        this(source, target, baseDistance, 1.0);
    }

    /**
     * Creates an edge with specified traffic factor.
     * @param source Source node
     * @param target Target node
     * @param baseDistance Distance between nodes
     * @param trafficFactor Traffic multiplier
     */
    public Edge(Node source, Node target, double baseDistance, double trafficFactor) {
        if (baseDistance <= 0) throw new IllegalArgumentException("Base distance must be positive.");
        if (trafficFactor <= 0) throw new IllegalArgumentException("Traffic factor must be positive.");
        this.id = nextId++;
        this.source = source;
        this.target = target;
        this.baseDistance = baseDistance;
        this.trafficFactor = trafficFactor;
    }

    /** @return Edge ID */
    public int getId() {
        return id;
    }

    /** @return Source node */
    public Node getSource() {
        return source;
    }

    /** @return Target node */
    public Node getTarget() {
        return target;
    }

    /** @return Base distance */
    public double getBaseDistance() {
        return baseDistance;
    }

    /** @return Traffic factor */
    public double getTrafficFactor() {
        return trafficFactor;
    }

    /**
     * Gets the effective weight (distance × traffic factor).
     * @return Weighted distance
     */
    public double getWeight() {
        return baseDistance * trafficFactor;
    }

    /**
     * Updates the traffic factor for this edge.
     * @param newTrafficFactor New traffic multiplier
     */
    public void updateTraffic(double newTrafficFactor) {
        if (newTrafficFactor <= 0) throw new IllegalArgumentException("Traffic factor must be positive.");
        this.trafficFactor = newTrafficFactor;
    }
}