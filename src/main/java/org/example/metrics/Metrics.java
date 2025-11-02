package org.example.metrics;

/**
 * Collects performance metrics for graph algorithms.
 * Tracks execution time and operation counts for analysis.
 *
 * @author Marat Yerkanat
 * @version 1.0
 */
public class Metrics {
    private long startTime;
    private long endTime;

    // SCC algorithm counters
    private int dfsVisits;
    private int dfsEdges;

    // Topological sort counters
    private int kahnPushes;
    private int kahnPops;

    // Shortest path counters
    private int relaxations;

    /**
     * Starts the performance timer.
     */
    public void startTimer() {
        startTime = System.nanoTime();
    }

    /**
     * Stops the performance timer.
     */
    public void stopTimer() {
        endTime = System.nanoTime();
    }

    /**
     * @return elapsed time in nanoseconds
     */
    public long getElapsedTime() {
        return endTime - startTime;
    }

    // Counter increment methods
    /**
     * Increments DFS node visit counter.
     */
    public void incrementDfsVisits() { dfsVisits++; }

    /**
     * Increments DFS edge traversal counter.
     */
    public void incrementDfsEdges() { dfsEdges++; }

    /**
     * Increments Kahn's algorithm queue push counter.
     */
    public void incrementKahnPushes() { kahnPushes++; }

    /**
     * Increments Kahn's algorithm queue pop counter.
     */
    public void incrementKahnPops() { kahnPops++; }

    /**
     * Increments edge relaxation counter for path algorithms.
     */
    public void incrementRelaxations() { relaxations++; }

    // Getter methods
    /**
     * @return number of DFS node visits
     */
    public int getDfsVisits() { return dfsVisits; }

    /**
     * @return number of DFS edge traversals
     */
    public int getDfsEdges() { return dfsEdges; }

    /**
     * @return number of Kahn's algorithm pushes
     */
    public int getKahnPushes() { return kahnPushes; }

    /**
     * @return number of Kahn's algorithm pops
     */
    public int getKahnPops() { return kahnPops; }

    /**
     * @return number of edge relaxations
     */
    public int getRelaxations() { return relaxations; }

    /**
     * Resets all counters to zero.
     */
    public void reset() {
        dfsVisits = dfsEdges = kahnPushes = kahnPops = relaxations = 0;
    }

    @Override
    public String toString() {
        return String.format(
                "Time: %d ns, DFS Visits: %d, DFS Edges: %d, Kahn Pushes: %d, Kahn Pops: %d, Relaxations: %d",
                getElapsedTime(), dfsVisits, dfsEdges, kahnPushes, kahnPops, relaxations
        );
    }
}