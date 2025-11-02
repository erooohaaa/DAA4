package org.example.topo;

import org.example.model.Graph;
import org.example.scc.KosarajuSCC;
import java.util.*;

/**
 * Builds a condensation graph from strongly connected components.
 * Each SCC becomes a single node in the condensation DAG.
 *
 * @author Your Name
 * @version 1.0
 */
public class CondensationGraph {
    private final Graph originalGraph;
    private final KosarajuSCC.SCCResult sccResult;
    private Graph condensationGraph;
    private final int[] nodeToComponent;
    private final List<List<Integer>> components;

    /**
     * Constructs a condensation graph from SCC results.
     *
     * @param graph the original graph
     * @param sccResult the SCC analysis results
     */
    public CondensationGraph(Graph graph, KosarajuSCC.SCCResult sccResult) {
        this.originalGraph = graph;
        this.sccResult = sccResult;
        this.nodeToComponent = new int[graph.getN()];
        this.components = sccResult.components;
        buildComponentMapping();
        buildCondensationGraph();
    }

    /**
     * Maps each original node to its component ID.
     */
    private void buildComponentMapping() {
        int compId = 0;
        for (List<Integer> component : components) {
            for (int node : component) {
                nodeToComponent[node] = compId;
            }
            compId++;
        }
    }

    /**
     * Builds the condensation graph by connecting components.
     */
    private void buildCondensationGraph() {
        int numComponents = components.size();
        condensationGraph = new Graph(numComponents, true, originalGraph.getWeightModel());

        Set<String> addedEdges = new HashSet<>();

        // Create edges between different components
        for (int u = 0; u < originalGraph.getN(); u++) {
            int compU = nodeToComponent[u];

            for (Graph.Edge edge : originalGraph.getEdges(u)) {
                int v = edge.v;
                int compV = nodeToComponent[v];

                // Add edge only if components are different
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

    /**
     * @return the condensation DAG
     */
    public Graph getCondensationGraph() {
        return condensationGraph;
    }

    /**
     * @param node the original node identifier
     * @return the component ID containing the node
     */
    public int getComponent(int node) {
        return nodeToComponent[node];
    }

    /**
     * @param compId the component identifier
     * @return list of nodes in the specified component
     */
    public List<Integer> getComponentNodes(int compId) {
        return components.get(compId);
    }

    /**
     * Prints condensation graph statistics.
     */
    public void printCondensationInfo() {
        System.out.println("Original nodes: " + originalGraph.getN());
        System.out.println("Condensation nodes: " + condensationGraph.getN());

        int edges = 0;
        for (int i = 0; i < condensationGraph.getN(); i++) {
            edges += condensationGraph.getEdges(i).size();
        }
        System.out.println("Condensation edges: " + edges);

        List<Integer> sizes = new ArrayList<>();
        for (List<Integer> component : components) {
            sizes.add(component.size());
        }
        System.out.println("Component sizes: " + sizes);
    }
}