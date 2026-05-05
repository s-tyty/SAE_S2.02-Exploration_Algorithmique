package sprint1;

import java.util.*;

/**
 * A directed graph representation using an Adjacency Matrix.
 * Vertices are represented by Strings.
 * Edges have String labels.
 */
public class AdjacencyMatrixGraph {

    // To map String vertices to matrix indices
    private final List<String> indexToVertex;
    private final Map<String, Integer> vertexToIndex;

    // The matrix storing the edge labels. Null means no edge.
    private String[][] matrix;
    private int capacity;
    private int size;

    public AdjacencyMatrixGraph() {
        this.capacity = 10;
        this.size = 0;
        this.indexToVertex = new ArrayList<>();
        this.vertexToIndex = new HashMap<>();
        this.matrix = new String[capacity][capacity];
    }

    /**
     * Adds a vertex to the graph.
     *
     * @param vertex the name of the vertex to add
     * @return true if the vertex was added, false if it already existed
     */
    public boolean addVertex(String vertex) {
        if (vertex == null || vertexToIndex.containsKey(vertex)) {
            return false;
        }

        if (size == capacity) {
            expandMatrix();
        }

        vertexToIndex.put(vertex, size);
        indexToVertex.add(vertex);
        size++;
        return true;
    }

    private void expandMatrix() {
        capacity *= 2;
        String[][] newMatrix = new String[capacity][capacity];
        for (int i = 0; i < size; i++) {
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, size);
        }
        matrix = newMatrix;
    }

    /**
     * Adds a directed edge between two vertices with an optional label.
     *
     * @param source the source vertex
     * @param target the target vertex
     * @param label  the label of the edge
     * @return true if the edge was added successfully, false if vertices don't exist or edge already exists
     */
    public boolean addEdge(String source, String target, String label) {
        Integer sourceIdx = vertexToIndex.get(source);
        Integer targetIdx = vertexToIndex.get(target);

        if (sourceIdx == null || targetIdx == null) {
            return false;
        }

        if (matrix[sourceIdx][targetIdx] != null) {
            return false; // Edge already exists
        }

        matrix[sourceIdx][targetIdx] = label;
        return true;
    }

    /**
     * Gets all vertices in the graph.
     *
     * @return a set of all vertex names
     */
    public Set<String> getVertices() {
        return Collections.unmodifiableSet(vertexToIndex.keySet());
    }

    /**
     * Retrieves the label of an edge between source and target.
     *
     * @param source the source vertex
     * @param target the target vertex
     * @return the edge label, or null if no such edge exists
     */
    public String getEdgeLabel(String source, String target) {
        Integer sourceIdx = vertexToIndex.get(source);
        Integer targetIdx = vertexToIndex.get(target);

        if (sourceIdx == null || targetIdx == null) {
            return null;
        }

        return matrix[sourceIdx][targetIdx];
    }
}
