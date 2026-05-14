package graphe.modele;

import graphe.outils.AreteDejaExistante;
import graphe.outils.SommetExisteDeja;
import graphe.outils.SommetExistePas;
import graphe.outils.AreteNonExistante;

import java.util.ArrayList;
import java.util.List;

public interface IGrapheSprint1 {
    boolean contientSommet(String nomSommet);

    Boolean contientArete(String sommetDepart, String sommetArrivee);

    void ajouterSommet(String nomSommet) throws SommetExisteDeja;

    void ajouterArete(String sommetDepart, String sommetArrivee);

    void ajouterArete(String depart, String arrivee, String etiquette) throws SommetExistePas, AreteDejaExistante;

    void supprimerSommet(String nomSommet) throws SommetExistePas;

    void supprimerArete(String sommetDepart, String sommetArrivee) throws SommetExistePas, AreteNonExistante;

    List<String> listeSommet();

    ArrayList<String> listeSuccesseurSommet(String nomSommet) throws SommetExistePas;

    ArrayList<String> listePredecesseurSommet(String nomSommet) throws SommetExistePas;

    String getEtiquette(String depart, String arrivee);
}
