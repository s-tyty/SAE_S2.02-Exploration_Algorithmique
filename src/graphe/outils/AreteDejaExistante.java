package graphe.outils;

public class AreteDejaExistante extends RuntimeException {
    public AreteDejaExistante(String depart,String arrivee) {
        super("L'arête '" + depart + "->" + arrivee +"' existe déjà");
    }
}
