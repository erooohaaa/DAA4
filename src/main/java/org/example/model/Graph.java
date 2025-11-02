package org.example.model;

import java.util.*;

public class Graph {
    private final int n;
    private final List<List<Edge>> adjList;
    private final boolean directed;
    private final String weightModel;
    private final Map<Integer, Integer> nodeDurations;
    private int source;

    public Graph(int n, boolean directed, String weightModel) {
        this.n = n;
        this.directed = directed;
        this.weightModel = weightModel;
        this.adjList = new ArrayList<>();
        this.nodeDurations = new HashMap<>();

        for (int i = 0; i < n; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int w) {
        adjList.get(u).add(new Edge(u, v, w));
        if (!directed) {
            adjList.get(v).add(new Edge(v, u, w));
        }
    }

    public void setNodeDuration(int node, int duration) {
        nodeDurations.put(node, duration);
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getN() { return n; }
    public List<Edge> getEdges(int u) { return adjList.get(u); }
    public boolean isDirected() { return directed; }
    public String getWeightModel() { return weightModel; }
    public Integer getNodeDuration(int node) { return nodeDurations.get(node); }
    public int getSource() { return source; }
    public List<List<Edge>> getAdjList() { return adjList; }

    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjList) {
            count += edges.size();
        }
        return directed ? count : count / 2;
    }

    public static class Edge {
        public final int u, v, weight;

        public Edge(int u, int v, int weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return u + "->" + v + "(" + weight + ")";
        }
    }
}