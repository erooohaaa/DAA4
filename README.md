# Smart City Scheduling - Graph Algorithms Report

## Data Summary

| Dataset | Nodes | Edges | Type | SCCs | Largest SCC | Critical Path Length |
|---------|-------|-------|------|------|-------------|---------------------|
| small_1 | 8 | 12 | Cyclic | 5 | 4 | 9 |
| small_2 | 6 | 8 | DAG | 6 | 1 | 13 |
| small_3 | 10 | 15 | Cyclic | 2 | 9 | 0 |
| medium_1 | 15 | 27 | Cyclic | 4 | 12 | 11 |
| medium_2 | 12 | 18 | Cyclic | 4 | 9 | 15 |
| medium_3 | 20 | 36 | Cyclic | 5 | 16 | 16 |
| large_1 | 30 | 61 | Cyclic | 5 | 26 | 10 |
| large_2 | 25 | 40 | Cyclic | 7 | 19 | 19 |
| large_3 | 50 | 101 | Cyclic | 13 | 38 | 22 |

**Weight Model:** Edge-based weights (1-10)

## Performance Results

### SCC Algorithm (Kosaraju)

| Dataset | Time (ns) | DFS Visits | DFS Edges |
|---------|-----------|------------|-----------|
| small_1 | 61,400 | 16 | 24 |
| large_3 | 420,800 | 100 | 202 |

### Topological Sort (Kahn)

| Dataset | Time (ns) | Kahn Pushes | Kahn Pops |
|---------|-----------|-------------|-----------|
| small_1 | 23,900 | 5 | 5 |
| large_3 | 61,800 | 13 | 13 |

### Shortest Paths

| Dataset | Time (ns) | Relaxations |
|---------|-----------|-------------|
| small_1 | 38,400 | 3 |
| large_3 | 44,400 | 10 |

## Analysis

### SCC Performance
- **Time Complexity**: O(V+E) confirmed
- **Bottleneck**: DFS traversal of large components
- **Effect of Structure**: More cycles → more components

### Topological Sort
- **Efficiency**: Linear complexity on condensed graph
- **Bottleneck**: In-degree calculation for large graphs

### DAG Shortest Paths
- **Performance**: O(V+E) with topological ordering
- **Bottleneck**: Edge relaxations in dense graphs

## Conclusions

1. **SCC is essential** for graphs with cyclic dependencies
2. **Condensation** significantly simplifies planning
3. **Topological sort** works only on DAGs
4. **Critical Path** determines minimum execution time

## Practical Recommendations

- For city planning, always start with SCC analysis
- Use condensed graph for route optimization
- Critical Path helps identify scheduling bottlenecks
- Apply topological sorting after SCC compression
- Path algorithms should operate on condensed DAG

## Build & Run

```bash
# Compile project
mvn clean compile

# Generate datasets
mvn exec:java -Dexec.mainClass="org.example.util.GraphGenerator"

# Run analysis on all datasets
mvn exec:java -Dexec.mainClass="org.example.Main"

# Run on specific dataset
mvn exec:java -Dexec.mainClass="org.example.Main" -Dexec.args="data/small_1.json"

# Run tests
mvn test

# Build package
mvn clean package
```
Project Structure
text
DAA4/
├── data/               # 9 generated datasets
├── src/
│   ├── main/java/org/example/
│   │   ├── model/      # Graph data structures
│   │   ├── scc/        # Kosaraju SCC implementation
│   │   ├── topo/       # Condensation & topological sort
│   │   ├── dagsp/      # Shortest/longest paths in DAG
│   │   ├── metrics/    # Performance metrics
│   │   └── util/       # Dataset generator
│   └── test/java/      # JUnit tests
├── pom.xml
└── README.md
Algorithms Implemented:

Strongly Connected Components (Kosaraju)

Graph Condensation

Topological Sort (Kahn)

Shortest/Longest Paths in DAG

Critical Path Analysis
