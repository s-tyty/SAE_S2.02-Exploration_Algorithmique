package sprint2;

import java.util.*;

/**
 * Implementation of IGraphe based on the AdjacencyListGraph from Sprint 1.
 * Uses a map-based adjacency list to store entities and their relations.
 */
public class Sprint2Graph implements IGraphe {

    // Maps an entity to a map of adjacent target entities and their relation natures.
    // We use String (nom) as the key internally to ensure distinct entities.
    private final Map<String, IEntite> entitiesByName;
    private final Map<String, Map<String, NatureRelation>> adjacencyList;

    // Reverse adjacency list to efficiently query incoming relations.
    private final Map<String, Map<String, NatureRelation>> reverseAdjacencyList;

    public Sprint2Graph() {
        this.entitiesByName = new HashMap<>();
        this.adjacencyList = new HashMap<>();
        this.reverseAdjacencyList = new HashMap<>();
    }

    @Override
    public boolean ajouterEntite(IEntite entite) {
        if (entite == null || entitiesByName.containsKey(entite.nom())) {
            return false;
        }
        entitiesByName.put(entite.nom(), entite);
        adjacencyList.put(entite.nom(), new HashMap<>());
        reverseAdjacencyList.put(entite.nom(), new HashMap<>());
        return true;
    }

    @Override
    public boolean ajouterRelation(IEntite source, IEntite cible, NatureRelation nature) {
        if (source == null || cible == null || nature == null) {
            return false;
        }

        String srcNom = source.nom();
        String tgtNom = cible.nom();

        if (!entitiesByName.containsKey(srcNom) || !entitiesByName.containsKey(tgtNom)) {
            return false;
        }

        Map<String, NatureRelation> outEdges = adjacencyList.get(srcNom);
        if (outEdges.containsKey(tgtNom)) {
            return false; // Edge already exists between these nodes
        }

        outEdges.put(tgtNom, nature);
        reverseAdjacencyList.get(tgtNom).put(srcNom, nature);

        return true;
    }

    @Override
    public Set<IEntite> entites() {
        return new HashSet<>(entitiesByName.values());
    }

    @Override
    public Set<RelationSortante> relationsSortantes(IEntite source) {
        if (source == null || !entitiesByName.containsKey(source.nom())) {
            return Collections.emptySet();
        }

        Set<RelationSortante> sortantes = new HashSet<>();
        Map<String, NatureRelation> outEdges = adjacencyList.get(source.nom());

        for (Map.Entry<String, NatureRelation> entry : outEdges.entrySet()) {
            IEntite cible = entitiesByName.get(entry.getKey());
            sortantes.add(new RelationSortante(cible, entry.getValue()));
        }

        return sortantes;
    }

    @Override
    public Set<RelationEntrante> relationsEntrantes(IEntite cible) {
        if (cible == null || !entitiesByName.containsKey(cible.nom())) {
            return Collections.emptySet();
        }

        Set<RelationEntrante> entrantes = new HashSet<>();
        Map<String, NatureRelation> inEdges = reverseAdjacencyList.get(cible.nom());

        for (Map.Entry<String, NatureRelation> entry : inEdges.entrySet()) {
            IEntite source = entitiesByName.get(entry.getKey());
            entrantes.add(new RelationEntrante(source, entry.getValue()));
        }

        return entrantes;
    }
}
