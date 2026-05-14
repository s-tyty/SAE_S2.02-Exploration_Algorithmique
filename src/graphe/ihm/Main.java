package graphe.ihm;

import graphe.impl.Graphe;
import graphe.outils.ImporteurPlantUml;
import graphe.modele.IGraphe;
import graphe.outils.ExportPlantUml;

public final class Main {
    public static void main(String[] args) {
        String diagramme = """
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

        IGraphe graphe = new Graphe();
        ImporteurPlantUml.charger(diagramme, graphe);

        System.out.println("=== PlantUML régénéré ===");
        System.out.println(ExportPlantUml.exporter(graphe));
    }
}