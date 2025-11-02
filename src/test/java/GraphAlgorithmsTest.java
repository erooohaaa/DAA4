package org.example;

import org.example.model.Graph;
import org.example.model.GraphJsonParser;
import org.example.scc.KosarajuSCC;
import org.example.topo.CondensationGraph;
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
    public void testCondensationGraph() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_1.json");
        KosarajuSCC sccFinder = new KosarajuSCC(graph);
        KosarajuSCC.SCCResult sccResult = sccFinder.findSCCs();

        CondensationGraph condensation = new CondensationGraph(graph, sccResult);
        Graph condGraph = condensation.getCondensationGraph();

        assertTrue(condGraph.getN() <= graph.getN());
    }

    @Test
    public void testTopologicalSortOnCondensation() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_1.json");
        KosarajuSCC sccFinder = new KosarajuSCC(graph);
        KosarajuSCC.SCCResult sccResult = sccFinder.findSCCs();

        CondensationGraph condensation = new CondensationGraph(graph, sccResult);
        KahnTopologicalSort topoSort = new KahnTopologicalSort(condensation.getCondensationGraph());
        KahnTopologicalSort.TopoResult result = topoSort.topologicalSort();

        assertTrue(result.isDAG);
    }

    @Test
    public void testShortestPathOnCondensation() throws Exception {
        Graph graph = GraphJsonParser.parse("data/small_1.json");
        KosarajuSCC sccFinder = new KosarajuSCC(graph);
        KosarajuSCC.SCCResult sccResult = sccFinder.findSCCs();

        CondensationGraph condensation = new CondensationGraph(graph, sccResult);
        DAGShortestLongestPaths pathFinder = new DAGShortestLongestPaths(condensation.getCondensationGraph());

        DAGShortestLongestPaths.ShortestPathResult result = pathFinder.shortestPath(0);

        assertTrue(result.isValid);
    }
}