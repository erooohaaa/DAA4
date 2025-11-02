package org.example;

import org.example.model.Graph;
import org.example.model.GraphJsonParser;
import org.example.scc.KosarajuSCC;
import org.example.topo.CondensationGraph;
import org.example.topo.KahnTopologicalSort;
import org.example.dagsp.DAGShortestLongestPaths;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java -jar DAA4.jar <input_json_file>");
            return;
        }

        try {
            Graph graph = GraphJsonParser.parse(args[0]);
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

            System.out.println("\n=== Shortest and Longest Paths ===");
            DAGShortestLongestPaths pathFinder = new DAGShortestLongestPaths(graph);
            int source = graph.getSource();

            DAGShortestLongestPaths.ShortestPathResult shortestResult = pathFinder.shortestPath(source);
            if (shortestResult.isValid) {
                System.out.println("Shortest distances from source " + source + ":");
                for (int i = 0; i < shortestResult.dist.length; i++) {
                    if (shortestResult.dist[i] != Integer.MAX_VALUE) {
                        System.out.println("  to " + i + ": " + shortestResult.dist[i]);
                    }
                }
            }

            DAGShortestLongestPaths.CriticalPathResult criticalResult = pathFinder.findCriticalPath();
            criticalResult.printResults();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}