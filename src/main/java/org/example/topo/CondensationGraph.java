package org.example.topo;

import org.example.model.Graph;
import org.example.scc.KosarajuSCC;
import java.util.*;

public class CondensationGraph {
    private final Graph originalGraph;
    private final KosarajuSCC.SCCResult sccResult;
    private Graph condensationGraph;
    private final int[] nodeToComponent;

    public CondensationGraph(Graph graph, KosarajuSCC.SCCResult sccResult) {
        this.originalGraph = graph;
        this.sccResult = sccResult;
        this.nodeToComponent = new int[graph.getN()];
        buildComponentMapping();
        buildCondensationGraph();
    }

    private void buildComponentMapping() {
        int compId = 0;
        for (List<Integer> component : sccResult.components) {
            for (int node : component) {
                nodeToComponent[node] = compId;
            }
            compId++;
        }
    }

    private void buildCondensationGraph() {
        int numComponents = sccResult.components.size();
        condensationGraph = new Graph(numComponents, true, originalGraph.getWeightModel());

        Set<String> addedEdges = new HashSet<>();

        for (int u = 0; u < originalGraph.getN(); u++) {
            int compU = nodeToComponent[u];

            for (Graph.Edge edge : originalGraph.getEdges(u)) {
                int v = edge.v;
                int compV = nodeToComponent[v];

                if (compU != compV) {
                    String edgeKey = compU + "->" + compV;
                    if (!addedEdges.contains(edgeKey)) {
                        condensationGraph.addEdge(compU, compV, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
    }

    public Graph getCondensationGraph() {
        return condensationGraph;
    }

    public int getComponent(int node) {
        return nodeToComponent[node];
    }

    public List<Integer> getComponentNodes(int compId) {
        return sccResult.components.get(compId);
    }

    public void printCondensationInfo() {
        System.out.println("Original nodes: " + originalGraph.getN());
        System.out.println("Condensation nodes: " + condensationGraph.getN());

        int edges = 0;
        for (int i = 0; i < condensationGraph.getN(); i++) {
            edges += condensationGraph.getEdges(i).size();
        }
        System.out.println("Condensation edges: " + edges);
    }
}