package graphe.outils;

import graphe.impl.Graphe;
import graphe.modele.IGraphe;
import org.junit.jupiter.api.Test;
import graphe.modele.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ImportExportPlantUmlTest {

    @Test
    void chenille_import_export_reimport_doit_preserver_le_graphe() {
        String entree = """
            @startuml
            package "chenille" #business {
               interface IAnneau
               interface IAnneauMobile
               class Chenille

               Chenille "1" o- "*" IAnneauMobile
               IAnneauMobile <|-- anneaux.Anneau
               IAnneau <|-- IAnneauMobile
               Chenille .[#red]> anneaux.Anneau : <color:red><<create>></color>
               Chenille .[#red]> anneaux.Tete : <color:red><<create>></color>

               package anneaux {
                 class Anneau
                 class Tete
                 Anneau <|-- Tete
               }

               package ihm {
                  class Appli
                  class Frame
                  Appli .> Chenille : <<create>>
                  Appli o-- Frame
                  Appli .> IAnneau
               }
            }
            @enduml
            """;

        IGraphe g1 = new Graphe();
        ImporteurPlantUml.charger(entree, g1);

        String exporte = ExportPlantUml.exporter(g1);

        IGraphe g2 = new Graphe();
        ImporteurPlantUml.charger(exporte, g2);

        AssertsGraphe.assertGraphesEquivalents(g1, g2);
    }

    @Test
    void chenille_export_normalise_doit_rester_stable() {
        String entree = """
            @startuml
            package "chenille" #business {
               interface IAnneau
               interface IAnneauMobile
               class Chenille

               Chenille "1" o- "*" IAnneauMobile
               IAnneauMobile <|-- anneaux.Anneau
               IAnneau <|-- IAnneauMobile
               Chenille .[#red]> anneaux.Anneau : <color:red><<create>></color>
               Chenille .[#red]> anneaux.Tete : <color:red><<create>></color>

               package anneaux {
                 class Anneau
                 class Tete
                 Anneau <|-- Tete
               }

               package ihm {
                  class Appli
                  class Frame
                  Appli .> Chenille : <<create>>
                  Appli o-- Frame
                  Appli .> IAnneau
               }
            }
            @enduml
            """;

        String attendu = """
            @startuml
            package "chenille" {
                class Chenille
                interface IAnneau
                interface IAnneauMobile
                package "anneaux" {
                    class Anneau
                    class Tete
                }
                package "ihm" {
                    class Appli
                    class Frame
                }
            }

            chenille.Chenille o-- chenille.IAnneauMobile
            chenille.Chenille .> chenille.anneaux.Anneau : <<create>>
            chenille.Chenille .> chenille.anneaux.Tete : <<create>>
            chenille.IAnneau <|-- chenille.IAnneauMobile
            chenille.IAnneauMobile <|-- chenille.anneaux.Anneau
            chenille.anneaux.Anneau <|-- chenille.anneaux.Tete
            chenille.ihm.Appli .> chenille.Chenille : <<create>>
            chenille.ihm.Appli .> chenille.IAnneau
            chenille.ihm.Appli o-- chenille.ihm.Frame
            @enduml
            """;

        IGraphe graphe = new Graphe();
        ImporteurPlantUml.charger(entree, graphe);

        String exporte = ExportPlantUml.exporter(graphe);

        assertEquals(
                UtilsTestGraphe.normaliserPlantUml(attendu),
                UtilsTestGraphe.normaliserPlantUml(exporte)
        );
    }

    @Test
    void deux_packages_avec_classes_homonymes_ne_doivent_pas_etre_fusionnes() {
        String entree = """
            @startuml
            package a {
                class X
            }
            package b {
                class X
                class Y
                Y .> X
            }
            @enduml
            """;

        IGraphe g1 = new Graphe();
        ImporteurPlantUml.charger(entree, g1);

        String exporte = ExportPlantUml.exporter(g1);

        IGraphe g2 = new Graphe();
        ImporteurPlantUml.charger(exporte, g2);

        AssertsGraphe.assertGraphesEquivalents(g1, g2);
    }

    @Test
    void abstract_class_et_interface_doivent_etre_preservees_a_l_export() {
        String entree = """
            @startuml
            package p {
                abstract class A
                interface I
                class C
                A <|-- C
                I <|-- C
            }
            @enduml
            """;

        IGraphe g1 = new Graphe();
        ImporteurPlantUml.charger(entree, g1);

        String exporte = ExportPlantUml.exporter(g1);

        String normalise = UtilsTestGraphe.normaliserPlantUml(exporte);

        assertTrue(normalise.contains("abstract class A"));
        assertTrue(normalise.contains("interface I"));
        assertTrue(normalise.contains("class C"));

        IGraphe g2 = new Graphe();
        ImporteurPlantUml.charger(exporte, g2);

        AssertsGraphe.assertGraphesEquivalents(g1, g2);
    }
}