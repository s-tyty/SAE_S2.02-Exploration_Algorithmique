package Appli.Graphe;

import Appli.IGraphe;

import java.util.ArrayList;
import java.util.List;


public class GrapheMatrice implements IGraphe {

    private final List<String> sommets;
    private final ArrayList<ArrayList<String>> matriceAdjacence;

    public GrapheMatrice(){
        sommets = new ArrayList<>();
        matriceAdjacence = new ArrayList<>();
    }

    @Override
    public boolean contientSommet(String nomSommet){
        return sommets.contains(nomSommet);
    }

    @Override
    public Boolean contientArete(String depart, String arrivee) {
        if (!contientSommet(depart) || !contientSommet(arrivee)) return false;
        return matriceAdjacence.get(sommets.indexOf(depart))
                .get(sommets.indexOf(arrivee)) != null;
    }

    @Override
    public void ajouterSommet(String nomSommet) {
        assert (!contientSommet(nomSommet));

        sommets.add(nomSommet);

        int nouvelleTaille = sommets.size();

        for (ArrayList<String> ligne : matriceAdjacence) {
            ligne.add(null);
        }

        ArrayList<String> nouvelleLigne = new ArrayList<>();
        for (int i = 0; i < nouvelleTaille; i++) {
            nouvelleLigne.add(null);
        }

        matriceAdjacence.add(nouvelleLigne);
    }

    @Override
    public void ajouterArete(String depart, String arrivee) {
        ajouterArete(depart, arrivee, "");
    }
    @Override
    public void ajouterArete(String depart, String arrivee, String etiquette) {
        assert (contientSommet(depart) && contientSommet(arrivee) && !contientArete(depart, arrivee));
        matriceAdjacence.get(sommets.indexOf(depart))
                .set(sommets.indexOf(arrivee), etiquette);
    }

    @Override
    public void supprimerSommet(String nomSommet){
        assert (contientSommet(nomSommet));

        int indice = sommets.indexOf(nomSommet);
        matriceAdjacence.remove(indice);
        for (ArrayList<String> ligne : matriceAdjacence) {
            ligne.remove(indice);
        }
        sommets.remove(indice);
    }

    @Override
    public void supprimerArete(String sommetDepart, String sommetArrivee){
        assert (contientSommet(sommetDepart) && contientSommet(sommetArrivee) && contientArete(sommetDepart,sommetArrivee));
        matriceAdjacence.get(sommets.indexOf(sommetDepart)).set(sommets.indexOf(sommetArrivee),null);
    }

    @Override
    public List<String> listeSommet(){
        return new ArrayList<>(sommets);
    }

    @Override
    public ArrayList<String> listeSuccesseurSommet(String nomSommet) {
        assert (contientSommet(nomSommet));
        ArrayList<String> listeSuccesseur = new ArrayList<>();
        int n = 0, indiceSommet = sommets.indexOf(nomSommet);
        for (String cellule : matriceAdjacence.get(indiceSommet)) {
            if (cellule != null)  // null = pas d'arête, "" ou étiquette = arête
                listeSuccesseur.add(sommets.get(n));
            n++;
        }
        return listeSuccesseur;
    }

    @Override
    public ArrayList<String> listePredecesseurSommet(String nomSommet) {
        assert (contientSommet(nomSommet));
        ArrayList<String> listePredecesseur = new ArrayList<>();
        int indiceSommet = sommets.indexOf(nomSommet);
        for (int i = 0; i < matriceAdjacence.size(); i++) {
            if (matriceAdjacence.get(i).get(indiceSommet) != null) {
                listePredecesseur.add(sommets.get(i));
            }
        }
        return listePredecesseur;
    }

    @Override
    public String getEtiquette(String depart, String arrivee) {
        if (!contientArete(depart, arrivee)) return "";
        String etiquette = matriceAdjacence.get(sommets.indexOf(depart))
                .get(sommets.indexOf(arrivee));
        return etiquette != null ? etiquette : "";
    }
}
