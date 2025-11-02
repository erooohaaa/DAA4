package org.example;

import org.example.model.Graph;
import org.example.model.GraphJsonParser;
import org.example.scc.KosarajuSCC;
import org.example.topo.CondensationGraph;
import org.example.topo.KahnTopologicalSort;
import org.example.dagsp.DAGShortestLongestPaths;
import java.io.File;

public class Main {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("No file specified. Running analysis on ALL datasets...\n");
            runAllDatasets();
            return;
        }


        try {
            runSingleAnalysis(args[0]);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void runAllDatasets() {
        File dataDir = new File("data");
        File[] files = dataDir.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null && files.length > 0) {
            for (File file : files) {
                System.out.println("\n" + "=".repeat(70));
                System.out.println("ANALYZING: " + file.getName());
                System.out.println("=".repeat(70));

                try {
                    runSingleAnalysis("data/" + file.getName());
                } catch (Exception e) {
                    System.err.println("Error processing " + file.getName() + ": " + e.getMessage());
                }


                System.out.println("\n" + "─".repeat(70));
            }
            System.out.println("\nCompleted analysis of " + files.length + " datasets");
        } else {
            System.out.println("No JSON files found in data/ directory");
            System.out.println("Usage: java -jar DAA4.jar <input_json_file>");
            System.out.println("Or run without arguments to analyze all datasets in data/");
        }
    }

    private static void runSingleAnalysis(String filePath) throws Exception {
        Graph graph = GraphJsonParser.parse(filePath);
        System.out.println("Graph loaded: " + graph.getN() + " nodes, " + graph.getEdgeCount() + " edges");

        System.out.println("\n=== Strongly Connected Components ===");
        KosarajuSCC sccFinder = new KosarajuSCC(graph);
        KosarajuSCC.SCCResult sccResult = sccFinder.findSCCs();
        sccResult.printResults();

        System.out.println("\n=== Condensation Graph ===");
        CondensationGraph condensation = new CondensationGraph(graph, sccResult);
        condensation.printCondensationInfo();

        System.out.println("\n=== Topological Sort ===");
        KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation.getCondensationGraph());
        KahnTopologicalSort.TopoResult topoResult = topoSort.topologicalSort();
        topoResult.printResults();

        System.out.println("\n=== Shortest and Longest Paths on Condensation DAG ===");
        DAGShortestLongestPaths pathFinder = new DAGShortestLongestPaths(condensation.getCondensationGraph());
        int source = 0;

        DAGShortestLongestPaths.ShortestPathResult shortestResult = pathFinder.shortestPath(source);
        if (shortestResult.isValid) {
            System.out.println("Shortest distances from source component " + source + ":");
            for (int i = 0; i < shortestResult.dist.length; i++) {
                if (shortestResult.dist[i] != Integer.MAX_VALUE) {
                    System.out.println("  to component " + i + ": " + shortestResult.dist[i] +
                            " (nodes: " + condensation.getComponentNodes(i) + ")");
                }
            }
            System.out.println("Shortest path metrics: " + shortestResult.metrics);
        } else {
            System.out.println("Cannot compute shortest paths on condensation graph");
        }

        DAGShortestLongestPaths.CriticalPathResult criticalResult = pathFinder.findCriticalPath();
        criticalResult.printResults();
    }
}