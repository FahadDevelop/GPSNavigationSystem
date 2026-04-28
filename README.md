# GPS Navigation System

A console-based Java application for finding shortest and alternative routes on a custom road network using Dijkstra's and A* algorithms.

---

## Compilation Guide

**Prerequisites:**
- [JDK 8 or higher](https://adoptopenjdk.net/)
- Command line or terminal access

**Steps to Compile & Run:**

1. **Compile all Java source files:**
    ```sh
    javac -d bin src/*.java
    ```

2. **Create a manifest file** (provided as `manifest.txt`):
    ```
    Main-Class: Main
    ```

3. **Package everything into a JAR file:**
    ```sh
    jar cfm GPSNavigationSystem.jar manifest.txt -C bin .
    ```

4. **Ensure `map_data.txt` is present in the program's working directory.**

5. **Run the program:**
    ```sh
    java -jar GPSNavigationSystem.jar
    ```

---

## Program Usage

When run, the program will display a menu:

```
GPS Navigation System

[1] List All Nodes
[2] List All Edges
[3] Find Shortest Route (Dijkstra)
[4] Find Shortest Route (A-Star)
[5] Update Edge Traffic
[0] Exit Program
```

**Options:**

- **[1] List All Nodes:**  
  Displays all nodes (locations) in the loaded map.

- **[2] List All Edges:**  
  Lists all road segments with details: ID, start/end nodes, base distance, traffic factor, and weighted cost.

- **[3] Find Shortest Route (Dijkstra):**  
  Prompts for a Start and End node ID, then finds and displays the lowest-cost path, including an alternative route (if available).  
  - Shows total weighted cost and total base distance for both routes.
  - Prints the sequence of nodes and the individual segments (edges).

- **[4] Find Shortest Route (A-Star):**  
  Same as above, but uses the A* algorithm (with geodesic heuristic).

- **[5] Update Edge Traffic:**  
  Lists all edges. Prompts for the edge ID and a new traffic factor, updates the edge in `map_data.txt`.

- **[0] Exit Program:**  
  Closes the application.

**Other Notes:**
- Node IDs and Edge IDs are shown in the lists—use these when prompted.
- Paths and segments are printed with base and weighted distances to help you interpret results.

---

## `map_data.txt` Format

This file contains the map data (nodes and roads) used by the program.  
**Structure:**

- **Sections:**  
  - `[NODES]` — List of locations (nodes)
  - `[EDGES]` — List of road segments (edges) between nodes

- **Node Format (CSV):**
    ```
    ID,Name,Latitude,Longitude
    ```
    - Example:
        ```
        CP,Central Park,12.9600,77.6400
        MS,Main Square,12.9610,77.6410
        ```

- **Edge Format (CSV):**
    ```
    FromID,ToID,BaseDistance,TrafficFactor
    ```
    - `BaseDistance` is a positive number (e.g., in kilometers).
    - `TrafficFactor` is a multiplier representing current traffic; must be > 0.  
      - (1.0 = normal, >1.0 = slower/heavier traffic)
    - Example (bidirectional edge between CP and MS):
        ```
        CP,MS,0.5,1.0
        MS,CP,0.5,1.0
        ```

- **Example File:**
    ```
    [NODES]
    CP,Central Park,12.9600,77.6400
    MS,Main Square,12.9610,77.6410
    LB,Library,12.9620,77.6420

    [EDGES]
    CP,MS,0.5,1.0
    MS,CP,0.5,1.0
    MS,LB,0.5,1.2
    LB,MS,0.5,1.2
    ```

**Important:**  
- The `[NODES]` section must come before `[EDGES]`.  
- Node IDs in edges must match IDs defined in the node list.  
- You can add or modify nodes/edges using any text editor, or via the program’s update traffic function.

---

## Feature Highlights

- Fast shortest path search via Dijkstra & A* (heuristic: Haversine distance)
- Detects alternative routes (avoiding most congested edge)
- Update and persist road traffic factors
- Menu-driven console interface
- Easily modifiable map data

---

## Troubleshooting

- Make sure `map_data.txt` is in the same directory as the program/JAR.
- Edge or node ID not found? Double-check your map file or use "List All Nodes/Edges".
- The program will save updated traffic values back to `map_data.txt` after changes.

---
