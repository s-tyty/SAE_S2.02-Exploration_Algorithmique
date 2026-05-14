package graphe.outils;

import graphe.impl.Graphe;
import graphe.modele.IGraphe;
import graphe.modele.IEntite;
import graphe.modele.NatureRelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImporteurPlantUmlTest {

    private IGraphe graphe;

    @BeforeEach
    void setUp() {
        String diagramme = """
            @startuml
            package "chenille" #business {
               interface IAnneau
               interface IAnneauMobile
               class Chenille

               Chenille "1" o- "*" IAnneauMobile
               IAnneauMobile <|-- anneaux.Anneau
               IAnneau <|-- IAnneauMobile
               Chenille .[#red]> anneaux.Anneau : <color:red><<create>></color>
               Chenille .[#red]> anneaux.Tete : <color:red><<create>></color>

               package anneaux {
                 class Anneau
                 class Tete
                 Anneau <|-- Tete
               }

               package ihm {
                  class Appli
                  class Frame
                  Appli .> Chenille : <<create>>
                  Appli o-- Frame
                  Appli .> IAnneau
               }
            }
            @enduml
            """;

        graphe = new Graphe();
        ImporteurPlantUml.charger(diagramme, graphe);
    }

    @Test
    void doit_creer_10_entites() {
        assertEquals(10, graphe.entites().size());
    }

    @Test
    void doit_contenir_les_entites_attendues() {
        Set<String> attendues = Set.of(
                "chenille",
                "chenille.Chenille",
                "chenille.IAnneau",
                "chenille.IAnneauMobile",
                "chenille.anneaux",
                "chenille.anneaux.Anneau",
                "chenille.anneaux.Tete",
                "chenille.ihm",
                "chenille.ihm.Appli",
                "chenille.ihm.Frame"
        );

        Set<String> obtenues = graphe.entites().stream()
                .map(IEntite::nom)
                .collect(Collectors.toSet());

        assertEquals(attendues, obtenues);
    }

    @Test
    void chenille_a_les_bonnes_relations_sortantes() {
        IEntite chenille = trouverEntite("chenille.Chenille");

        Set<String> obtenues = graphe.relationsSortantes(chenille).stream()
                .map(r -> r.nature() + "->" + r.cible().nom())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> attendues = Set.of(
                "AGREGE->chenille.IAnneauMobile",
                "CREE->chenille.anneaux.Anneau",
                "CREE->chenille.anneaux.Tete"
        );

        assertEquals(attendues, obtenues);
    }

    @Test
    void anneau_a_les_bonnes_relations_entrantes() {
        IEntite anneau = trouverEntite("chenille.anneaux.Anneau");

        Set<String> obtenues = graphe.relationsEntrantes(anneau).stream()
                .filter(r -> r.nature().estDependanceStatique())
                .map(r -> r.source().nom() + "->" + r.nature())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> attendues = Set.of(
                "chenille.Chenille->CREE",
                "chenille.anneaux.Tete->SOUS_TYPE_DE"
        );

        assertEquals(attendues, obtenues);
    }

    @Test
    void anneau_a_aussi_une_relation_de_contenance_entrante() {
        IEntite anneau = trouverEntite("chenille.anneaux.Anneau");

        Set<String> obtenues = graphe.relationsEntrantes(anneau).stream()
                .filter(r -> r.nature() == NatureRelation.CONTIENT)
                .map(r -> r.source().nom() + "->" + r.nature())
                .collect(Collectors.toCollection(TreeSet::new));

        Set<String> attendues = Set.of(
                "chenille.anneaux->CONTIENT"
        );

        assertEquals(attendues, obtenues);
    }

    @Test
    void contient_n_est_pas_une_dependance_statique() {
        assertTrue(NatureRelation.DEPEND_DE.estDependanceStatique());
        assertTrue(NatureRelation.CREE.estDependanceStatique());
        assertTrue(NatureRelation.SOUS_TYPE_DE.estDependanceStatique());
        assertTrue(NatureRelation.AGREGE.estDependanceStatique());
        assertTrue(!NatureRelation.CONTIENT.estDependanceStatique());
    }

    private IEntite trouverEntite(String nom) {
        return graphe.entites().stream()
                .filter(e -> e.nom().equals(nom))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Entité introuvable : " + nom));
    }
}