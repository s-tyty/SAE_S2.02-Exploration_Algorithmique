package graphe.outils;

import graphe.impl.Entite;
import graphe.impl.TypeEntite;
import graphe.modele.IGraphe;
import graphe.modele.IEntite;
import graphe.modele.NatureRelation;
import graphe.modele.RelationEntrante;
import graphe.modele.RelationSortante;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ExportPlantUml {
    private ExportPlantUml() {
    }

    public static String exporter(IGraphe graphe) {
        StringBuilder sb = new StringBuilder();
        sb.append("@startuml\n");

        Set<IEntite> dejaEmises = new HashSet<>();

        List<IEntite> racines = graphe.entites().stream()
                .filter(e -> parentsDe(graphe, e).isEmpty())
                .sorted(Comparator.comparing(IEntite::nom))
                .toList();

        for (IEntite racine : racines) {
            exporterRec(graphe, racine, null, sb, 0, dejaEmises);
        }

        List<IEntite> restantes = graphe.entites().stream()
                .filter(e -> !dejaEmises.contains(e))
                .sorted(Comparator.comparing(IEntite::nom))
                .toList();

        for (IEntite e : restantes) {
            exporterDeclarationSimple(e, null, sb, 0);
            dejaEmises.add(e);
        }

        sb.append("\n");
        exporterRelationsNonContenance(graphe, sb);

        sb.append("@enduml\n");
        return sb.toString();
    }

    private static void exporterRec(
            IGraphe graphe,
            IEntite entite,
            IEntite parent,
            StringBuilder sb,
            int indent,
            Set<IEntite> dejaEmises
    ) {
        if (dejaEmises.contains(entite)) {
            return;
        }

        if (estPackage(entite)) {
            indent(sb, indent)
                    .append("package ")
                    .append(formaterNom(nomLocal(entite, parent)))
                    .append(" {\n");

            dejaEmises.add(entite);

            List<IEntite> enfants = enfantsDe(graphe, entite).stream()
                    .sorted(Comparator.comparing(IEntite::nom))
                    .toList();

            for (IEntite enfant : enfants) {
                exporterRec(graphe, enfant, entite, sb, indent + 1, dejaEmises);
            }

            indent(sb, indent).append("}\n");
        } else {
            exporterDeclarationSimple(entite, parent, sb, indent);
            dejaEmises.add(entite);
        }
    }

    private static void exporterDeclarationSimple(IEntite entite, IEntite parent, StringBuilder sb, int indent) {
        indent(sb, indent);
        sb.append(motCleDeclaration(entite))
                .append(" ")
                .append(nomLocal(entite, parent))
                .append("\n");
    }

    private static String motCleDeclaration(IEntite entite) {
        if (entite instanceof Entite e) {
            TypeEntite t = e.type();
            return switch (t) {
                case INTERFACE -> "interface";
                case ABSTRACT_CLASS -> "abstract class";
                case PACKAGE -> "class";
                case CLASS -> "class";
            };
        }
        return entite.estType() ? "class" : "class";
    }

    private static void exporterRelationsNonContenance(IGraphe graphe, StringBuilder sb) {
        List<String> lignes = new ArrayList<>();

        List<IEntite> entitesTriees = graphe.entites().stream()
                .sorted(Comparator.comparing(IEntite::nom))
                .toList();

        for (IEntite source : entitesTriees) {
            List<RelationSortante> rels = graphe.relationsSortantes(source).stream()
                    .filter(r -> r.nature() != NatureRelation.CONTIENT)
                    .sorted(Comparator
                            .comparing((RelationSortante r) -> r.cible().nom())
                            .thenComparing(r -> r.nature().name()))
                    .toList();

            for (RelationSortante r : rels) {
                lignes.add(versLignePlantUml(source, r));
            }
        }

        for (String ligne : lignes) {
            sb.append(ligne).append("\n");
        }
    }

    private static String versLignePlantUml(IEntite source, RelationSortante r) {
        String s = source.nom();
        String c = r.cible().nom();

        return switch (r.nature()) {
            case DEPEND_DE -> s + " .> " + c;
            case CREE -> s + " .> " + c + " : <<create>>";
            case SOUS_TYPE_DE -> c + " <|-- " + s;
            case AGREGE -> s + " o-- " + c;
            case CONTIENT -> throw new IllegalArgumentException("CONTIENT ne doit pas être exportée ici");
        };
    }

    private static List<IEntite> enfantsDe(IGraphe graphe, IEntite parent) {
        return graphe.relationsSortantes(parent).stream()
                .filter(r -> r.nature() == NatureRelation.CONTIENT)
                .map(RelationSortante::cible)
                .toList();
    }

    private static List<IEntite> parentsDe(IGraphe graphe, IEntite enfant) {
        return graphe.relationsEntrantes(enfant).stream()
                .filter(r -> r.nature() == NatureRelation.CONTIENT)
                .map(RelationEntrante::source)
                .toList();
    }

    private static boolean estPackage(IEntite entite) {
        if (entite instanceof Entite e) {
            return e.type() == TypeEntite.PACKAGE;
        }
        return false;
    }

    private static String nomLocal(IEntite entite, IEntite parent) {
        if (parent == null) {
            return nomCourt(entite.nom());
        }

        String prefixe = parent.nom() + ".";
        if (entite.nom().startsWith(prefixe)) {
            return entite.nom().substring(prefixe.length());
        }

        return nomCourt(entite.nom());
    }

    private static String nomCourt(String nomComplet) {
        int i = nomComplet.lastIndexOf('.');
        return i >= 0 ? nomComplet.substring(i + 1) : nomComplet;
    }

    private static String formaterNom(String nom) {
        return "\"" + nom + "\"";
    }

    private static StringBuilder indent(StringBuilder sb, int niveau) {
        return sb.append("    ".repeat(Math.max(0, niveau)));
    }
}