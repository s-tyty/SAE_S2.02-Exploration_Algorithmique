# SAE 2.02 – Exploration Algorithmique d'un Problème

## Composition de l'équipe

### GROUPE : 108

| NOM Prénom |
|-------|
| SITTININAE Aditya |
| MOISANT Lény |
| TCHOUANGOU Louis |
| MUHAMMAD MAJHARUL ISLAM Shafi |

## Sprint 1 – Représentation des graphes

### Ce qui a été réalisé

Implémentation de deux classes représentant un **graphe orienté étiqueté**, toutes deux conformes à l'interface `IGraphe` (version sprint 1) :

- **`GrapheMap`** : représentation par liste d'adjacence via une `HashMap<String, HashMap<String, String>>`.
- **`GrapheMatrice`** : représentation par matrice d'adjacence via une `ArrayList<ArrayList<String>>`.

Les deux implémentations supportent l'ajout et la suppression de sommets/d'arêtes, les arêtes avec ou sans étiquette, et la récupération des successeurs/prédécesseurs.

### Tests

Tests JUnit 5 dans `TestGrapheMap` et `TestGrapheMatrice` : chaque méthode non triviale a au moins deux tests.

---

## Sprint final (Sprint 2)

### Ce qui a été réalisé

**Exercice A – Implémentation de `IGraphe`**
- La classe `Graphe` (paquetage `graphe.impl`) implémente complètement l’interface `IGraphe` (graphe orienté avec entités et relations typées).
- Une classe de test `GrapheTest` hérite de `AbstractIGrapheTest` et valide le comportement (tous les tests passent).

**Exercice B – Algorithmes génériques**
- `AlgorithmesGraphe.dependantsDirects()` : retourne les entités qui dépendent statiquement de la cible (ignore `CONTIENT`).
- `AlgorithmesGraphe.dependantsElargis()` : remonte par conteneurs (types puis paquetage) sans dépasser le premier paquetage.
- Les algorithmes n’utilisent que l’interface `IGraphe`, aucune classe concrète.
- Tous les tests de `AbstractAlgorithmesGrapheTest` passent (classe `AlgorithmesGrapheTest`).

**Fonctionnalités annexes**
- Import et export au format PlantUML fonctionnels (`ImporteurPlantUml`, `ExportPlantUml`).
- Les tests d’import/export (`ImportExportPlantUmlTest`) sont tous verts.

### Ce qui ne marche pas (ou difficultés rencontrées)
Rien à signaler : toutes les fonctionnalités demandées sont fonctionneles et les tests associées réussissent intégralement.

### Structure du projet 
```
src/
├── graphe/
│ ├── ihm/
│ │ └── Main.java
│ ├── impl/
│ │ ├── Entite.java
│ │ ├── Graphe.java
│ │ ├── GrapheMap.java
│ │ ├── GrapheMatrice.java
│ │ └── TypeEntite.java
│ ├── modele/
│ │ ├── IEntite.java
│ │ ├── IGraphe.java
│ │ ├── IGrapheSprint1.java
│ │ ├── NatureRelation.java
│ │ ├── RelationEntrante.java
│ │ ├── RelationSortante.java
│ │ └── UtilsTestGraphe.java
│ └── outils/
│ ├── AlgorithmesGraphe.java
│ ├── AreteDejaExistante.java
│ ├── AreteNonExistante.java
│ ├── ExportPlantUml.java
│ ├── ImporteurPlantUml.java
│ ├── ParseException.java
│ ├── SommetExisteDeja.java
│ └── SommetExistePas.java
test/
├── graphe/
│ ├── impl/
│ │ ├── GrapheTest.java
│ │ ├── TestGrapheMap.java
│ │ └── TestGrapheMatrice.java
│ ├── modele/
│ │ ├── AbstractIGrapheTest.java
│ │ └── AssertsGraphe.java
│ └── outils/
│ ├── AbstractAlgorithmesGrapheTest.java
│ ├── AlgorithmesGrapheTest.java
│ ├── ImporteurPlantUmlTest.java
│ └── ImportExportPlantUmlTest.java

```