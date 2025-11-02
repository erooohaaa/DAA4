package org.example;

import org.example.model.Graph;
import org.example.model.GraphJsonParser;
import org.example.scc.KosarajuSCC;
import org.example.topo.KahnTopologicalSort;
import org.example.dagsp.DAGShortestLongestPaths;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GraphAlgorithmsTest {

    @Test
    public void testSCC() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_1.json");
        KosarajuSCC sccFinder = new KosarajuSCC(graph);
        KosarajuSCC.SCCResult result = sccFinder.findSCCs();

        assertFalse(result.components.isEmpty());
        assertTrue(result.components.size() >= 1);
    }

    @Test
    public void testTopologicalSort() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_2.json");
        KahnTopologicalSort topoSort = new KahnTopologicalSort(graph);
        KahnTopologicalSort.TopoResult result = topoSort.topologicalSort();

        assertTrue(result.isDAG);
    }

    @Test
    public void testShortestPath() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_2.json");
        DAGShortestLongestPaths pathFinder = new DAGShortestLongestPaths(graph);

        DAGShortestLongestPaths.ShortestPathResult result = pathFinder.shortestPath(0);

        assertTrue(result.isValid);
    }

    @Test
    public void testCriticalPath() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_2.json");
        DAGShortestLongestPaths pathFinder = new DAGShortestLongestPaths(graph);

        DAGShortestLongestPaths.CriticalPathResult result = pathFinder.findCriticalPath();

        assertNotNull(result);
        assertFalse(result.path.isEmpty());
    }
}