package sprint2;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmesGrapheTest extends AbstractIGrapheTest {

    @Override
    protected IGraphe creerGrapheVide() {
        return new Sprint2Graph();
    }

    private IEntite pkgChenille, pkgIhm, pkgAnneaux;
    private IEntite clAppli, clFrame, clChenille, clAnneau, clTete;
    private IEntite intIAnneau;

    private void setUpExample() {
        // Create the specific graph from the provided PlantUML diagram

        // Packages (estType = false)
        pkgChenille = new EntiteMock("chenille", false);
        pkgIhm = new EntiteMock("ihm", false);
        pkgAnneaux = new EntiteMock("anneaux", false);

        // Classes/Interfaces (estType = true)
        clAppli = new EntiteMock("Appli", true);
        clFrame = new EntiteMock("Frame", true);
        clChenille = new EntiteMock("Chenille", true);
        intIAnneau = new EntiteMock("IAnneau", true);
        clAnneau = new EntiteMock("Anneau", true);
        clTete = new EntiteMock("Tete", true);

        // Add entities
        graph.ajouterEntite(pkgChenille);
        graph.ajouterEntite(pkgIhm);
        graph.ajouterEntite(pkgAnneaux);
        graph.ajouterEntite(clAppli);
        graph.ajouterEntite(clFrame);
        graph.ajouterEntite(clChenille);
        graph.ajouterEntite(intIAnneau);
        graph.ajouterEntite(clAnneau);
        graph.ajouterEntite(clTete);

        // Containment relations (CONTIENT)
        graph.ajouterRelation(pkgChenille, pkgIhm, NatureRelation.CONTIENT);
        graph.ajouterRelation(pkgChenille, pkgAnneaux, NatureRelation.CONTIENT);
        graph.ajouterRelation(pkgChenille, clChenille, NatureRelation.CONTIENT);
        graph.ajouterRelation(pkgChenille, intIAnneau, NatureRelation.CONTIENT);

        graph.ajouterRelation(pkgIhm, clAppli, NatureRelation.CONTIENT);
        graph.ajouterRelation(pkgIhm, clFrame, NatureRelation.CONTIENT);

        graph.ajouterRelation(pkgAnneaux, clAnneau, NatureRelation.CONTIENT);
        graph.ajouterRelation(pkgAnneaux, clTete, NatureRelation.CONTIENT);

        // Other relations
        graph.ajouterRelation(clFrame, clAppli, NatureRelation.SOUS_TYPE_DE);
        graph.ajouterRelation(clAppli, clAnneau, NatureRelation.CREE);
        graph.ajouterRelation(clAppli, clTete, NatureRelation.CREE);
        graph.ajouterRelation(clAppli, clChenille, NatureRelation.CREE);

        graph.ajouterRelation(clChenille, intIAnneau, NatureRelation.AGREGE);
        graph.ajouterRelation(clAnneau, intIAnneau, NatureRelation.SOUS_TYPE_DE);
        graph.ajouterRelation(clTete, clAnneau, NatureRelation.SOUS_TYPE_DE);
    }

    @Test
    void testDependantsDirects() {
        setUpExample();
        Set<IEntite> directs = AlgorithmesGraphe.dependantsDirects(graph, clAnneau);

        assertTrue(directs.contains(clAppli));
        assertTrue(directs.contains(clTete));
        assertEquals(2, directs.size());
    }

    @Test
    void testDependantsElargis() {
        setUpExample();
        Set<IEntite> elargis = AlgorithmesGraphe.dependantsElargis(graph, clAnneau);

        // Direct dependents
        assertTrue(elargis.contains(clAppli));
        assertTrue(elargis.contains(clTete));

        // Containment up to first package
        assertTrue(elargis.contains(pkgIhm)); // Contains Appli
        assertTrue(elargis.contains(pkgAnneaux)); // Contains Tete

        // Should NOT contain the higher level package 'chenille'
        assertFalse(elargis.contains(pkgChenille));

        assertEquals(4, elargis.size(), "Should contain exactly Appli, Tete, ihm, and anneaux");
    }
}
