// Represents a road (edge) between two locations with a weight.
public class Edge {
    private final Node source;   // Starting location
    private final Node target;   // Destination location
    private final double weight; // Weight (e.g., distance)

    // Constructor to create an edge
    public Edge(Node source, Node target, double weight) {
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    public Node getSource() { return source; }
    public Node getTarget() { return target; }
    public double getWeight() { return weight; }
}