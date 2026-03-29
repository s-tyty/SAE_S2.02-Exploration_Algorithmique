import java.util.*;

public class GrapheMatrice {
    // Permet de faire le lien entre le nom (String) et l'indice (int) dans le tableau 
    private Map<String, Integer> sommets = new HashMap<>();
    private String[][] matrice;
    private int nbMax;

    public GrapheMatrice(int nbMax) {
        this.nbMax = nbMax;
        this.matrice = new String[nbMax][nbMax];
    }

    public void ajouterSommet(String nom) {
        if (!sommets.containsKey(nom)) {
            sommets.put(nom, sommets.size());
        }
    }

    public void ajouterArete(String source, String destination, String etiquette) {
        int i = sommets.get(source);
        int j = sommets.get(destination);
        // On stocke l'étiquette (ex: "create") directement dans la case 
        matrice[i][j] = etiquette;
    }
}