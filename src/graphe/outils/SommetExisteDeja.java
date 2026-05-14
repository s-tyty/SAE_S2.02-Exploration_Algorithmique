package graphe.outils;

public class SommetExisteDeja extends RuntimeException {
    public SommetExisteDeja(String sommet) {
        super("Le sommet '" + sommet + "' existe déjà");
    }
}
