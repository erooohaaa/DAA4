package org.example.metrics;

public class Metrics {
    private long startTime;
    private long endTime;

    private int dfsVisits;
    private int dfsEdges;

    private int kahnPushes;
    private int kahnPops;

    private int relaxations;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    public void incrementDfsVisits() { dfsVisits++; }
    public void incrementDfsEdges() { dfsEdges++; }
    public void incrementKahnPushes() { kahnPushes++; }
    public void incrementKahnPops() { kahnPops++; }
    public void incrementRelaxations() { relaxations++; }

    public int getDfsVisits() { return dfsVisits; }
    public int getDfsEdges() { return dfsEdges; }
    public int getKahnPushes() { return kahnPushes; }
    public int getKahnPops() { return kahnPops; }
    public int getRelaxations() { return relaxations; }

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