package sprint1;

import java.util.*;

/**
 * A directed graph representation using an Adjacency List.
 * Vertices are represented by Strings.
 * Edges have String labels.
 */
public class AdjacencyListGraph {

    // Maps a vertex to a map of adjacent vertices and their edge labels.
    private final Map<String, Map<String, String>> adjacencyList;

    public AdjacencyListGraph() {
        this.adjacencyList = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the name of the vertex to add
     * @return true if the vertex was added, false if it already existed
     */
    public boolean addVertex(String vertex) {
        if (vertex == null || adjacencyList.containsKey(vertex)) {
            return false;
        }
        adjacencyList.put(vertex, new HashMap<>());
        return true;
    }

    /**
     * Adds a directed edge between two vertices with an optional label.
     * Both vertices must exist in the graph.
     *
     * @param source the source vertex
     * @param target the target vertex
     * @param label  the label of the edge (e.g., "create", "depends_on")
     * @return true if the edge was added successfully, false if vertices don't exist or edge already exists
     */
    public boolean addEdge(String source, String target, String label) {
        if (!adjacencyList.containsKey(source) || !adjacencyList.containsKey(target)) {
            return false; // Both vertices must exist
        }

        Map<String, String> edges = adjacencyList.get(source);
        if (edges.containsKey(target)) {
            return false; // Edge already exists. Overwriting is not supported in this simple implementation.
        }

        edges.put(target, label);
        return true;
    }

    /**
     * Gets all vertices in the graph.
     *
     * @return a set of all vertex names
     */
    public Set<String> getVertices() {
        return Collections.unmodifiableSet(adjacencyList.keySet());
    }

    /**
     * Retrieves the label of an edge between source and target.
     *
     * @param source the source vertex
     * @param target the target vertex
     * @return the edge label, or null if no such edge exists
     */
    public String getEdgeLabel(String source, String target) {
        if (adjacencyList.containsKey(source)) {
            return adjacencyList.get(source).get(target);
        }
        return null;
    }

    /**
     * Retrieves all outgoing edges from a given vertex.
     *
     * @param vertex the source vertex
     * @return a map of target vertices to their edge labels
     */
    public Map<String, String> getOutgoingEdges(String vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap(adjacencyList.get(vertex));
    }
}
