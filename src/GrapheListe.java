import java.util.*;

public class GrapheListe {
    private Map<String, List<Arete>> adjacences = new HashMap<>();

    // Petite classe interne pour stocker le voisin et l'étiquette
    private static class Arete {
        String dest;
        String etiquette;
        Arete(String d, String e) { this.dest = d; this.etiquette = e; }
    }

    public void ajouterSommet(String nom) {
        adjacences.putIfAbsent(nom, new ArrayList<>());
    }

    public void ajouterArete(String source, String destination, String etiquette) {
        adjacences.get(source).add(new Arete(destination, etiquette));
    }
}