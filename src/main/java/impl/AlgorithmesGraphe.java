import java.util.HashSet;
import java.util.Set;


public final class AlgorithmesGraphe {

    private AlgorithmesGraphe() {

     }

   
    //  dependantsDirects                                                  //
    
     * La relation CONTIENT n'est pas prise en compte
     * (voir {@link NatureRelation#estDependanceStatique()}).
     *
     * @param graphe le graphe à explorer
     * @param cible  l'entité cible
     * @return l'ensemble (éventuellement vide) des dépendants directs
     */
    public static Set<IEntite> dependantsDirects(IGraphe graphe, IEntite cible) {
        Set<IEntite> resultat = new HashSet<>();
        for (RelationEntrante re : graphe.relationsEntrantes(cible)) {
            if (re.nature().estDependanceStatique()) {
                resultat.add(re.source());
            }
        }
        return resultat;
    }

    
    //  dependantsElargis                                                  //

    
     /**
     * @param graphe le graphe à explorer
     * @param cible  l'entité cible
     * @return l'ensemble élargi des dépendants
     */

    public static Set<IEntite> dependantsElargis(IGraphe graphe, IEntite cible) {
        Set<IEntite> directs = dependantsDirects(graphe, cible);
        Set<IEntite> resultat = new HashSet<>(directs);

        for (IEntite dependant : directs) {
            remonterContenance(graphe, dependant, resultat);
        }
        return resultat;
    }

    
    //  Méthode auxiliaire privée                                         //
    

    /**
     * Remonte la hiérarchie de contenance à partir de {@code entite} et
     * ajoute chaque conteneur rencontré dans {@code accumulation}.
     * S'arrête après le premier paquetage (estType() == false).
     */
    private static void remonterContenance(IGraphe graphe,
                                           IEntite entite,
                                           Set<IEntite> accumulation) {
        IEntite courant = entite;
        while (true) {
            // Chercher l'entité qui contient 'courant'
            IEntite conteneur = trouverConteneur(graphe, courant);
            if (conteneur == null) {
                break;              // aucun conteneur : on s'arrête
            }
            accumulation.add(conteneur);
            if (!conteneur.estType()) {
                break;              // c'est un paquetage : on inclut et on s'arrête
            }
            // C'est un type contenant (classe imbriquée) : on continue de monter
            courant = conteneur;
        }
    }

    /**
     * Retourne l'entité qui contient {@code entite} via une relation CONTIENT,
     * ou null si aucune n'existe.
     */
    private static IEntite trouverConteneur(IGraphe graphe, IEntite entite) {
        for (RelationEntrante re : graphe.relationsEntrantes(entite)) {
            if (re.nature() == NatureRelation.CONTIENT) {
                return re.source();
            }
        }
        return null;
    }
}
