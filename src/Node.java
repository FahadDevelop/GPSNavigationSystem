/**
 * Represents a location in the GPS Navigation system.
 */
public class Node {
    private final String id;       // Unique ID
    private final String name;     // Location name
    private final double latitude; // Latitude
    private final double longitude;// Longitude

    /**
     * Creates a node.
     * @param id Node ID
     * @param name Node name
     * @param latitude Latitude
     * @param longitude Longitude
     */
    public Node(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /** @return Node ID */
    public String getId() { return id; }
    /** @return Node name */
    public String getName() { return name; }
    /** @return Latitude */
    public double getLatitude() { return latitude; }
    /** @return Longitude */
    public double getLongitude() { return longitude; }

    /**
     * Two nodes are equal if their IDs are equal.
     */
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