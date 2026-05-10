package graphe.modele;

public enum NatureRelation {
    CONTIENT,
    DEPEND_DE,
    CREE,
    SOUS_TYPE_DE,
    AGREGE;

    public boolean estDependanceStatique() {
        return this != CONTIENT;
    }
}