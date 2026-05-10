import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Représentation d'un graphe orienté par listes d'adjacence.
 *
 * Structure : deux HashMap indexées par entité.
 *   - sortantes : entité source  → ensemble de RelationSortante
 *   - entrantes : entité cible   → ensemble de RelationEntrante
 *
 * Cette double indexation permet d'obtenir en O(1) les voisins
 * entrants et sortants d'un sommet, au prix d'un stockage doublé.
 *
 * Implémente IGraphe (Sprint Final).
 */
public class Graphe implements IGraphe {

    // Maps indexées par entité
    private final Map<IEntite, Set<RelationSortante>> sortantes;
    private final Map<IEntite, Set<RelationEntrante>> entrantes;

    /** Crée un graphe vide. */
    public Graphe() {
        sortantes = new HashMap<>();
        entrantes = new HashMap<>();
    }

    //  Construction du graphe 

    @Override
    public boolean ajouterEntite(IEntite entite) {
        if (sortantes.containsKey(entite)) {
            return false;           // déjà présente
        }
        sortantes.put(entite, new HashSet<>());
        entrantes.put(entite, new HashSet<>());
        return true;
    }

    @Override
    public boolean ajouterRelation(IEntite source, IEntite cible, NatureRelation nature) {
        // S'assurer que les deux sommets existent
        ajouterEntite(source);
        ajouterEntite(cible);

        // Tenter d'ajouter la relation (un Set refuse les doublons)
        boolean nouveau = sortantes.get(source).add(new RelationSortante(cible, nature));
        if (nouveau) {
            entrantes.get(cible).add(new RelationEntrante(source, nature));
        }
        return nouveau;
    }

    //  Consultation du graphe                                             //
    

    @Override
    public Set<IEntite> entites() {
        return Collections.unmodifiableSet(sortantes.keySet());
    }

    @Override
    public Set<RelationSortante> relationsSortantes(IEntite source) {
        Set<RelationSortante> res = sortantes.get(source);
        return res == null ? Collections.emptySet() : Collections.unmodifiableSet(res);
    }

    @Override
    public Set<RelationEntrante> relationsEntrantes(IEntite cible) {
        Set<RelationEntrante> res = entrantes.get(cible);
        return res == null ? Collections.emptySet() : Collections.unmodifiableSet(res);
    }

   
    //  Affichage                                                          //
   
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graphe (listes d'adjacence) :\n");
        for (Map.Entry<IEntite, Set<RelationSortante>> e : sortantes.entrySet()) {
            sb.append("  ").append(e.getKey().nom()).append(" → ").append(e.getValue()).append('\n');
        }
        return sb.toString();
    }
}
