// Represents a road segment (edge) between two nodes.
// Each edge has a base distance and a traffic factor scale.
// Traffic factor: 1.0 = clear, 1.5 = moderate, 2.5 = jam.
public class Edge {
    private static int nextId = 0; // Simple static counter for edge IDs
    private final int id;
    private final Node source;
    private final Node target;
    private double baseDistance;
    private double trafficFactor;

    // Constructor with source, target, and base distance
    public Edge(Node source, Node target, double baseDistance) {
        this(source, target, baseDistance, 1.0); // Default: clear
    }

    // Constructor with traffic factor
    public Edge(Node source, Node target, double baseDistance, double trafficFactor) {
        if (baseDistance <= 0) throw new IllegalArgumentException("Base distance must be positive.");
        if (trafficFactor <= 0) throw new IllegalArgumentException("Traffic factor must be positive.");
        this.id = nextId++;
        this.source = source;
        this.target = target;
        this.baseDistance = baseDistance;
        this.trafficFactor = trafficFactor;
    }

    public int getId() {
        return id;
    }

    public Node getSource() {
        return source;
    }

    public Node getTarget() {
        return target;
    }

    public double getBaseDistance() {
        return baseDistance;
    }

    public double getTrafficFactor() {
        return trafficFactor;
    }

    // Returns the effective weight (distance × traffic factor)
    public double getWeight() {
        return baseDistance * trafficFactor;
    }

    // Updates the traffic factor for this edge
    public void updateTraffic(double newTrafficFactor) {
        if (newTrafficFactor <= 0) throw new IllegalArgumentException("Traffic factor must be positive.");
        this.trafficFactor = newTrafficFactor;
    }
}