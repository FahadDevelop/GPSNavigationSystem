# GPS Navigation System

## Compilation Guide

### Prerequisites
- [JDK 8 or higher](https://adoptopenjdk.net/)
- Command line or terminal

### How to Compile

1. Compile all Java files:
    ```
    javac -d bin src/*.java
    ```

2. Package everything into a JAR:
    ```
    jar cfm GPSNavigationSystem.jar manifest.txt -C bin .
    ```

3. Run the JAR file:
    ```
    java -jar GPSNavigationSystem.jar
    ```

### Features

- Graph data model using adjacency list
- Dijkstra's and A* pathfinding algorithms
- Performance benchmark in nanoseconds
- Simple console-based interface

### Usage

- Enter valid start and end node IDs when prompted.
- View shortest path distances, alternate route path and execution time for the given algorithm.
