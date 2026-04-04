package Appli.Graphe;

import Appli.IGraphe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrapheMap implements IGraphe {

    private final Map<String, Map<String, String>> adjacence;

    public GrapheMap() {
        this.adjacence = new HashMap<>();
    }

    @Override
    public boolean contientSommet(String nomSommet) {
        return adjacence.containsKey(nomSommet);
    }

    @Override
    public Boolean contientArete(String sommetDepart, String sommetArrivee) {
        if (!contientSommet(sommetDepart)) return false;
        return adjacence.get(sommetDepart).containsKey(sommetArrivee);
    }

    @Override
    public void ajouterSommet(String nomSommet) {
        if (!contientSommet(nomSommet)) {
            adjacence.put(nomSommet, new HashMap<>());
        }
    }

    @Override
    public void ajouterArete(String sommetDepart, String sommetArrivee) {
        ajouterArete(sommetDepart, sommetArrivee, "");
    }

    @Override
    public void ajouterArete(String depart, String arrivee, String etiquette) {
        if (contientSommet(depart) && contientSommet(arrivee)) {
            adjacence.get(depart).put(arrivee, etiquette);
        }
    }

    @Override
    public void supprimerSommet(String nomSommet) {
        if (!contientSommet(nomSommet)) return;
        for (Map<String, String> successeurs : adjacence.values()) {
            successeurs.remove(nomSommet);
        }
        adjacence.remove(nomSommet);
    }

    @Override
    public void supprimerArete(String sommetDepart, String sommetArrivee) {
        if (contientSommet(sommetDepart)) {
            adjacence.get(sommetDepart).remove(sommetArrivee);
        }
    }

    @Override
    public List<String> listeSommet() {
        return new ArrayList<>(adjacence.keySet());
    }

    @Override
    public ArrayList<String> listeSuccesseurSommet(String nomSommet) {
        if (!contientSommet(nomSommet)) return new ArrayList<>();
        return new ArrayList<>(adjacence.get(nomSommet).keySet());
    }

    @Override
    public ArrayList<String> listePredecesseurSommet(String nomSommet) {
        ArrayList<String> predecesseurs = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> entry : adjacence.entrySet()) {
            if (entry.getValue().containsKey(nomSommet)) {
                predecesseurs.add(entry.getKey());
            }
        }
        return predecesseurs;
    }

    @Override
    public String getEtiquette(String depart, String arrivee) {
        if (!contientArete(depart, arrivee)) return "";
        return adjacence.get(depart).get(arrivee);
    }
}