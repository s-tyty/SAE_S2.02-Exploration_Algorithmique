package Appli.Exceptions;

public class SommetExisteDeja extends RuntimeException {
    public SommetExisteDeja(String sommet) {
        super("Le sommet '" + sommet + "' existe déjà");
    }
}
