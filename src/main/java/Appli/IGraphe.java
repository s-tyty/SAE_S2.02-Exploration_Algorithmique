package Appli;

import java.util.ArrayList;
import java.util.List;

public interface IGraphe {
    boolean contientSommet(String nomSommet);

    Boolean contientArete(String sommetDepart, String sommetArrivee);

    void ajouterSommet(String nomSommet);

    void ajouterArete(String sommetDepart, String sommetArrivee);

    void ajouterArete(String depart, String arrivee, String etiquette);

    void supprimerSommet(String nomSommet);

    void supprimerArete(String sommetDepart, String sommetArrivee);

    List<String> listeSommet();

    ArrayList<String> listeSuccesseurSommet(String nomSommet);

    ArrayList<String> listePredecesseurSommet(String nomSommet);

    String getEtiquette(String depart, String arrivee);
}
