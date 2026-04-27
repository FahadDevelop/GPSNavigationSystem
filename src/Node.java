// Represents a location in the GPS Navigation system.
public class Node {
    private final String id;       // Unique ID of the location
    private final String name;     // Name of the location
    private final double latitude; // Latitude coordinate
    private final double longitude;// Longitude coordinate

    // Constructor to create a node
    public Node(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // Two nodes are equal if their IDs are equal
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Node)) return false;
        Node other = (Node) obj;
        return this.id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}