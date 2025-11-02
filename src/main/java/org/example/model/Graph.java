package org.example.model;

import java.util.*;

/**
 * Represents a graph model for city service task dependencies.
 * Supports directed and undirected graphs with edge-based weights.
 *
 * @author Your Name
 * @version 1.0
 */
public class Graph {
    private final int n;
    private final List<List<Edge>> adjList;
    private final boolean directed;
    private final String weightModel;
    private final Map<Integer, Integer> nodeDurations;
    private int source;

    /**
     * Constructs a new graph with specified parameters.
     *
     * @param n the number of nodes in the graph
     * @param directed whether the graph is directed
     * @param weightModel the weight model ("edge" or "node")
     */
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

    /**
     * Adds a directed edge with weight between two nodes.
     * For undirected graphs, adds reverse edge automatically.
     *
     * @param u the source node
     * @param v the target node
     * @param w the edge weight
     */
    public void addEdge(int u, int v, int w) {
        adjList.get(u).add(new Edge(u, v, w));
        if (!directed) {
            adjList.get(v).add(new Edge(v, u, w));
        }
    }

    /**
     * Sets the duration for a node (used with "node" weight model).
     *
     * @param node the node identifier
     * @param duration the node duration
     */
    public void setNodeDuration(int node, int duration) {
        nodeDurations.put(node, duration);
    }

    /**
     * Sets the source node for path algorithms.
     *
     * @param source the source node identifier
     */
    public void setSource(int source) {
        this.source = source;
    }

    // Getters with Javadoc
    /**
     * @return the number of nodes in the graph
     */
    public int getN() { return n; }

    /**
     * @param u the node identifier
     * @return list of edges from the specified node
     */
    public List<Edge> getEdges(int u) { return adjList.get(u); }

    /**
     * @return true if the graph is directed
     */
    public boolean isDirected() { return directed; }

    /**
     * @return the weight model type
     */
    public String getWeightModel() { return weightModel; }

    /**
     * @param node the node identifier
     * @return the duration of the specified node
     */
    public Integer getNodeDuration(int node) { return nodeDurations.get(node); }

    /**
     * @return the source node for path algorithms
     */
    public int getSource() { return source; }

    /**
     * @return the adjacency list representation of the graph
     */
    public List<List<Edge>> getAdjList() { return adjList; }

    /**
     * Calculates the total number of edges in the graph.
     *
     * @return the edge count
     */
    public int getEdgeCount() {
        int count = 0;
        for (List<Edge> edges : adjList) {
            count += edges.size();
        }
        return directed ? count : count / 2;
    }

    /**
     * Represents a directed edge in the graph with weight.
     */
    public static class Edge {
        public final int u, v, weight;

        /**
         * Constructs a new edge.
         *
         * @param u the source node
         * @param v the target node
         * @param weight the edge weight
         */
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