package org.example.scc;

import org.example.model.Graph;
import org.example.metrics.Metrics;
import java.util.*;

/**
 * Implements Kosaraju's algorithm for finding strongly connected components.
 * Time complexity: O(V + E)
 * Space complexity: O(V)
 *
 * @author Your Name
 * @version 1.0
 */
public class KosarajuSCC {
    private final Graph graph;
    private final boolean[] visited;
    private final Stack<Integer> stack;
    private final List<List<Integer>> components;
    private final int[] componentId;
    private final Metrics metrics;

    /**
     * Constructs an SCC finder for the given graph.
     *
     * @param graph the graph to analyze
     */
    public KosarajuSCC(Graph graph) {
        this.graph = graph;
        this.visited = new boolean[graph.getN()];
        this.stack = new Stack<>();
        this.components = new ArrayList<>();
        this.componentId = new int[graph.getN()];
        this.metrics = new Metrics();
        Arrays.fill(componentId, -1);
    }

    /**
     * Finds all strongly connected components using Kosaraju's algorithm.
     *
     * @return SCCResult containing components and performance metrics
     */
    public SCCResult findSCCs() {
        metrics.startTimer();

        // First DFS pass: fill stack with finishing times
        for (int i = 0; i < graph.getN(); i++) {
            if (!visited[i]) {
                dfsFirstPass(i);
            }
        }

        // Create reversed graph for second pass
        Graph reversedGraph = reverseGraph();

        // Second DFS pass: process in reverse order to find SCCs
        Arrays.fill(visited, false);
        int currentComponent = 0;

        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited[node]) {
                List<Integer> component = new ArrayList<>();
                dfsSecondPass(reversedGraph, node, component, currentComponent);
                components.add(component);
                currentComponent++;
            }
        }

        metrics.stopTimer();
        return new SCCResult(components, metrics);
    }

    /**
     * First DFS pass: visits nodes and pushes to stack in post-order.
     *
     * @param node the starting node for DFS
     */
    private void dfsFirstPass(int node) {
        metrics.incrementDfsVisits();
        visited[node] = true;

        for (Graph.Edge edge : graph.getEdges(node)) {
            metrics.incrementDfsEdges();
            if (!visited[edge.v]) {
                dfsFirstPass(edge.v);
            }
        }

        stack.push(node);
    }

    /**
     * Second DFS pass: finds SCCs in the reversed graph.
     *
     * @param reversedGraph the reversed graph
     * @param node the current node
     * @param component the current component being built
     * @param compId the component identifier
     */
    private void dfsSecondPass(Graph reversedGraph, int node, List<Integer> component, int compId) {
        metrics.incrementDfsVisits();
        visited[node] = true;
        component.add(node);
        componentId[node] = compId;

        for (Graph.Edge edge : reversedGraph.getEdges(node)) {
            metrics.incrementDfsEdges();
            if (!visited[edge.v]) {
                dfsSecondPass(reversedGraph, edge.v, component, compId);
            }
        }
    }

    /**
     * Creates the reversed graph by inverting all edges.
     *
     * @return the reversed graph
     */
    private Graph reverseGraph() {
        Graph reversed = new Graph(graph.getN(), graph.isDirected(), graph.getWeightModel());

        for (int u = 0; u < graph.getN(); u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                reversed.addEdge(edge.v, edge.u, edge.weight);
            }
        }

        return reversed;
    }

    /**
     * Container for SCC algorithm results and metrics.
     */
    public static class SCCResult {
        public final List<List<Integer>> components;
        public final Metrics metrics;

        /**
         * Constructs an SCC result.
         *
         * @param components the list of strongly connected components
         * @param metrics performance metrics
         */
        public SCCResult(List<List<Integer>> components, Metrics metrics) {
            this.components = components;
            this.metrics = metrics;
        }

        /**
         * Prints the SCC results in human-readable format.
         */
        public void printResults() {
            System.out.println("=== Strongly Connected Components ===");
            for (int i = 0; i < components.size(); i++) {
                System.out.println("Component " + i + ": " + components.get(i) +
                        " (Size: " + components.get(i).size() + ")");
            }
            System.out.println("Total components: " + components.size());
            System.out.println("Metrics: " + metrics);
        }

        /**
         * @return list of component sizes
         */
        public List<Integer> getComponentSizes() {
            List<Integer> sizes = new ArrayList<>();
            for (List<Integer> component : components) {
                sizes.add(component.size());
            }
            return sizes;
        }
    }
}