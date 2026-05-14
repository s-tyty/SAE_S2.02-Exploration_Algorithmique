package graphe.outils;

import graphe.impl.Entite;
import graphe.impl.TypeEntite;
import graphe.modele.IGraphe;
import graphe.modele.IEntite;
import graphe.modele.NatureRelation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractAlgorithmesGrapheTest {

    protected IGraphe graphe;

    protected abstract IGraphe creerGrapheVide();

    @BeforeEach
    void setUp() {
        String diagramme = """
            @startuml
            package "chenille" {
               class Chenille
               interface IAnneau

               Chenille o-- IAnneau
               IAnneau <|-- anneaux.Anneau

               package anneaux {
                 class Anneau
                 class Tete
                 Anneau <|-- Tete
               }

               package ihm {
                  class Appli
                  class Frame
                  Appli .> Chenille : <<create>>
                  Appli .> chenille.anneaux.Tete : <<create>>
                  Appli .> chenille.anneaux.Anneau : <<create>>
                  Appli o-- Frame
               }
            }
            @enduml
            """;

        graphe = creerGrapheVide();
        ImporteurPlantUml.charger(diagramme, graphe);
    }

    @Test
    void dependants_directs_de_tete() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsDirects(graphe, e("chenille.anneaux.Tete"))
        );

        assertEquals(Set.of("chenille.ihm.Appli"), res);
    }

    @Test
    void dependants_elargis_de_tete() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.anneaux.Tete"))
        );

        assertEquals(Set.of(
                "chenille.ihm",
                "chenille.ihm.Appli"
        ), res);
    }

    @Test
    void dependants_directs_de_anneau() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsDirects(graphe, e("chenille.anneaux.Anneau"))
        );

        assertEquals(Set.of(
                "chenille.anneaux.Tete",
                "chenille.ihm.Appli"
        ), res);
    }

    @Test
    void dependants_elargis_de_anneau() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.anneaux.Anneau"))
        );

        assertEquals(Set.of(
                "chenille.anneaux",
                "chenille.anneaux.Tete",
                "chenille.ihm",
                "chenille.ihm.Appli"
        ), res);
    }

    @Test
    void piege_ne_pas_remonter_jusqu_au_package_racine_pour_tete() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.anneaux.Tete"))
        );

        assertFalse(res.contains("chenille"),
                "Erreur classique : vous remontez trop haut dans la hiérarchie.");
    }

    @Test
    void piege_ne_pas_remonter_jusqu_au_package_racine_pour_anneau() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.anneaux.Anneau"))
        );

        assertFalse(res.contains("chenille"),
                "Erreur classique : vous remontez trop haut dans la hiérarchie.");
    }

    @Test
    void pas_de_doublons_pour_tete() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.anneaux.Tete"))
        );

        assertEquals(2, res.size());
    }

    @Test
    void pas_de_doublons_pour_anneau() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.anneaux.Anneau"))
        );

        assertEquals(4, res.size());
    }

    protected IEntite e(String nom) {
        return graphe.entites().stream()
                .filter(x -> x.nom().equals(nom))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Entité introuvable : " + nom));
    }

    protected Set<String> noms(Set<IEntite> s) {
        return s.stream()
                .map(IEntite::nom)
                .collect(Collectors.toCollection(TreeSet::new));
    }

     // Test 1
    @Test
    void test1_appli_depend_directement_de_chenille_donc_dependance_elargie() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.Chenille"))
        );

        assertTrue(res.contains("chenille.ihm.Appli"));
    }

    // Test 2
    @Test
    void test2_package_ihm_contient_appli_donc_dependance_elargie_vers_chenille() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.Chenille"))
        );

        assertTrue(res.contains("chenille.ihm"));
    }

    // Test 3
    @Test
    void test3_frame_soeur_de_appli_ne_depend_pas_de_chenille() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.Chenille"))
        );

        assertFalse(res.contains("chenille.ihm.Frame"));
    }

    // Test 4
    @Test
    void test4_classe_c_contenant_appli_comme_inner_classe_depend_de_chenille() {
        IEntite c = new Entite("chenille.ihm.C", TypeEntite.CLASS);
        graphe.ajouterEntite(c);
        graphe.ajouterRelation(c, e("chenille.ihm.Appli"), NatureRelation.CONTIENT);

        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.Chenille"))
        );

        assertTrue(res.contains("chenille.ihm.C"));
    }

    // Test 5
    @Test
    void test5_chenille_ne_depend_pas_de_chenille_par_remontee_depuis_ihm() {
        Set<String> res = noms(
                AlgorithmesGraphe.dependantsElargis(graphe, e("chenille.Chenille"))
        );

        assertFalse(res.contains("chenille"),
                "On ne remonte pas au-dessus d'un paquetage : chenille.ihm ne rend pas chenille dépendant.");
    }
}
