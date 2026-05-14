package graphe.impl;

import graphe.outils.AreteDejaExistante;
import graphe.outils.SommetExisteDeja;
import graphe.outils.SommetExistePas;
import graphe.outils.AreteNonExistante;
import graphe.modele.IGrapheSprint1;

import java.util.ArrayList;
import java.util.List;


public class GrapheMatrice implements IGrapheSprint1 {

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
    public void ajouterSommet(String nomSommet) throws SommetExisteDeja {
        if (contientSommet(nomSommet))
        {
            throw new SommetExisteDeja(nomSommet);
        }

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
    public void ajouterArete(String depart, String arrivee, String etiquette) throws SommetExistePas,AreteDejaExistante{
        if (!contientSommet(depart)){
            throw new SommetExistePas(depart);
        }
        if (!contientSommet(arrivee)){
            throw new SommetExistePas(arrivee);
        }
        if (contientArete(depart, arrivee)){
            throw new AreteDejaExistante(depart,arrivee);
        }
        matriceAdjacence.get(sommets.indexOf(depart))
                .set(sommets.indexOf(arrivee), etiquette);
    }

    @Override
    public void supprimerSommet(String nomSommet) throws SommetExistePas {
        if (!contientSommet(nomSommet))
        {
            throw new SommetExistePas(nomSommet);
        }

        int indice = sommets.indexOf(nomSommet);
        matriceAdjacence.remove(indice);
        for (ArrayList<String> ligne : matriceAdjacence) {
            ligne.remove(indice);
        }
        sommets.remove(indice);
    }

    @Override
    public void supprimerArete(String sommetDepart, String sommetArrivee) throws SommetExistePas, AreteNonExistante{
        if (!contientSommet(sommetDepart)){
            throw new SommetExistePas(sommetDepart);
        }
        if (!contientSommet(sommetArrivee)){
            throw new SommetExistePas(sommetArrivee);
        }
        if (!contientArete(sommetDepart, sommetArrivee)){
            throw new AreteNonExistante(sommetDepart,sommetArrivee);
        }
        matriceAdjacence.get(sommets.indexOf(sommetDepart)).set(sommets.indexOf(sommetArrivee),null);
    }

    @Override
    public List<String> listeSommet(){
        return new ArrayList<>(sommets);
    }

    @Override
    public ArrayList<String> listeSuccesseurSommet(String nomSommet) throws SommetExistePas {
        if (!contientSommet(nomSommet))
        {
            throw new SommetExistePas(nomSommet);
        }
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
    public ArrayList<String> listePredecesseurSommet(String nomSommet) throws SommetExistePas{
        if (!contientSommet(nomSommet))
        {
            throw new SommetExistePas(nomSommet);
        }
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
        return matriceAdjacence.get(sommets.indexOf(depart))
                .get(sommets.indexOf(arrivee));
    }
}
