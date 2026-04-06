package Appli.Graphe;

import Appli.Exceptions.AreteDejaExistante;
import Appli.Exceptions.AreteNonExistante;
import Appli.Exceptions.SommetExisteDeja;
import Appli.Exceptions.SommetExistePas;
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
    public void ajouterSommet(String nomSommet) throws SommetExisteDeja {
        if (contientSommet(nomSommet))
        {
            throw new SommetExisteDeja(nomSommet);
        }
            adjacence.put(nomSommet, new HashMap<>());
    }

    @Override
    public void ajouterArete(String sommetDepart, String sommetArrivee) {
        ajouterArete(sommetDepart, sommetArrivee, "");
    }

    @Override
    public void ajouterArete(String depart, String arrivee, String etiquette) throws SommetExistePas, AreteDejaExistante {
        if (!contientSommet(depart)){
            throw new SommetExistePas(depart);
        }
        if (!contientSommet(arrivee)){
            throw new SommetExistePas(arrivee);
        }
        if (contientArete(depart, arrivee)){
            throw new AreteDejaExistante(depart,arrivee);
        }
        adjacence.get(depart).put(arrivee, etiquette);

    }

    @Override
    public void supprimerSommet(String nomSommet) throws SommetExistePas{
        if (!contientSommet(nomSommet))
        {
            throw new SommetExistePas(nomSommet);
        }
        for (Map<String, String> successeurs : adjacence.values()) {
            successeurs.remove(nomSommet);
        }
        adjacence.remove(nomSommet);
    }

    @Override
    public void supprimerArete(String sommetDepart, String sommetArrivee)throws SommetExistePas, AreteNonExistante {
        if (!contientSommet(sommetDepart)){
            throw new SommetExistePas(sommetDepart);
        }
        if (!contientSommet(sommetArrivee)){
            throw new SommetExistePas(sommetArrivee);
        }
        if (!contientArete(sommetDepart, sommetArrivee)){
            throw new AreteNonExistante(sommetDepart,sommetArrivee);
        }
        adjacence.get(sommetDepart).remove(sommetArrivee);

    }

    @Override
    public List<String> listeSommet() {
        return new ArrayList<>(adjacence.keySet());
    }

    @Override
    public ArrayList<String> listeSuccesseurSommet(String nomSommet) throws SommetExistePas {
        if (!contientSommet(nomSommet))
        {
            throw new SommetExistePas(nomSommet);
        }
        return new ArrayList<>(adjacence.get(nomSommet).keySet());
    }

    @Override
    public ArrayList<String> listePredecesseurSommet(String nomSommet) throws SommetExistePas {
        if (!contientSommet(nomSommet))
        {
            throw new SommetExistePas(nomSommet);
        }
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