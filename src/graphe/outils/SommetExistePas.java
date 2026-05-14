package graphe.outils;

public class SommetExistePas extends RuntimeException {
    public SommetExistePas(String sommet) {
        super("Le sommet '" + sommet + "' n'existe pas");
    }
}
