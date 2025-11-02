package org.example.topo;

import org.example.model.Graph;
import org.example.metrics.Metrics;
import java.util.*;

public class KahnTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;

    public KahnTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }

    public TopoResult topologicalSort() {
        metrics.startTimer();

        int n = graph.getN();
        int[] inDegree = new int[n];

        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getEdges(u)) {
                inDegree[edge.v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                metrics.incrementKahnPushes();
                queue.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();

        while (!queue.isEmpty()) {
            metrics.incrementKahnPops();
            int u = queue.poll();
            topoOrder.add(u);

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

        boolean isDAG = topoOrder.size() == n;

        return new TopoResult(topoOrder, isDAG, metrics);
    }

    public static class TopoResult {
        public final List<Integer> order;
        public final boolean isDAG;
        public final Metrics metrics;

        public TopoResult(List<Integer> order, boolean isDAG, Metrics metrics) {
            this.order = order;
            this.isDAG = isDAG;
            this.metrics = metrics;
        }

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