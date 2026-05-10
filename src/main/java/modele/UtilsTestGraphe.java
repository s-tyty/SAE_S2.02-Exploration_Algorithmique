package graphe.modele;


import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public final class UtilsTestGraphe {
    private UtilsTestGraphe() {
    }

    public static Set<String> signatureEntites(IGraphe graphe) {
        return graphe.entites().stream()
                .map(UtilsTestGraphe::signatureEntite)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public static Set<String> signatureRelationsSortantes(IGraphe graphe) {
        Set<String> resultats = new TreeSet<>();
        for (IEntite source : graphe.entites()) {
            for (RelationSortante r : graphe.relationsSortantes(source)) {
                resultats.add(source.nom() + " -" + r.nature() + "-> " + r.cible().nom());
            }
        }
        return resultats;
    }

    public static Set<String> signatureRelationsEntrantes(IGraphe graphe) {
        Set<String> resultats = new TreeSet<>();
        for (IEntite cible : graphe.entites()) {
            for (RelationEntrante r : graphe.relationsEntrantes(cible)) {
                resultats.add(r.source().nom() + " -" + r.nature() + "-> " + cible.nom());
            }
        }
        return resultats;
    }

    public static String signatureEntite(IEntite entite) {
        String type = entite.estType() ? "TYPE" : "NON_TYPE";
        return entite.nom() + " [" + type + "]";
    }

    public static String normaliserPlantUml(String texte) {
        return texte.lines()
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.joining("\n"));
    }

    public static String dumpComplet(IGraphe graphe) {
        StringBuilder sb = new StringBuilder();

        sb.append("ENTITES\n");
        signatureEntites(graphe).forEach(s -> sb.append("  ").append(s).append("\n"));

        sb.append("RELATIONS_SORTANTES\n");
        signatureRelationsSortantes(graphe).forEach(s -> sb.append("  ").append(s).append("\n"));

        sb.append("RELATIONS_ENTRANTES\n");
        signatureRelationsEntrantes(graphe).forEach(s -> sb.append("  ").append(s).append("\n"));

        return sb.toString();
    }
}