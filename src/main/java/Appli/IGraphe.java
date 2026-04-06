package Appli;

import Appli.Exceptions.AreteDejaExistante;
import Appli.Exceptions.SommetExisteDeja;
import Appli.Exceptions.SommetExistePas;
import Appli.Exceptions.AreteNonExistante;

import java.util.ArrayList;
import java.util.List;

public interface IGraphe {
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
