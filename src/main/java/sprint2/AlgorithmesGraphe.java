package sprint2;

import java.util.HashSet;
import java.util.Set;

public final class AlgorithmesGraphe {

    private AlgorithmesGraphe() {
        // Prevent instantiation
    }

    /**
     * Returns the entities that depend directly on the target
     * via a single static dependency edge.
     * The CONTIENT relation is NOT taken into account.
     */
    public static Set<IEntite> dependantsDirects(IGraphe graphe, IEntite cible) {
        Set<IEntite> dependants = new HashSet<>();
        if (graphe == null || cible == null) {
            return dependants;
        }

        for (RelationEntrante relation : graphe.relationsEntrantes(cible)) {
            if (relation.nature().estDependanceStatique()) {
                dependants.add(relation.source());
            }
        }
        return dependants;
    }

    /**
     * Returns the direct dependents of the target, then goes up by containment:
     * - through any containing types;
     * - up to the first package encountered, inclusive;
     * - without ever going up beyond this first package;
     * - without throwing an error if no enclosing package exists.
     */
    public static Set<IEntite> dependantsElargis(IGraphe graphe, IEntite cible) {
        Set<IEntite> result = new HashSet<>();
        if (graphe == null || cible == null) {
            return result;
        }

        // 1. Get direct static dependents
        Set<IEntite> directs = dependantsDirects(graphe, cible);
        result.addAll(directs);

        // 2. For each direct dependent, traverse up the CONTIENT hierarchy
        for (IEntite directDependent : directs) {
            traverseContainmentHierarchy(graphe, directDependent, result);
        }

        return result;
    }

    private static void traverseContainmentHierarchy(IGraphe graphe, IEntite currentEntity, Set<IEntite> result) {
        // Find entities that contain the currentEntity
        // This means currentEntity is the target of a CONTIENT relation
        for (RelationEntrante relation : graphe.relationsEntrantes(currentEntity)) {
            if (relation.nature() == NatureRelation.CONTIENT) {
                IEntite container = relation.source();

                // Avoid infinite loops in case of cyclic CONTIENT relations (though illogical)
                if (result.add(container)) {
                    // If it's a type (class/interface), continue traversing up
                    if (container.estType()) {
                        traverseContainmentHierarchy(graphe, container, result);
                    }
                    // If it's not a type (i.e., a package), we stop traversing up for this branch
                }
            }
        }
    }
}
