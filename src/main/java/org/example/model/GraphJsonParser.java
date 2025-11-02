package org.example.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 * Parser for JSON graph files in the specified task format.
 * Handles both edge-based and node-based weight models.
 *
 * @author Your Name
 * @version 1.0
 */
public class GraphJsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Parses a graph from a JSON file.
     *
     * @param filePath the path to the JSON file
     * @return a Graph object representing the parsed data
     * @throws IOException if file reading or parsing fails
     */
    public static Graph parse(String filePath) throws IOException {
        JsonNode root = mapper.readTree(new File(filePath));
        return parseJsonNode(root);
    }

    /**
     * Internal method to parse JSON node into Graph object.
     *
     * @param root the root JSON node
     * @return constructed Graph object
     */
    private static Graph parseJsonNode(JsonNode root) {
        boolean directed = root.get("directed").asBoolean(true);
        int n = root.get("n").asInt();
        String weightModel = root.has("weight_model") ?
                root.get("weight_model").asText() : "edge";

        Graph graph = new Graph(n, directed, weightModel);

        // Parse edges array
        JsonNode edges = root.get("edges");
        for (JsonNode edge : edges) {
            int u = edge.get("u").asInt();
            int v = edge.get("v").asInt();
            int w = edge.get("w").asInt();
            graph.addEdge(u, v, w);
        }

        // Parse node durations if using node weight model
        if ("node".equals(weightModel) && root.has("node_durations")) {
            JsonNode durations = root.get("node_durations");
            var fields = durations.fields();
            while (fields.hasNext()) {
                var entry = fields.next();
                int node = Integer.parseInt(entry.getKey());
                int duration = entry.getValue().asInt();
                graph.setNodeDuration(node, duration);
            }
        }

        // Parse source node if specified
        if (root.has("source")) {
            graph.setSource(root.get("source").asInt());
        }

        return graph;
    }
}