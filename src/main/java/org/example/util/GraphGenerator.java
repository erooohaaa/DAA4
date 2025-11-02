package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GraphGenerator {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        generateAllDatasets();
    }

    public static void generateAllDatasets() throws IOException {
        System.out.println("Starting dataset generation...");

        generateDataset("small_1", 8, 12, 1, 2);
        generateDataset("small_2", 6, 8, 0, 0);
        generateDataset("small_3", 10, 15, 2, 3);

        generateDataset("medium_1", 15, 25, 3, 5);
        generateDataset("medium_2", 12, 18, 1, 2);
        generateDataset("medium_3", 20, 35, 4, 6);

        generateDataset("large_1", 30, 60, 5, 8);
        generateDataset("large_2", 25, 40, 2, 4);
        generateDataset("large_3", 50, 100, 8, 12);

        System.out.println("All datasets generated successfully!");
    }

    private static void generateDataset(String name, int nodes, int edges, int minCycles, int maxCycles) throws IOException {
        File dataDir = new File("data");
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            System.out.println("Created data directory: " + created);
        }

        ObjectNode root = mapper.createObjectNode();
        root.put("directed", true);
        root.put("n", nodes);
        root.put("weight_model", "edge");

        var edgesArray = mapper.createArrayNode();
        Set<String> edgeSet = new HashSet<>();

        int numCycles = random.nextInt(maxCycles - minCycles + 1) + minCycles;
        createCycles(edgesArray, edgeSet, nodes, numCycles);

        while (edgeSet.size() < edges) {
            int u = random.nextInt(nodes);
            int v = random.nextInt(nodes);

            if (u != v) {
                String edgeKey = u + "->" + v;
                if (!edgeSet.contains(edgeKey)) {
                    int weight = random.nextInt(10) + 1;
                    var edgeNode = mapper.createObjectNode();
                    edgeNode.put("u", u);
                    edgeNode.put("v", v);
                    edgeNode.put("w", weight);
                    edgesArray.add(edgeNode);
                    edgeSet.add(edgeKey);
                }
            }
        }

        root.set("edges", edgesArray);
        root.put("source", 0);

        String filename = "data/" + name + ".json";
        File outputFile = new File(filename);
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, root);

        System.out.println("✓ Generated: " + filename + " (" + nodes + " nodes, " + edges + " edges, " + numCycles + " cycles)");
    }

    private static void createCycles(com.fasterxml.jackson.databind.node.ArrayNode edgesArray,
                                     Set<String> edgeSet, int nodes, int numCycles) {
        for (int i = 0; i < numCycles && i < nodes / 3; i++) {
            int cycleSize = random.nextInt(4) + 3;
            List<Integer> cycleNodes = new ArrayList<>();

            while (cycleNodes.size() < cycleSize) {
                int node = random.nextInt(nodes);
                if (!cycleNodes.contains(node)) {
                    cycleNodes.add(node);
                }
            }

            for (int j = 0; j < cycleSize; j++) {
                int u = cycleNodes.get(j);
                int v = cycleNodes.get((j + 1) % cycleSize);
                int weight = random.nextInt(10) + 1;

                var edgeNode = mapper.createObjectNode();
                edgeNode.put("u", u);
                edgeNode.put("v", v);
                edgeNode.put("w", weight);
                edgesArray.add(edgeNode);
                edgeSet.add(u + "->" + v);
            }
        }
    }
}