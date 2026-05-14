package graphe.modele;

import org.junit.jupiter.api.Assertions;

public final class AssertsGraphe {
    private AssertsGraphe() {
    }

    public static void assertGraphesEquivalents(IGraphe attendu, IGraphe obtenu) {
        Assertions.assertEquals(
                UtilsTestGraphe.signatureEntites(attendu),
                UtilsTestGraphe.signatureEntites(obtenu),
                message("Différence sur les entités", attendu, obtenu)
        );

        Assertions.assertEquals(
                UtilsTestGraphe.signatureRelationsSortantes(attendu),
                UtilsTestGraphe.signatureRelationsSortantes(obtenu),
                message("Différence sur les relations sortantes", attendu, obtenu)
        );

        Assertions.assertEquals(
                UtilsTestGraphe.signatureRelationsEntrantes(attendu),
                UtilsTestGraphe.signatureRelationsEntrantes(obtenu),
                message("Différence sur les relations entrantes", attendu, obtenu)
        );
    }

    private static String message(String titre, IGraphe attendu, IGraphe obtenu) {
        return titre
                + "\n\n=== ATTENDU ===\n"
                + UtilsTestGraphe.dumpComplet(attendu)
                + "\n=== OBTENU ===\n"
                + UtilsTestGraphe.dumpComplet(obtenu);
    }
}