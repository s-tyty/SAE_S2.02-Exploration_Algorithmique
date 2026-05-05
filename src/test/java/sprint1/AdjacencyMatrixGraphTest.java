package sprint1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    private AdjacencyMatrixGraph graph;

    @BeforeEach
    void setUp() {
        graph = new AdjacencyMatrixGraph();
    }

    @Test
    void testAddVertex_Success() {
        assertTrue(graph.addVertex("V1"));
        assertTrue(graph.addVertex("V2"));

        Set<String> vertices = graph.getVertices();
        assertEquals(2, vertices.size());
        assertTrue(vertices.contains("V1"));
        assertTrue(vertices.contains("V2"));
    }

    @Test
    void testAddVertex_DuplicateFails() {
        assertTrue(graph.addVertex("V1"));
        assertFalse(graph.addVertex("V1"), "Adding a duplicate vertex should return false");
        assertEquals(1, graph.getVertices().size());
    }

    @Test
    void testAddVertex_ExpandCapacity() {
        // Exceed default capacity of 10
        for (int i = 0; i < 15; i++) {
            assertTrue(graph.addVertex("V" + i));
        }
        assertEquals(15, graph.getVertices().size());

        // Test edge after expansion
        assertTrue(graph.addEdge("V0", "V14", "link"));
        assertEquals("link", graph.getEdgeLabel("V0", "V14"));
    }

    @Test
    void testAddEdge_Success() {
        graph.addVertex("V1");
        graph.addVertex("V2");

        assertTrue(graph.addEdge("V1", "V2", "create"));
        assertEquals("create", graph.getEdgeLabel("V1", "V2"));
    }

    @Test
    void testAddEdge_MissingVertexFails() {
        graph.addVertex("V1");

        // Target missing
        assertFalse(graph.addEdge("V1", "V2", "create"));
    }

    @Test
    void testGetEdgeLabel_NonExistent() {
        graph.addVertex("V1");
        graph.addVertex("V2");
        assertNull(graph.getEdgeLabel("V1", "V2"));
        assertNull(graph.getEdgeLabel("V3", "V1"));
    }
}
