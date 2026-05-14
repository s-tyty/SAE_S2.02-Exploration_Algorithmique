package graphe.modele;

import java.util.Set;

public interface IGraphe {
    boolean ajouterEntite(IEntite entite);
    boolean ajouterRelation(IEntite source, IEntite cible, NatureRelation nature);

    Set<IEntite> entites();
    Set<RelationSortante> relationsSortantes(IEntite source);
    Set<RelationEntrante> relationsEntrantes(IEntite cible);
}