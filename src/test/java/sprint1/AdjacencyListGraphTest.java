package sprint1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    private AdjacencyListGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyListGraph();
    }

    @Test
    void testAddVertex_Success() {
        assertTrue(graph.addVertex("A"));
        assertTrue(graph.addVertex("B"));

        Set<String> vertices = graph.getVertices();
        assertEquals(2, vertices.size());
        assertTrue(vertices.contains("A"));
        assertTrue(vertices.contains("B"));
    }

    @Test
    void testAddVertex_DuplicateFails() {
        assertTrue(graph.addVertex("A"));
        assertFalse(graph.addVertex("A"), "Adding a duplicate vertex should return false");
        assertEquals(1, graph.getVertices().size());
    }

    @Test
    void testAddEdge_Success() {
        graph.addVertex("A");
        graph.addVertex("B");

        assertTrue(graph.addEdge("A", "B", "depends_on"));
        assertEquals("depends_on", graph.getEdgeLabel("A", "B"));
    }

    @Test
    void testAddEdge_MissingVertexFails() {
        graph.addVertex("A");

        // Target missing
        assertFalse(graph.addEdge("A", "B", "depends_on"));
        // Source missing
        assertFalse(graph.addEdge("C", "A", "depends_on"));
    }

    @Test
    void testGetOutgoingEdges() {
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addVertex("C");

        graph.addEdge("A", "B", "create");
        graph.addEdge("A", "C", "depends_on");

        Map<String, String> edges = graph.getOutgoingEdges("A");
        assertEquals(2, edges.size());
        assertEquals("create", edges.get("B"));
        assertEquals("depends_on", edges.get("C"));
    }

    @Test
    void testGetEdgeLabel_NonExistent() {
        graph.addVertex("A");
        graph.addVertex("B");
        assertNull(graph.getEdgeLabel("A", "B"));
        assertNull(graph.getEdgeLabel("C", "A"));
    }
}
