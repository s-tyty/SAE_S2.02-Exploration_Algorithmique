package sprint2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractIGrapheTest {

    protected IGraphe graph;

    protected abstract IGraphe creerGrapheVide();

    @BeforeEach
    void setUp() {
        graph = creerGrapheVide();
    }

    // A simple mock for IEntite used in tests
    protected static class EntiteMock implements IEntite {
        private final String nom;
        private final boolean estType;

        public EntiteMock(String nom, boolean estType) {
            this.nom = nom;
            this.estType = estType;
        }

        @Override
        public String nom() {
            return nom;
        }

        @Override
        public boolean estType() {
            return estType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EntiteMock that = (EntiteMock) o;
            return nom.equals(that.nom);
        }

        @Override
        public int hashCode() {
            return nom.hashCode();
        }
    }

    @Test
    void testAjouterEntite_Success() {
        IEntite e1 = new EntiteMock("package", false);
        IEntite e2 = new EntiteMock("ClasseA", true);

        assertTrue(graph.ajouterEntite(e1));
        assertTrue(graph.ajouterEntite(e2));

        Set<IEntite> entities = graph.entites();
        assertEquals(2, entities.size());
        assertTrue(entities.contains(e1));
        assertTrue(entities.contains(e2));
    }

    @Test
    void testAjouterEntite_DuplicateFails() {
        IEntite e1 = new EntiteMock("ClasseA", true);
        IEntite e2 = new EntiteMock("ClasseA", true); // Same name

        assertTrue(graph.ajouterEntite(e1));
        assertFalse(graph.ajouterEntite(e2));
        assertEquals(1, graph.entites().size());
    }

    @Test
    void testAjouterRelation_Success() {
        IEntite src = new EntiteMock("Source", true);
        IEntite tgt = new EntiteMock("Target", true);
        graph.ajouterEntite(src);
        graph.ajouterEntite(tgt);

        assertTrue(graph.ajouterRelation(src, tgt, NatureRelation.DEPEND_DE));

        Set<RelationSortante> out = graph.relationsSortantes(src);
        assertEquals(1, out.size());
        assertEquals(tgt, out.iterator().next().cible());
        assertEquals(NatureRelation.DEPEND_DE, out.iterator().next().nature());

        Set<RelationEntrante> in = graph.relationsEntrantes(tgt);
        assertEquals(1, in.size());
        assertEquals(src, in.iterator().next().source());
        assertEquals(NatureRelation.DEPEND_DE, in.iterator().next().nature());
    }

    @Test
    void testAjouterRelation_MissingEntityFails() {
        IEntite src = new EntiteMock("Source", true);
        IEntite tgt = new EntiteMock("Target", true);

        graph.ajouterEntite(src);
        // tgt is missing
        assertFalse(graph.ajouterRelation(src, tgt, NatureRelation.CREE));
    }
}
