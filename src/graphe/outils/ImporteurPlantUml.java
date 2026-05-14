package graphe.outils;

import graphe.impl.Entite;
import graphe.impl.TypeEntite;
import graphe.modele.IGraphe;
import graphe.modele.IEntite;
import graphe.modele.NatureRelation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImporteurPlantUml {
    private final IGraphe graphe;

    private final Map<String, Entite> entitesParNomCanonique = new HashMap<>();
    private final Deque<Entite> pilePackages = new ArrayDeque<>();

    private static final Pattern ABSTRACT_CLASS_PATTERN =
            Pattern.compile("^abstract\\s+class\\s+([\\w.]+)$");

    private static final Pattern CLASS_PATTERN =
            Pattern.compile("^class\\s+([\\w.]+)$");

    private static final Pattern INTERFACE_PATTERN =
            Pattern.compile("^interface\\s+([\\w.]+)$");

    private static final Pattern SUBTYPE_PATTERN =
            Pattern.compile("^([\\w.]+)\\s*<\\|--\\s*([\\w.]+)$");

    private static final Pattern CREATE_PATTERN =
            Pattern.compile("^([\\w.]+)\\s*\\.>\\s*([\\w.]+)\\s*:\\s*<<create>>$");

    private static final Pattern DEP_PATTERN =
            Pattern.compile("^([\\w.]+)\\s*\\.>\\s*([\\w.]+)$");

    private static final Pattern AGG_PATTERN =
            Pattern.compile("^([\\w.]+)\\s*o--?\\s*([\\w.]+)$");

    public ImporteurPlantUml(IGraphe graphe) {
        this.graphe = Objects.requireNonNull(graphe);
    }

    public static void charger(Path fichier, IGraphe graphe) throws IOException {
        new ImporteurPlantUml(graphe).charger(Files.readAllLines(fichier));
    }

    public static void charger(String texte, IGraphe graphe) {
        new ImporteurPlantUml(graphe).charger(texte.lines().toList());
    }

    public void charger(List<String> lignesBrutes) {
        int numero = 0;

        for (String brute : lignesBrutes) {
            numero++;
            String ligne = normaliser(brute);

            if (ligne.isEmpty()) {
                continue;
            }

            if (ligne.equals("@startuml") || ligne.equals("@enduml")) {
                continue;
            }

            if (ligne.equals("}")) {
                if (pilePackages.isEmpty()) {
                    throw new ParseException("Accolade fermante inattendue à la ligne " + numero);
                }
                pilePackages.pop();
                continue;
            }

            if (essayerPackage(ligne)) {
                continue;
            }
            if (essayerDeclarationType(ligne)) {
                continue;
            }
            if (essayerRelation(ligne)) {
                continue;
            }

            throw new ParseException("Syntaxe non supportée à la ligne " + numero + " : " + brute);
        }

        if (!pilePackages.isEmpty()) {
            throw new ParseException("Il manque une ou plusieurs accolades fermantes de package.");
        }
    }

    private String normaliser(String ligne) {
        String s = ligne.strip();

        if (s.isEmpty() || s.startsWith("'") || s.startsWith("//")) {
            return "";
        }

        s = supprimerBalisesColor(s);
        s = normaliserFlechesColorees(s);
        s = supprimerMultiplicites(s);
        s = s.replaceAll("\\s+", " ").trim();

        return s;
    }

    private String supprimerBalisesColor(String s) {
        s = s.replaceAll("</?color:[^>]+>", "");
        s = s.replaceAll("</?color>", "");
        return s;
    }

    private String normaliserFlechesColorees(String s) {
        return s.replaceAll("\\.\\s*\\[#\\w+\\]\\s*>", ".>");
    }

    /**
     * Supprime seulement les multiplicités UML classiques.
     * Ne supprime pas les noms de package entre guillemets.
     */
    private String supprimerMultiplicites(String s) {
        return s.replaceAll("\"(?:\\*|\\d+|\\d+\\.\\.\\d+|\\d+\\.\\.\\*|\\*\\.\\.\\*|\\d+\\.\\.n|n\\.\\.\\*|0\\.\\.1)\"", "");
    }

    private boolean essayerPackage(String ligne) {
        if (!ligne.startsWith("package ")) {
            return false;
        }
        if (!ligne.endsWith("{")) {
            return false;
        }

        String interieur = ligne.substring("package ".length(), ligne.length() - 1).trim();
        if (interieur.isEmpty()) {
            return false;
        }

        String nomLocal = extraireNomPackage(interieur);
        if (nomLocal == null || nomLocal.isBlank()) {
            return false;
        }

        String nomCanonique = nomCanoniquePourDeclarationDansContexte(nomLocal);

        Entite pkg = creerOuRecupererEntite(nomCanonique, TypeEntite.PACKAGE);
        graphe.ajouterEntite(pkg);

        if (!pilePackages.isEmpty()) {
            graphe.ajouterRelation(pilePackages.peek(), pkg, NatureRelation.CONTIENT);
        }

        pilePackages.push(pkg);
        return true;
    }

    private String extraireNomPackage(String interieur) {
        String s = interieur.trim();

        if (s.startsWith("\"")) {
            int fin = s.indexOf('"', 1);
            if (fin <= 0) {
                return null;
            }
            return s.substring(1, fin).trim();
        }

        int espace = s.indexOf(' ');
        if (espace < 0) {
            return s;
        }
        return s.substring(0, espace).trim();
    }

    private boolean essayerDeclarationType(String ligne) {
        Matcher m;

        m = ABSTRACT_CLASS_PATTERN.matcher(ligne);
        if (m.matches()) {
            declarerType(m.group(1), TypeEntite.ABSTRACT_CLASS);
            return true;
        }

        m = CLASS_PATTERN.matcher(ligne);
        if (m.matches()) {
            declarerType(m.group(1), TypeEntite.CLASS);
            return true;
        }

        m = INTERFACE_PATTERN.matcher(ligne);
        if (m.matches()) {
            declarerType(m.group(1), TypeEntite.INTERFACE);
            return true;
        }

        return false;
    }

    private void declarerType(String nomLu, TypeEntite type) {
        String nomCanonique = nomCanoniquePourDeclarationDansContexte(nomLu);
        Entite entite = creerOuRecupererEntite(nomCanonique, type);
        graphe.ajouterEntite(entite);

        if (!pilePackages.isEmpty()) {
            graphe.ajouterRelation(pilePackages.peek(), entite, NatureRelation.CONTIENT);
        }
    }

    private boolean essayerRelation(String ligne) {
        Matcher m;

        m = CREATE_PATTERN.matcher(ligne);
        if (m.matches()) {
            ajouterRelationDepuisSyntaxe(m.group(1), m.group(2), NatureRelation.CREE);
            return true;
        }

        m = SUBTYPE_PATTERN.matcher(ligne);
        if (m.matches()) {
            IEntite superType = resoudreNomReference(m.group(1));
            IEntite sousType = resoudreNomReference(m.group(2));
            graphe.ajouterRelation(sousType, superType, NatureRelation.SOUS_TYPE_DE);
            return true;
        }

        m = AGG_PATTERN.matcher(ligne);
        if (m.matches()) {
            ajouterRelationDepuisSyntaxe(m.group(1), m.group(2), NatureRelation.AGREGE);
            return true;
        }

        m = DEP_PATTERN.matcher(ligne);
        if (m.matches()) {
            ajouterRelationDepuisSyntaxe(m.group(1), m.group(2), NatureRelation.DEPEND_DE);
            return true;
        }

        return false;
    }

    private void ajouterRelationDepuisSyntaxe(String sourceLu, String cibleLu, NatureRelation nature) {
        IEntite source = resoudreNomReference(sourceLu);
        IEntite cible = resoudreNomReference(cibleLu);
        graphe.ajouterRelation(source, cible, nature);
    }

    private Entite resoudreNomReference(String nomLu) {
        // 1. Nom canonique exact déjà connu
        Entite exacte = entitesParNomCanonique.get(nomLu);
        if (exacte != null) {
            return exacte;
        }

        // 2. Nom local dans le contexte courant
        String candidatLocal = nomCanoniqueDansContexte(nomLu);
        Entite locale = entitesParNomCanonique.get(candidatLocal);
        if (locale != null) {
            return locale;
        }

        // 3. Référence partiellement qualifiée : on la rattache au package racine courant
        String candidatRacine = nomCanoniqueDepuisRacine(nomLu);
        if (candidatRacine != null) {
            Entite e = entitesParNomCanonique.get(candidatRacine);
            if (e != null) {
                return e;
            }
        }

        // 4. Recherche par suffixe unique
        Entite parSuffixe = chercherParSuffixeUnique(nomLu);
        if (parSuffixe != null) {
            return parSuffixe;
        }

        // 5. Création automatique d'une entité canonique
        String nomCanonique;
        if (nomLu.contains(".")) {
            nomCanonique = candidatRacine != null ? candidatRacine : nomLu;
        } else {
            nomCanonique = candidatLocal;
        }

        Entite e = creerOuRecupererEntite(nomCanonique, TypeEntite.CLASS);
        graphe.ajouterEntite(e);
        return e;
    }

    /**
     * Si on est dans un package racine, un nom qualifié partiel "anneaux.Anneau"
     * devient "chenille.anneaux.Anneau".
     */
    private String nomCanoniqueDepuisRacine(String nomLu) {
        if (!nomLu.contains(".")) {
            return null;
        }
        if (pilePackages.isEmpty()) {
            return nomLu;
        }

        Entite racine = pilePackages.peekLast();
        if (racine == null) {
            return nomLu;
        }

        return racine.nom() + "." + nomLu;
    }

    private Entite chercherParSuffixeUnique(String suffixe) {
        Entite resultat = null;
        String pointSuffixe = "." + suffixe;

        for (Map.Entry<String, Entite> entry : entitesParNomCanonique.entrySet()) {
            String nom = entry.getKey();
            if (nom.equals(suffixe) || nom.endsWith(pointSuffixe)) {
                if (resultat != null && !resultat.equals(entry.getValue())) {
                    return null;
                }
                resultat = entry.getValue();
            }
        }

        return resultat;
    }

    private Entite creerOuRecupererEntite(String nomCanonique, TypeEntite type) {
        Entite existante = entitesParNomCanonique.get(nomCanonique);
        if (existante != null) {
            return existante;
        }

        Entite e = new Entite(nomCanonique, type);
        entitesParNomCanonique.put(nomCanonique, e);
        return e;
    }

    private String nomCanoniquePourDeclarationDansContexte(String nomLu) {
        return nomCanoniqueDansContexte(nomLu);
    }

    private String nomCanoniqueDansContexte(String nomLocal) {
        if (pilePackages.isEmpty()) {
            return nomLocal;
        }
        return pilePackages.peek().nom() + "." + nomLocal;
    }
}