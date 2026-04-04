# SAE 2.02 – Exploration Algorithmique d'un Problème

## Composition de l'équipe 

### GROUPE : 108

| Nom |
|-----|
| SITTININAE Aditya |
| MOISANT Lény |
| TCHOUANGOU Louis |
| MUHAMMAD MAJHARUL ISLAM Shafi |


## Sprint 1 – Représentation des graphes

### Ce qui a été réalisé

Implémentation de deux classes représentant un **graphe orienté étiqueté**, toutes deux conformes à l'interface `IGraphe` :

- **`GrapheMap`** : représentation par liste d'adjacence via une `HashMap<String, HashMap<String, String>>`. Chaque sommet est associé à une map de ses successeurs avec l'étiquette de l'arête.
- **`GrapheMatrice`** : représentation par matrice d'adjacence via une `ArrayList<ArrayList<String>>`. Les sommets sont indexés dans une liste, et les cases de la matrice contiennent l'étiquette de l'arête (`null` si aucune arête).

Les deux implémentations supportent :
- l'ajout et la suppression de sommets et d'arêtes
- les arêtes avec ou sans étiquette
- la récupération des successeurs et prédécesseurs d'un sommet

### Tests

Les tests sont écrits avec **JUnit 5** dans deux classes distinctes :
- `TestGrapheMap` – tests pour `GrapheMap`
- `TestGrapheMatrice` – tests pour `GrapheMatrice`

Chaque méthode est couverte par au moins deux tests.

### Structure du projet

```
src/
├── Appli/
│   ├── IGraphe.java
│   ├── Main.java
│   └── Graphe/
│       ├── GrapheMap.java
│       └── GrapheMatrice.java
test/
├── TestGrapheMap.java
└── TestGrapheMatrice.java
```