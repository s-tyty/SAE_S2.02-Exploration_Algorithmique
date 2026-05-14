package graphe.outils;

public class AreteNonExistante extends RuntimeException {
    public AreteNonExistante(String depart,String arrivee) {
        super("L'arête '" + depart + "->" + arrivee +"' n'existe pas");
    }
}
