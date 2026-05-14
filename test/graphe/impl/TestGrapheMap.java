package graphe.impl;

import graphe.outils.AreteDejaExistante;
import graphe.outils.SommetExisteDeja;
import graphe.outils.SommetExistePas;
import graphe.outils.AreteNonExistante;
import graphe.modele.IGrapheSprint1;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestGrapheMap {

    @Test
    public void testExceptionAjouterSommet() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));
        try {
            g.ajouterSommet("A");
            throw new RuntimeException();
        } catch (SommetExisteDeja _) {}
    }

    @Test
    public void testExceptionAjouterArete() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));

        try {
            g.ajouterArete("A","B");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}

        try {
            g.ajouterArete("B","A");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}

        g.ajouterSommet("B");
        assert (g.contientSommet("B"));
        g.ajouterArete("A","B");
        assert (g.contientArete("A","B"));
        try {
            g.ajouterArete("A","B");
            throw new RuntimeException();
        } catch (AreteDejaExistante _) {}
    }

    @Test
    public void testExceptionSupprimerSommet() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));

        try {
            g.supprimerSommet("B");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}
    }

    @Test
    public void testExceptionSupprimerArete() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));

        try {
            g.supprimerArete("A","B");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}

        try {
            g.supprimerArete("B","A");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}

        g.ajouterSommet("B");
        assert (g.contientSommet("B"));
        try {
            g.supprimerArete("A","B");
            throw new RuntimeException();
        } catch (AreteNonExistante _) {}
    }

    @Test
    public void testExceptionListeSuccesseurSommet() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));

        try {
            g.listeSuccesseurSommet("B");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}
    }

    @Test
    public void testExceptionListePredecesseurSommet() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));

        try {
            g.listePredecesseurSommet("B");
            throw new RuntimeException();
        } catch (SommetExistePas _) {}
    }


    @Test
    public void contientSommet_sommetAjoute_retourneVrai() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));
        assert (!g.contientSommet("Z"));
    }

    @Test
    public void contientSommet_apresSupression_retourneFaux() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.supprimerSommet("A");
        assert (!g.contientSommet("A"));
        assert (g.contientSommet("B"));
    }

    @Test
    public void ajouterSommet_unSommet_estBienPresent() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (g.contientSommet("A"));
        assert (!g.contientSommet("B"));
    }

    @Test
    public void ajouterSommet_plusieursSommets_tousPresents() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        assert (g.contientSommet("A"));
        assert (g.contientSommet("B"));
        assert (g.contientSommet("C"));
        assert (!g.contientSommet("D"));
    }


    @Test
    public void supprimerSommet_sommetExistant_nEstPlusPresent() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.supprimerSommet("A");
        assert (!g.contientSommet("A"));
        assert (g.contientSommet("B"));
    }

    @Test
    public void supprimerSommet_supprimeLesAretes_associees() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterArete("A", "B");
        g.ajouterArete("A", "C");
        g.supprimerSommet("B");
        assert (!g.contientSommet("B"));
        assert (!g.listeSuccesseurSommet("A").contains("B"));
        assert (g.contientArete("A", "C"));
    }


    @Test
    public void contientArete_areteAjoutee_retourneVrai() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterArete("A", "B");
        assert (g.contientArete("A", "B"));
        assert (!g.contientArete("B", "A"));
    }

    @Test
    public void contientArete_sansBoucleEtApresSupression_retourneFaux() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        assert (!g.contientArete("A", "A"));
        g.ajouterArete("A", "B");
        g.supprimerArete("A", "B");
        assert (!g.contientArete("A", "B"));
    }

    @Test
    public void contientArete_sommetInexistant_retourneFaux() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        assert (!g.contientArete("A", "B"));
        assert (!g.contientArete("B", "A"));
    }


    @Test
    public void ajouterArete_areteValide_estBienPresente() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterArete("A", "B");
        assert (g.contientArete("A", "B"));
        assert (!g.contientArete("B", "A"));
        assert (!g.contientArete("A", "C"));
    }

    @Test
    public void ajouterArete_multiplesAretes_correctementAjoutees() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterArete("A", "B");
        g.ajouterArete("B", "C");
        assert (g.contientArete("A", "B"));
        assert (g.contientArete("B", "C"));
        assert (!g.contientArete("A", "C"));
        assert (!g.contientArete("C", "B"));
    }


    @Test
    public void supprimerArete_areteExistante_nEstPlusPresente() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterArete("A", "B");
        g.ajouterArete("A", "C");
        g.supprimerArete("A", "B");
        assert (!g.contientArete("A", "B"));
        assert (g.contientArete("A", "C"));
    }

    @Test
    public void supprimerArete_neSupprimePasAreteInverse() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterArete("A", "B");
        g.ajouterArete("B", "A");
        g.supprimerArete("A", "B");
        assert (!g.contientArete("A", "B"));
        assert (g.contientArete("B", "A"));
    }


    @Test
    public void listeSommet_grapheVide_retourneListeVide() {
        IGrapheSprint1 g = new GrapheMap();
        assert (g.listeSommet().isEmpty());
        g.ajouterSommet("A");
        assert (g.listeSommet().size() == 1);
        assert (g.listeSommet().contains("A"));
    }

    @Test
    public void listeSommet_apresSupression_nContientPlusSommet() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.supprimerSommet("B");
        List<String> liste = g.listeSommet();
        assert (liste.size() == 2);
        assert (!liste.contains("B"));
        assert (liste.contains("A"));
        assert (liste.contains("C"));
    }


    @Test
    public void listeSuccesseurSommet_sansArete_retourneListeVide() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        assert (g.listeSuccesseurSommet("A").isEmpty());
        assert (!g.listeSuccesseurSommet("A").contains("B"));
    }

    @Test
    public void listeSuccesseurSommet_avecEtSansAretes_retourneCorrectement() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterArete("A", "B");
        g.ajouterArete("B", "A");
        List<String> successeurs = g.listeSuccesseurSommet("A");
        assert (successeurs.size() == 1);
        assert (successeurs.contains("B"));
        assert (!successeurs.contains("C"));
        g.supprimerArete("A", "B");
        assert (g.listeSuccesseurSommet("A").isEmpty());
    }

    @Test
    public void listeSuccesseurSommet_areteAvecEtiquette_estBienIncluse() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterArete("A", "B", "create");
        List<String> successeurs = g.listeSuccesseurSommet("A");
        assert (successeurs.size() == 1);
        assert (successeurs.contains("B"));
    }

    @Test
    public void listePredecesseurSommet_sansArete_retourneListeVide() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        assert (g.listePredecesseurSommet("B").isEmpty());
        assert (!g.listePredecesseurSommet("B").contains("A"));
    }

    @Test
    public void listePredecesseurSommet_avecEtSansAretes_retourneCorrectement() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterSommet("C");
        g.ajouterArete("A", "C");
        g.ajouterArete("B", "C");
        List<String> predecesseurs = g.listePredecesseurSommet("C");
        assert (predecesseurs.size() == 2);
        assert (predecesseurs.contains("A"));
        assert (predecesseurs.contains("B"));
        assert (!g.listePredecesseurSommet("A").contains("B"));
        g.supprimerArete("A", "C");
        assert (!g.listePredecesseurSommet("C").contains("A"));
        assert (g.listePredecesseurSommet("C").contains("B"));
    }

    @Test
    public void getEtiquette_areteAvecEtiquette_retourneEtiquette() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterArete("A", "B", "create");
        assert ("create".equals(g.getEtiquette("A", "B")));
    }

    @Test
    public void getEtiquette_areteSansEtiquette_retourneChaineVide() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        g.ajouterArete("A", "B");
        assert ("".equals(g.getEtiquette("A", "B")));
    }

    @Test
    public void getEtiquette_areteInexistante_retourneChaineVide() {
        IGrapheSprint1 g = new GrapheMap();
        g.ajouterSommet("A");
        g.ajouterSommet("B");
        assert ("".equals(g.getEtiquette("A", "B")));
    }
}