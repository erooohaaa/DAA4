package org.example.topo;

import org.example.model.Graph;
import org.example.metrics.Metrics;
import java.util.*;

/**
 * Implements Kahn's algorithm for topological sorting of DAGs.
 * Time complexity: O(V + E)
 *
 * @author Your Name
 * @version 1.0
 */
public class KahnTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    /**
     * Constructs a topological sorter for the given graph.
     *
     * @param graph the graph to sort (must be a DAG)
     */
    public KahnTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }

    /**
     * Performs topological sort using Kahn's algorithm.
     *
     * @return TopoResult containing the topological order and validation
     */
    public TopoResult topologicalSort() {
        metrics.startTimer();

        int n = graph.getN();
        int[] inDegree = new int[n];

        // Calculate in-degrees for all nodes
        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                inDegree[edge.v]++;
            }
        }

        // Initialize queue with nodes having zero in-degree
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                metrics.incrementKahnPushes();
                queue.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();

        // Process nodes in topological order
        while (!queue.isEmpty()) {
            metrics.incrementKahnPops();
            int u = queue.poll();
            topoOrder.add(u);

            // Decrement in-degree of neighbors
            for (Graph.Edge edge : graph.getEdges(u)) {
                int v = edge.v;
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    metrics.incrementKahnPushes();
                    queue.offer(v);
                }
            }
        }

        metrics.stopTimer();

        // Check if graph is a DAG (all nodes processed)
        boolean isDAG = topoOrder.size() == n;

        return new TopoResult(topoOrder, isDAG, metrics);
    }

    /**
     * Container for topological sort results and metrics.
     */
    public static class TopoResult {
        public final List<Integer> order;
        public final boolean isDAG;
        public final Metrics metrics;

        /**
         * Constructs a topological sort result.
         *
         * @param order the topological order
         * @param isDAG whether the graph is acyclic
         * @param metrics performance metrics
         */
        public TopoResult(List<Integer> order, boolean isDAG, Metrics metrics) {
            this.order = order;
            this.isDAG = isDAG;
            this.metrics = metrics;
        }

        /**
         * Prints the topological sort results.
         */
        public void printResults() {
            if (isDAG) {
                System.out.println("Topological Order: " + order);
            } else {
                System.out.println("Graph contains cycles - no valid topological order");
            }
            System.out.println("Metrics: " + metrics);
        }
    }
}