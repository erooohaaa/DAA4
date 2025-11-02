package org.example.scc;

import org.example.model.Graph;
import org.example.metrics.Metrics;
import java.util.*;

public class KosarajuSCC {
    private final Graph graph;
    private final boolean[] visited;
    private final Stack<Integer> stack;
    private final List<List<Integer>> components;
    private final int[] componentId;
    private final Metrics metrics;

    public KosarajuSCC(Graph graph) {
        this.graph = graph;
        this.visited = new boolean[graph.getN()];
        this.stack = new Stack<>();
        this.components = new ArrayList<>();
        this.componentId = new int[graph.getN()];
        this.metrics = new Metrics();
        Arrays.fill(componentId, -1);
    }

    public SCCResult findSCCs() {
        metrics.startTimer();

        for (int i = 0; i < graph.getN(); i++) {
            if (!visited[i]) {
                dfsFirstPass(i);
            }
        }

        Graph reversedGraph = reverseGraph();

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

    private Graph reverseGraph() {
        Graph reversed = new Graph(graph.getN(), graph.isDirected(), graph.getWeightModel());

        for (int u = 0; u < graph.getN(); u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                reversed.addEdge(edge.v, edge.u, edge.weight);
            }
        }

        return reversed;
    }

    public static class SCCResult {
        public final List<List<Integer>> components;
        public final Metrics metrics;

        public SCCResult(List<List<Integer>> components, Metrics metrics) {
            this.components = components;
            this.metrics = metrics;
        }

        public void printResults() {
            System.out.println("=== Strongly Connected Components ===");
            for (int i = 0; i < components.size(); i++) {
                System.out.println("Component " + i + ": " + components.get(i) +
                        " (Size: " + components.get(i).size() + ")");
            }
            System.out.println("Total components: " + components.size());
            System.out.println("Metrics: " + metrics);
        }

        public List<Integer> getComponentSizes() {
            List<Integer> sizes = new ArrayList<>();
            for (List<Integer> component : components) {
                sizes.add(component.size());
            }
            return sizes;
        }
    }
}