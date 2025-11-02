package org.example.dagsp;

import org.example.model.Graph;
import org.example.topo.KahnTopologicalSort;
import org.example.metrics.Metrics;
import java.util.*;

public class DAGShortestLongestPaths {
    private final Graph graph;
    private final Metrics metrics;

    public DAGShortestLongestPaths(Graph graph) {
        this.graph = graph;
        this.metrics = new Metrics();
    }

    public ShortestPathResult shortestPath(int source) {
        metrics.startTimer();

        int n = graph.getN();
        int[] dist = new int[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        var topoResult = topoSort.topologicalSort();

        if (!topoResult.isDAG) {
            metrics.stopTimer();
            return new ShortestPathResult(dist, prev, false, metrics);
        }

        for (int u : topoResult.order) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Graph.Edge edge : graph.getEdges(u)) {
                    metrics.incrementRelaxations();
                    int newDist = dist[u] + edge.weight;
                    if (newDist < dist[edge.v]) {
                        dist[edge.v] = newDist;
                        prev[edge.v] = u;
                    }
                }
            }
        }

        metrics.stopTimer();
        return new ShortestPathResult(dist, prev, true, metrics);
    }

    public LongestPathResult longestPath(int source) {
        metrics.startTimer();

        int n = graph.getN();
        int[] dist = new int[n];
        int[] prev = new int[n];

        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(prev, -1);
        dist[source] = 0;

        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        var topoResult = topoSort.topologicalSort();

        if (!topoResult.isDAG) {
            metrics.stopTimer();
            return new LongestPathResult(dist, prev, false, metrics);
        }

        for (int u : topoResult.order) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Graph.Edge edge : graph.getEdges(u)) {
                    metrics.incrementRelaxations();
                    int newDist = dist[u] + edge.weight;
                    if (newDist > dist[edge.v]) {
                        dist[edge.v] = newDist;
                        prev[edge.v] = u;
                    }
                }
            }
        }

        metrics.stopTimer();
        return new LongestPathResult(dist, prev, true, metrics);
    }

    public CriticalPathResult findCriticalPath() {
        int maxLength = Integer.MIN_VALUE;
        List<Integer> criticalPath = new ArrayList<>();
        int bestSource = -1;
        int bestTarget = -1;

        for (int source = 0; source < graph.getN(); source++) {
            LongestPathResult result = longestPath(source);
            if (result.isValid) {
                for (int target = 0; target < result.dist.length; target++) {
                    if (result.dist[target] > maxLength && result.dist[target] != Integer.MIN_VALUE) {
                        maxLength = result.dist[target];
                        bestSource = source;
                        bestTarget = target;
                        criticalPath = reconstructPath(result.prev, target);
                    }
                }
            }
        }

        if (maxLength == Integer.MIN_VALUE && graph.getN() > 0) {
            maxLength = 0;
            criticalPath = List.of(0);
            bestSource = 0;
        }

        return new CriticalPathResult(criticalPath, maxLength, bestSource);
    }

    private List<Integer> reconstructPath(int[] prev, int target) {
        List<Integer> path = new ArrayList<>();
        if (prev[target] == -1) {
            path.add(target);
            return path;
        }

        for (int at = target; at != -1; at = prev[at]) {
            path.add(at);
        }
        Collections.reverse(path);
        return path;
    }

    public static class ShortestPathResult {
        public final int[] dist;
        public final int[] prev;
        public final boolean isValid;
        public final Metrics metrics;

        public ShortestPathResult(int[] dist, int[] prev, boolean isValid, Metrics metrics) {
            this.dist = dist;
            this.prev = prev;
            this.isValid = isValid;
            this.metrics = metrics;
        }
    }

    public static class LongestPathResult {
        public final int[] dist;
        public final int[] prev;
        public final boolean isValid;
        public final Metrics metrics;

        public LongestPathResult(int[] dist, int[] prev, boolean isValid, Metrics metrics) {
            this.dist = dist;
            this.prev = prev;
            this.isValid = isValid;
            this.metrics = metrics;
        }
    }

    public static class CriticalPathResult {
        public final List<Integer> path;
        public final int length;
        public final int source;

        public CriticalPathResult(List<Integer> path, int length, int source) {
            this.path = path;
            this.length = length;
            this.source = source;
        }

        public void printResults() {
            if (path.isEmpty() || length == Integer.MIN_VALUE) {
                System.out.println("No critical path found");
            } else {
                System.out.println("Critical Path (Longest): " + path);
                System.out.println("Critical Path Length: " + length);
                System.out.println("Starting from component: " + source);
            }
        }
    }
}