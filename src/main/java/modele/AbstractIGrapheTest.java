package graphe.modele;

import graphe.impl.Entite;
import graphe.impl.TypeEntite;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractIGrapheTest {

    /**
     * Chaque sous-classe de test doit fournir une nouvelle instance vide
     * de son implémentation de IGraphe.
     */
    protected abstract IGraphe creerGrapheVide();

    protected IEntite entite(String nom, TypeEntite type) {
        return new Entite(nom, type);
    }

    @Test
    void egalite_des_entites() {
        assertTrue(entite("A", TypeEntite.CLASS).equals(entite("A", TypeEntite.CLASS)));
        assertTrue(entite("A", TypeEntite.CLASS).equals(entite("A", TypeEntite.INTERFACE)));
        assertFalse(entite("A", TypeEntite.CLASS).equals(entite("B", TypeEntite.CLASS)));
    }

    @Test
    void ajouterEntite_nouvelle_entite_retourne_true() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        assertTrue(g.ajouterEntite(a));
    }

    @Test
    void ajouterEntite_entite_deja_presente_retourne_false() {
        IGraphe g = creerGrapheVide();
        IEntite a1 = entite("A", TypeEntite.CLASS);
        assertTrue(g.ajouterEntite(a1));
        IEntite a2 = entite("A", TypeEntite.INTERFACE); // Même nom, mais type différent
        assertFalse(g.ajouterEntite(a2));
        IEntite a3 = entite("A", TypeEntite.CLASS);  // Même nom et meme type
        assertFalse(g.ajouterEntite(a3));
    }

    @Test
    void ajouterEntite_meme_entite_deux_fois_retourne_false_la_deuxieme_fois() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);

        assertTrue(g.ajouterEntite(a));
        assertFalse(g.ajouterEntite(a));
    }

    @Test
    void entites_contient_les_entites_ajoutees() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.INTERFACE);

        g.ajouterEntite(a);
        g.ajouterEntite(b);

        assertEquals(Set.of(a, b), g.entites());
    }

    @Test
    void ajouterRelation_ajoute_automatiquement_les_entites_absentes() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);

        assertTrue(g.ajouterRelation(a, b, NatureRelation.DEPEND_DE));

        assertTrue(g.entites().contains(a));
        assertTrue(g.entites().contains(b));
    }

    @Test
    void ajouterRelation_cree_une_sortante_et_une_entrante_coherentes() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);

        g.ajouterRelation(a, b, NatureRelation.CREE);

        assertEquals(
                Set.of(new RelationSortante(b, NatureRelation.CREE)),
                g.relationsSortantes(a)
        );

        assertEquals(
                Set.of(new RelationEntrante(a, NatureRelation.CREE)),
                g.relationsEntrantes(b)
        );
    }

    @Test
    void ajouterRelation_en_double_ne_duplique_pas_la_relation() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);

        assertTrue(g.ajouterRelation(a, b, NatureRelation.DEPEND_DE));
        assertFalse(g.ajouterRelation(a, b, NatureRelation.DEPEND_DE));

        assertEquals(1, g.relationsSortantes(a).size());
        assertEquals(1, g.relationsEntrantes(b).size());
    }

    @Test
    void relationsSortantes_sur_entite_inconnue_retourne_ensemble_vide() {
        IGraphe g = creerGrapheVide();
        IEntite inconnue = entite("X", TypeEntite.CLASS);

        assertTrue(g.relationsSortantes(inconnue).isEmpty());
    }

    @Test
    void relationsEntrantes_sur_entite_inconnue_retourne_ensemble_vide() {
        IGraphe g = creerGrapheVide();
        IEntite inconnue = entite("X", TypeEntite.CLASS);

        assertTrue(g.relationsEntrantes(inconnue).isEmpty());
    }

    @Test
    void entites_retourne_une_vue_non_modifiable() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        g.ajouterEntite(a);

        Set<IEntite> entites = g.entites();

        assertThrows(UnsupportedOperationException.class, () ->
                entites.add(entite("B", TypeEntite.CLASS))
        );
    }

    @Test
    void relationsSortantes_retourne_une_vue_non_modifiable() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);
        g.ajouterRelation(a, b, NatureRelation.DEPEND_DE);

        Set<RelationSortante> relations = g.relationsSortantes(a);

        assertThrows(UnsupportedOperationException.class, () ->
                relations.add(new RelationSortante(entite("C", TypeEntite.CLASS), NatureRelation.CREE))
        );
    }

    @Test
    void relationsEntrantes_retourne_une_vue_non_modifiable() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);
        g.ajouterRelation(a, b, NatureRelation.DEPEND_DE);

        Set<RelationEntrante> relations = g.relationsEntrantes(b);

        assertThrows(UnsupportedOperationException.class, () ->
                relations.add(new RelationEntrante(entite("C", TypeEntite.CLASS), NatureRelation.CREE))
        );
    }

    @Test
    void plusieurs_relations_de_natures_differentes_vers_la_meme_cible_sont_distinctes() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);

        g.ajouterRelation(a, b, NatureRelation.DEPEND_DE);
        g.ajouterRelation(a, b, NatureRelation.CREE);

        assertEquals(2, g.relationsSortantes(a).size());
        assertEquals(2, g.relationsEntrantes(b).size());
    }

    @Test
    void plusieurs_sources_vers_la_meme_cible_sont_bien_representees() {
        IGraphe g = creerGrapheVide();
        IEntite a = entite("A", TypeEntite.CLASS);
        IEntite b = entite("B", TypeEntite.CLASS);
        IEntite c = entite("C", TypeEntite.CLASS);

        g.ajouterRelation(a, c, NatureRelation.DEPEND_DE);
        g.ajouterRelation(b, c, NatureRelation.CREE);

        assertEquals(
                Set.of(
                        new RelationEntrante(a, NatureRelation.DEPEND_DE),
                        new RelationEntrante(b, NatureRelation.CREE)
                ),
                g.relationsEntrantes(c)
        );
    }
}
