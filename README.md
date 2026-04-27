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

2. Run the program:
    ```
    java -cp bin Main
    ```

### Features

- Graph data model using adjacency list
- Dijkstra's and A* pathfinding algorithms
- Performance benchmark table (nanoseconds)
- Simple console-based interface

### Usage

- Enter valid start and end node IDs when prompted (A, B, C, D).
- View shortest path distances and execution time for each algorithm.
